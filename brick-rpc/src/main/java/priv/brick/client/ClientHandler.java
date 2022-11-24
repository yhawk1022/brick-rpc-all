package priv.brick.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import priv.brick.protocol.MessageFrame;
import priv.brick.protocol.Response;
import priv.brick.util.SingletonFactory;


/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
@Slf4j
public class ClientHandler  extends ChannelInboundHandlerAdapter {

    private RequestPool requestPool;

    public ClientHandler(RequestPool requestPool) {
        this.requestPool = requestPool;
    }

    /**
     * Read the message transmitted by the server
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("client receive msg: [{}]", msg);
            if (msg instanceof MessageFrame) {
                MessageFrame tmp = (MessageFrame) msg;
                Response<Object> Response = (Response<Object>) tmp.getData();
                requestPool.complete(Response);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
