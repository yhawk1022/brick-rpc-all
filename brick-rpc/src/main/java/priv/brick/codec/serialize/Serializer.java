package priv.brick.codec.serialize;

/**
 * @author : pangyan
 * @date : 2022/11/19
 * @description :
 */
public interface Serializer {


    byte[] serialize(Object obj);


    <T> T deserialize(byte[] bytes, Class<T> target);

}
