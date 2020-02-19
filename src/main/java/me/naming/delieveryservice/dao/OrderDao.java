package me.naming.delieveryservice.dao;

import java.util.List;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.PaymentDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  void orderAddress(OrderInfoDTO orderInfoDTO);
  void orderProduct(OrderInfoDTO orderInfoDTO);
  void updateDeliveryType(PaymentDTO paymentType);
  List<UserOrderListDTO> userOrderList(String userId);

  float selectOrderDistance(int orderNum);
}