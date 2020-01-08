package me.naming.delieveryservice.service;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.dao.OrderDao;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderService {

  @Autowired private OrderDao orderDao;

  public List<UserOrderListDTO> userOrderList(String userId){
    return orderDao.userOrderList(userId);
  }

  public int orderInfo(OrderInfoDTO orderInfoDTO) {
    orderDao.orderAddress(orderInfoDTO);
    orderDao.orderProduct(orderInfoDTO);

    return orderInfoDTO.getOrderNum();
  }
}