package me.naming.delieveryservice.dto;

import lombok.Getter;

/**
 * FEE 테이블에서 데이터를 갖고오기 위한 DTO
 */
@Getter
public class FeeDTO {

  private String deliveryType;
  private float basicDistance;
  private int basicFee;
  private float extraDistance;
  private int extraFee;

  private int deliveryPrice;

  // 고객이 지불해야 할 금액을 저장하기 위한 메소드
  public FeeDTO copyDeliveryPrice(int deliveryPrice){
    this.deliveryPrice = deliveryPrice;
    return this;
  }
}