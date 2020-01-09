package me.naming.delieveryservice.dto;

import lombok.Getter;

/**
 * 테이블(Address)에서 주소 정보를 갖고오기 위해 DTO 생성
 */
@Getter
public class AddressDTO {
  private String cityName;
  private String countryName;
  private String roadName;
  private int buildingNum;
  private int buildingNumSide;
  private String buildingName;
}