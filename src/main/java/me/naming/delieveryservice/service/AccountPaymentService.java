package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.AccountDao;
import me.naming.delieveryservice.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountPaymentService implements PaymentService {

  @Autowired AccountDao accountDao;

  @Override
  public void pay(PaymentDTO paymentDTO) {
    accountDao.insertAccountTransferPayment(paymentDTO);
  }
}
