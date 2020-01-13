package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("PaymentDTO")
public class PaymentDTO {
  @NonNull private String paymentType;
  @NonNull private int amount;
  @NonNull private int orderNum;
  @NonNull private String deliveryType;
}
