package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.CardDao;
import me.naming.delieveryservice.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardPaymentService implements PaymentService {

  @Autowired CardDao cardDao;

  @Override
  public void pay(PaymentDTO paymentDTO) {
    cardDao.insertCardPayment(paymentDTO);
  }
}
