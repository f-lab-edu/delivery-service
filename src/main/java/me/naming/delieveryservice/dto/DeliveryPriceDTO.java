package me.naming.delieveryservice.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeliveryPriceDTO {
  private final DeliveryType.Type deliveryType;
  private final int deliveryPrice;

  @Getter
  public static class DeliveryType {
    public enum Type {
      빠른배송,
      일괄배송
    }

    @NotNull private Type type;

    public static final DeliveryType FAST = new DeliveryType(Type.빠른배송);
    public static final DeliveryType BASIC = new DeliveryType(Type.일괄배송);

    public DeliveryType(Type type) {
      this.type = type;
    }
  }
}
