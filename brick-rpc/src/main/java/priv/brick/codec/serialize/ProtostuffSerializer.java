package priv.brick.codec.serialize;

import com.google.common.collect.Lists;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import priv.brick.codec.compress.GzipCompressor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : pangyan
 * @date : 2022/11/21
 * @description :
 */
public class ProtostuffSerializer implements Serializer {

    public static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        try {
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            bytes = ProtostuffIOUtil.toByteArray(obj,schema , BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

}
