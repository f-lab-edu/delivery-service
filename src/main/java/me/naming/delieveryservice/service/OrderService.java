package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired private OrderDao orderDao;

  public void deliveryAddress(String userId, int departureCode, String departureDetail, int destinationCode, String destinationDetail) {
    orderDao.deliveryAddress(userId, departureCode, departureDetail, destinationCode, destinationDetail);
  }
}