package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.PaymentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentDao {
  int paymentInfo(PaymentDTO paymentDTO);

  void testDataDelete();
  int getCount();
}