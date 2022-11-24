package priv.brick;

import priv.brick.codec.serialize.ProtostuffSerializer;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class SerializeTest {

    public static void main(String[] args) {
        Order order = new Order();
        order.setOrderId(123123123L);
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        byte[] serializeBytes = serializer.serialize(order);

        Order deserialize = serializer.deserialize(serializeBytes, Order.class);
        System.out.println(deserialize);
    }
}
