package priv.brick.service;


import priv.brick.dto.OrderDTO;

import java.util.concurrent.TimeUnit;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class OrderManageServiceImpl implements OrderManageService{


   @Override
   public OrderDTO updateOrder(OrderDTO dto) {
      try {
         TimeUnit.MICROSECONDS.sleep(20);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }

      return dto;
   }
}
