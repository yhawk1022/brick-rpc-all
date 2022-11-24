package priv.brick.codec;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import priv.brick.codec.compress.Compressor;
import priv.brick.codec.compress.GzipCompressor;
import priv.brick.codec.serialize.ProtostuffSerializer;
import priv.brick.common.CompressSupport;
import priv.brick.common.SerializeSupport;
import priv.brick.protocol.MessageFrame;
import priv.brick.util.SingletonFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static priv.brick.common.Constants.*;

/**
 * @author : pangyan
 * @date : 2022/11/20
 * @description :
 */
@Slf4j
public class MessageFrameEncoder extends MessageToByteEncoder<MessageFrame> {


    /**
     * MessageFrame
     *  4byte 消息长度
     *  4byte 请求id
     *  1byte  请求类型
     *  1byte 序列化类型
     *  1byte 压缩类型
     *  11byt
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageFrame messageFrame, ByteBuf byteBuf)  {
        try {
            ProtostuffSerializer serializer = SingletonFactory.getInstance(ProtostuffSerializer.class);
            Compressor compressor = SingletonFactory.getInstance(GzipCompressor.class);

            log.info("encode messageFrame={}", JSON.toJSONString(messageFrame));
            byte[] data = compressor.compress(serializer.serialize(messageFrame.getData()));
            int dataLength = 0;

            if (data != null){
                dataLength = data.length;
            }
            int msgLength = HEAD_LENGTH + dataLength;

            byteBuf.writeInt(msgLength);
            byteBuf.writeInt(messageFrame.getRequestId());
            byteBuf.writeByte(messageFrame.getMessageType());
            byteBuf.writeByte(SerializeSupport.Protostuff.getCode());
            byteBuf.writeByte(CompressSupport.GZIP.getCode());
            byteBuf.writeBytes(data);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
