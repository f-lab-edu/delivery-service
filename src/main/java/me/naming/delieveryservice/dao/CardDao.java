package me.naming.delieveryservice.dao;

import java.time.LocalDate;
import me.naming.delieveryservice.dto.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CardDao {
  void cardPayment(String cardType, long cardNum, LocalDate validDate, int cvcNum, int paymentNum);
}
