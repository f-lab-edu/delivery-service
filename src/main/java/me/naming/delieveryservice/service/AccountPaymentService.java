package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.AccountTransferDao;
import me.naming.delieveryservice.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountPaymentService implements PaymentService {

  @Autowired AccountTransferDao accountTransferDao;

  @Override
  public void pay(PaymentDTO paymentDTO) {
    accountTransferDao.insertAccountTransferPayment(paymentDTO);
  }
}
