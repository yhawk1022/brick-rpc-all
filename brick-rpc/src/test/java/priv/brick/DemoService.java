package priv.brick;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public interface DemoService {


    String hello(String str);

    Order getOrder(Long orderId);

    Order update(Order order);

    List<Order> searchOrder(List<Long> orderIds);

}
