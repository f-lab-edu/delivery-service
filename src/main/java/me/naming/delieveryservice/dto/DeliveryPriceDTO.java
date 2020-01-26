package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeliveryPriceDTO {
  private final String deliveryType;
  private final int deliveryPrice;

  public enum DeliveryType{
    빠른배송,
    일괄배송
  }
}
