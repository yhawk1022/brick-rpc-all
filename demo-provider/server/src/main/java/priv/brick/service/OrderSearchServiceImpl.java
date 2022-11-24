package priv.brick.service;

import priv.brick.dto.OrderDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class OrderSearchServiceImpl implements OrderSearchService {

    @Override
    public OrderDTO getOrder(Long orderId) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setAmount(new BigDecimal(123123));
        orderDTO.setStatus(0);
        return orderDTO;
    }

    @Override
    public List<OrderDTO> listOrder(List<Long> orderIds) {
        List<OrderDTO> orders = orderIds.stream().map(orderId -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(orderId);
            orderDTO.setAmount(new BigDecimal(123123));
            orderDTO.setStatus(1);
            return orderDTO;
        }).collect(Collectors.toList());
        return orders;
    }


}
