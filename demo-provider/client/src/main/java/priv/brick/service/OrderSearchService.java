package priv.brick.service;

import priv.brick.dto.OrderDTO;

import java.util.List;

/**
 * @author : pangyan
 * @date : 2022/11/23
 * @description :
 */
public interface OrderSearchService {

    OrderDTO getOrder(Long orderId);

    List<OrderDTO> listOrder(List<Long> orderIds);
}
