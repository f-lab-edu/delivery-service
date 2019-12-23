package me.naming.delieveryservice.dao;

import java.util.List;
import me.naming.delieveryservice.dto.AddressDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  void deliveryAddress(AddressDTO addressDTO);
  void deliveryProduct(String category, String brandName, String productName, String comment, int orderNum);
  List<UserOrderListDTO> userOrderList(String userId);
}
