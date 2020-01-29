package me.naming.delieveryservice.dao;

import java.time.LocalDate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CardDao {
  void insertCardPayment(String cardType, long cardNum, LocalDate validDate, String cvcNum, int paymentNum);
}