package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeliveryPriceDTO {
  private final DeliveryType deliveryType;
  private final int deliveryPrice;

  public enum DeliveryType{
    FAST,
    BASIC
  }
}
