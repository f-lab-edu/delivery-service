package me.naming.delieveryservice.utils;

import me.naming.delieveryservice.dto.AddressDTO;

public class AddressUtil {

  /**
   * 도로명 주소 생성
   *  - DB로부터 받아온 데이터를 도로명 주소 값으로 변경
   * @param addressDTO
   * @return
   */
  public static String getRoadAddress(AddressDTO addressDTO) {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(addressDTO.getCityName());
    stringBuilder.append(" "+addressDTO.getCountryName());
    stringBuilder.append(" "+addressDTO.getRoadName());
    stringBuilder.append(" "+addressDTO.getBuildingNum());

    return stringBuilder.toString();
  }

}
