package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentDao {
  int paymentInfo(Payment payment);
}