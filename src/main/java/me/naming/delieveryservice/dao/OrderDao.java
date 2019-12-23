package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.AddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  void deliveryAddress(AddressDTO addressDTO);
  void deliveryProduct(String category, String brandName, String productName, String comment, int orderNum);
}
