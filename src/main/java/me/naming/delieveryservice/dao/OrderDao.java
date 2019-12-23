package me.naming.delieveryservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  void deliveryAddress(String userId, int departureCode, String departureDetail, int destinationCode, String destinationDetail);
}
