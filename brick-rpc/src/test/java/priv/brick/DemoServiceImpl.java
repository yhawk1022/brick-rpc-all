package priv.brick;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class DemoServiceImpl implements DemoService{
    @Override
    public String hello(String str) {
        return "i am server";
    }

    @Override
    public Order getOrder(Long orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setAddress("朝阳区");
        order.setName("pangyan");
        return null;
    }

    @Override
    public Order update(Order order) {

        return order;
    }

    @Override
    public List<Order> searchOrder(List<Long> orderIds) {
        List<Order> orders = orderIds.stream().map(orderId -> {
            Order order = new Order();
            order.setOrderId(orderId);
            order.setName("pang");
            order.setAddress("朝阳区");
            return order;
        }).collect(Collectors.toList());

        return orders;
    }
}
