package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeliveryPriceDTO {
  private final String deliveryType;
  private final int deliveryPrice;
}
