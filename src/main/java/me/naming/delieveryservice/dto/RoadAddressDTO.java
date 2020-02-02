package me.naming.delieveryservice.dto;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

/**
 * 테이블(ADDRESS)에서 도로명 주소 정보를 받아오기 위한 DTO
 */
@Getter
@Alias("RoadAddressDTO")
public class RoadAddressDTO {

  private String cityName;
  private String countryName;
  private String roadName;
  private int buildingNumber;

}