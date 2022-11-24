package priv.brick;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import priv.brick.config.Config;
import priv.brick.proxy.ProxyFactory;

import java.util.List;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
@Slf4j
public class ClientTest {
    public static void main(String[] args) {
        Config.init();
        DemoService demoService = ProxyFactory.proxy(DemoService.class);
        String hello = demoService.hello("111");
        log.info("hello resp={}", hello);

        Order order = demoService.getOrder(1231321L);
        log.info("getOrder resp={}", JSON.toJSONString(order));

        Order req = new Order();
        req.setOrderId(123802187309128L);
        req.setAddress("望京");
        req.setName("pangyan");
        Order res = demoService.update(req);
        log.info("update resp={}", JSON.toJSONString(res));


        List<Order> orders = demoService.searchOrder(Lists.newArrayList(1L,2L,3L,4L));
        log.info("searchOrder resp={}", JSON.toJSONString(orders));

    }
}
