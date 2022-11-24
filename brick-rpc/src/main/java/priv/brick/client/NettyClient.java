package priv.brick.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import priv.brick.codec.MessageFrameDecoder;
import priv.brick.codec.MessageFrameEncoder;
import priv.brick.common.CompressSupport;
import priv.brick.common.SerializeSupport;
import priv.brick.protocol.MessageFrame;
import priv.brick.protocol.Request;
import priv.brick.protocol.Response;
import priv.brick.util.SingletonFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static priv.brick.common.Constants.MESSAGE_TYPE_REQ;

/**
 * @author : pangyan
 * @date : 2022/11/12
 * @description :
 */
@Slf4j
public class NettyClient {
    private Bootstrap bootstrap;
    LocalConfigDiscovery configDiscovery;
    ChannelPool channelPool;

    RequestPool requestPool;

    private final static AtomicInteger requestId = new AtomicInteger(0);

    public NettyClient(){
        configDiscovery = SingletonFactory.getInstance(LocalConfigDiscovery.class);
        channelPool = SingletonFactory.getInstance(ChannelPool.class);
        requestPool =  SingletonFactory.getInstance(RequestPool.class);

        NioEventLoopGroup singleGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(singleGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(
                                new MessageFrameEncoder(),
                                new MessageFrameDecoder(),
                                new ClientHandler(requestPool)
                                );
                    }
                });
    }




    public Object sendRequest(Request request) {
        // build return value
        CompletableFuture<Response> resultFuture = new CompletableFuture<>();
        // get server address
        InetSocketAddress inetSocketAddress = configDiscovery.discovery(request);
        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // put unprocessed request
            requestPool.put(request.getRequestId(), resultFuture);
            MessageFrame messageFrame = new MessageFrame();
            messageFrame.setSerializeType(SerializeSupport.Protostuff.getCode());
            messageFrame.setCompressType(CompressSupport.GZIP.getCode());
            messageFrame.setMessageType(MESSAGE_TYPE_REQ);
            messageFrame.setData(request);
            messageFrame.setRequestId(requestId.getAndIncrement());

            channel.writeAndFlush(messageFrame).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("channel={}, client send message: {}",channel, messageFrame);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelPool.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelPool.putChannel(inetSocketAddress, channel);
        }
        return channel;
    }


    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    
}
