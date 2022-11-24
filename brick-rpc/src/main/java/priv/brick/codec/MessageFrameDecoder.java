package priv.brick.codec;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import priv.brick.codec.compress.GzipCompressor;
import priv.brick.codec.serialize.ProtostuffSerializer;
import priv.brick.common.CompressSupport;
import priv.brick.protocol.MessageFrame;
import priv.brick.protocol.Request;
import priv.brick.protocol.Response;
import priv.brick.util.SingletonFactory;

import static priv.brick.common.Constants.HEAD_LENGTH;
import static priv.brick.common.Constants.MESSAGE_TYPE_REQ;

/**
 * @author : pangyan
 * @date : 2022/11/20
 * @description :
 */
@Slf4j
public class MessageFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     *
     * @param maxFrameLength 单个帧最大长度
     * @param lengthFieldOffset 开始读数据的偏移量
     * @param lengthFieldLength 长度字段length的长度
     * @param lengthAdjustment 长度字段占的字节数
     * @param initialBytesToStrip 要读内容需要跳过的字节数
     * @param failFast true:超过maxFrameLength立抛出异常;false:读完再抛异常
     *e
     */
    public MessageFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }


    /**
     * MessageFrame
     *  4byte 消息长度
     *  4byte 请求id
     *  1byte  请求类型
     *  1byte 序列化类型
     *  1byte 是否压缩
     *  11byt
     */
    public MessageFrameDecoder(){
        this(Integer.MAX_VALUE, 0, 4, -4, 0, true);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= HEAD_LENGTH) {
                try {
                    int msgLength = frame.readInt();
                    int requestId = frame.readInt();
                    byte messageType = frame.readByte();
                    byte serializeType = frame.readByte();
                    byte compressType = frame.readByte();

                    MessageFrame messageFrame = new MessageFrame();
                    messageFrame.setRequestId(requestId);
                    messageFrame.setMessageType(messageType);
                    messageFrame.setSerializeType(serializeType);
                    messageFrame.setCompressType(compressType);
                    log.info("decode messageFrame={}", JSON.toJSONString(messageFrame));

                    int dataLength = msgLength - HEAD_LENGTH;
                    if (dataLength > 0) {
                        byte[] bs = new byte[dataLength];
                        frame.readBytes(bs);
                        // decompress the bytes

                        GzipCompressor compressor = SingletonFactory.getInstance(GzipCompressor.class);
                        ProtostuffSerializer serializer = SingletonFactory.getInstance(ProtostuffSerializer.class);

                        bs = compressor.decompress(bs);
                        // deserialize the object

                        if (messageType == MESSAGE_TYPE_REQ) {
                            Request tmpValue = serializer.deserialize(bs, Request.class);
                            messageFrame.setData(tmpValue);
                        } else {
                            log.info("msg type = 1");
                            Response tmpValue = serializer.deserialize(bs, Response.class);
                            messageFrame.setData(tmpValue);
                        }
                    }
                    return messageFrame;
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }


}
