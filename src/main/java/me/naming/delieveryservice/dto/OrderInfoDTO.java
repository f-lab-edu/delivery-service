package me.naming.delieveryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Builder
@Getter
@Alias("OrderInfoDTO")
public class OrderInfoDTO {
  private int orderNum;

  // 배달(출발지, 도착지)주소
  @NonNull private final String userId;
  @NonNull private final int departureCode;
  @NonNull private final String departureDetail;
  @NonNull private final int destinationCode;
  @NonNull private final String destinationDetail;

  // 상품정보
  @NonNull private final String category;
  @NonNull private final String brandName;
  @NonNull private final String productName;
  private String comment;

}