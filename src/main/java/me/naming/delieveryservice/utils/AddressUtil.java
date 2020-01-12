package me.naming.delieveryservice.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressUtil {

  /**
   * 도로명 주소 생성
   *  - HashMap Value 값을 연결하여 String 형태로 도로명 주소 생성
   * @param hashMap
   * @return
   */
  public static String getRoadAddress(HashMap<String, Object> hashMap) {
    ArrayList arrayList = new ArrayList();
    StringBuilder stringBuilder = new StringBuilder();

    arrayList.addAll(hashMap.values());
    for(int i=0; i<arrayList.size(); i++) {
      stringBuilder.append(arrayList.get(i).toString());
    }
    return stringBuilder.toString();
  }

  /** 'AddressMapper'에서 'resultType'을 DTO 형태로 받아올 경우 도로명 주소 생성 함수 */
//  public static String getRoadAddress(RoadAddressDTO roadAddressDTO) {
//    StringBuilder stringBuilder = new StringBuilder();
//    Method[] methods = RoadAddressDTO.class.getMethods();
//
//    try {
//      for(int i=0; i<methods.length; i++) {
//        if ("get".equals(methods[i].getName().substring(0, 3)) && !methods[i].getName().equals("getClass")) {
//          stringBuilder.append(methods[i].invoke(roadAddressDTO).toString());
//        }
//      }
//    } catch (IllegalAccessException e) {
//      e.printStackTrace();
//      log.error(e);
//    } catch (InvocationTargetException e){
//      e.printStackTrace();
//      log.error(e);
//    }
//
//    return stringBuilder.toString();
//  }

}
