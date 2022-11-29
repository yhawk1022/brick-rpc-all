package consumer;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import priv.brick.config.Config;
import priv.brick.dto.OrderDTO;
import priv.brick.proxy.ProxyFactory;
import priv.brick.service.OrderManageService;
import priv.brick.service.OrderSearchService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : pangyan
 * @date : 2022/11/23
 * @description :
 */
public class Application {

    public static void main(String[] args) {
        //第一步，初始化配置,非maven打包，需要先执行一次，再手动放到target/classes下
        Config.init();

        //第二步，创建代理类
        OrderSearchService orderSearchService = ProxyFactory.proxy(OrderSearchService.class);
        OrderDTO order = orderSearchService.getOrder(123123L);
        System.out.println("getOrder:"+JSON.toJSONString(order));

        List<OrderDTO> orders = orderSearchService.listOrder(Lists.newArrayList(1l,2l,3l,4l));
        System.out.println("listOrder:"+JSON.toJSONString(orders));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(1);
        orderDTO.setOrderId(123123L);
        orderDTO.setAmount(new BigDecimal("100.23"));
        OrderManageService orderManageService = ProxyFactory.proxy(OrderManageService.class);
        OrderDTO respDTO = orderManageService.updateOrder(orderDTO);
        System.out.println("updateOrder:"+JSON.toJSONString(respDTO));

    }
}
