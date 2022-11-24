package priv.brick.common;

/**
 * @author : pangyan
 * @date : 2022/11/
 * @description :
 */
public interface Constants {

    /**
     *  4byte 消息长度
     *  4byte 请求id
     *  1byte  请求类型
     *  1byte 序列化类型
     *  1byte 压缩类型
     *  11byte
     */
    int HEAD_LENGTH = 11;

    byte MESSAGE_TYPE_REQ = 0;

    byte MESSAGE_TYPE_RESP = 1;

}
