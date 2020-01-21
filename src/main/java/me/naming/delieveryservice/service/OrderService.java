package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired private OrderDao orderDao;

  public void reqOrder(String userId, int dprtCode, String dprtDetail, int dstinCode, String dstinDetail) {
    orderDao.reqOrder(userId, dprtCode, dprtDetail, dstinCode, dstinDetail);
  }
}
