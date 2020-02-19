package me.naming.delieveryservice.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeliveryPriceDTO {
  private final DeliveryType.Type deliveryType;
  private final int deliveryPrice;

  public enum DeliveryType{
    FAST,
    BASIC
  }
}
