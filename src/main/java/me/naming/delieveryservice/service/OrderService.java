package me.naming.delieveryservice.service;

import java.util.List;
import me.naming.delieveryservice.dao.OrderDao;
import me.naming.delieveryservice.dto.AddressDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired private OrderDao orderDao;

  public void deliveryAddress(AddressDTO addressDTO) {
    orderDao.deliveryAddress(addressDTO);
  }

  public void deliveryProduct(String category, String brandName, String productName, String comment, int orderNum){
    orderDao.deliveryProduct(category, brandName, productName, comment, orderNum);
  }

  public List<UserOrderListDTO> userOrderList(String userId){
    return orderDao.userOrderList(userId);
  }

}