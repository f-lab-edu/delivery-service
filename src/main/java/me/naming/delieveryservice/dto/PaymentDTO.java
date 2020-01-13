package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.Alias;

@RequiredArgsConstructor
@Getter
@Alias("PaymentDTO")
public class PaymentDTO {
  private int paymentNum;
  @NonNull private String paymentType;
  @NonNull private int amount;
  @NonNull private int orderNum;
  @NonNull private String deliveryType;
}
