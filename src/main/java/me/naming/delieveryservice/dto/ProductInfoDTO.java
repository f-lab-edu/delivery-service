package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("ProductInfoDTO")
public class ProductInfoDTO {
  @NonNull String category;
  @NonNull String brandName;
  @NonNull String productName;
  String comment;
  @NonNull int orderNum;
}