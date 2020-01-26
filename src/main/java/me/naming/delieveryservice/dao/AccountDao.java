package me.naming.delieveryservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountDao {
  void setAccountPaymentInfo(String bankName, long accountNum, String accountName, int paymentNum);
}
