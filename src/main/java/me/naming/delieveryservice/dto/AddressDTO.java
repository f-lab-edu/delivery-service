package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("AddressDTO")
public class AddressDTO {
  @NonNull int departureCode;
  @NonNull String departureDetail;
  @NonNull int destinationCode;
  @NonNull String destinationDetail;
}