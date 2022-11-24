package priv.brick.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : pangyan
 * @date : 2022/11/10
 * @description :
 *
 *  4byte 消息长度
 *  4byte 请求id
 *  1byte  请求类型
 *  1byte 序列化类型
 *  1byte 是否压缩
 *  11byte
 */
@Data
public class MessageFrame implements Serializable {

    private int requestId;

    private byte messageType;

    private byte serializeType;

    private byte compressType;

    private Object data;
}
