package me.naming.delieveryservice.dao;

import java.util.List;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.ProductInfoDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  void orderAddress(OrderInfoDTO orderInfoDTO);
  void orderProduct(OrderInfoDTO orderInfoDTO);
  List<UserOrderListDTO> userOrderList(String userId);
  List<ProductInfoDTO> productInfoDetail(int orderNum);
}