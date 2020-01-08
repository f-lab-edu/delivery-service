package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("OrderInfoDTO")
public class OrderInfoDTO {
  private int orderNum;

  // 배달(출발지, 도착지)주소
  private String userId;
  @NonNull private int departureCode;
  @NonNull private String departureDetail;
  @NonNull private int destinationCode;
  @NonNull private String destinationDetail;

  // 상품정보
  @NonNull private String category;
  @NonNull private String brandName;
  @NonNull private String productName;
  private String comment;

  public void setUserId(String userId) {
    this.userId = userId;
  }
}