package priv.brick.server;


import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import priv.brick.common.CompressSupport;
import priv.brick.common.SerializeSupport;
import priv.brick.protocol.MessageFrame;
import priv.brick.protocol.Request;
import priv.brick.protocol.Response;
import priv.brick.util.SingletonFactory;

import java.lang.reflect.Method;

import static priv.brick.common.Constants.MESSAGE_TYPE_RESP;

/**
 * @author : pangyan
 * @date : 2022/11/14
 * @description :
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ServiceProvider serviceProvider;

    public ServerHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        try {
            Channel channel = channelHandlerContext.channel();
            if (o instanceof MessageFrame) {
                log.info("server receive msg: {} ", o);
                MessageFrame requestFrame = (MessageFrame) o;
                Request request = (Request) requestFrame.getData();

                Class<?> serviceClazz = serviceProvider.getService(request.getInterfaceName());
                log.info("serviceProvider get={}", serviceClazz);
                Method method = serviceClazz.getDeclaredMethod(request.getMethodName(), request.getParamTypes()[0]);
                Object realCall = method.invoke(serviceClazz.newInstance(), request.getParameters());
                log.info("{} invoker success, result={}", serviceClazz, realCall);

                MessageFrame messageFrame = new MessageFrame();
                messageFrame.setRequestId(requestFrame.getRequestId());
                messageFrame.setSerializeType(SerializeSupport.Protostuff.getCode());
                messageFrame.setCompressType(CompressSupport.GZIP.getCode());
                messageFrame.setMessageType(MESSAGE_TYPE_RESP);
                Response<Object> realCallResponse = null;
                if (channel.isActive() && channelHandlerContext.channel().isWritable()) {
                    realCallResponse = Response.success(request.getRequestId(),realCall);
                    messageFrame.setData(realCallResponse);
                } else {
                    realCallResponse = Response.fail();
                    log.error("not writable now, message dropped");
                }

                channelHandlerContext.writeAndFlush(messageFrame).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("sever response message: [{}]", messageFrame);
                    } else {
                        future.channel().close();
                        log.error("sever response fail:", future.cause());
                    }
                });
//                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }

        }finally {
            ReferenceCountUtil.release(o);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }


}
