package me.naming.delieveryservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.api.KakaoAPI;
import me.naming.delieveryservice.dao.AddressDao;
import me.naming.delieveryservice.dao.OrderDao;
import me.naming.delieveryservice.dto.CoordinatesDTO;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import me.naming.delieveryservice.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OrderService {

  @Autowired private OrderDao orderDao;
  @Autowired private AddressDao addressDao;
  @Autowired private KakaoAPI kakaoAPI;

  public List<UserOrderListDTO> userOrderList(String userId){
    return orderDao.userOrderList(userId);
  }

  /**
   * 주문 정보 저장
   * 프런트에서 전송한 주문 정보를 활용해 좌표 값을 구한다.
   * 이후 좌표 값을 활용해 직선거리를 구한다(이때 직선거리는 km 단위이다)
   * 측정한 직선거리와 함께 주문정보를 저장한다.
   * 성공적으로 저장될 경우, 주문번호(orderNum)를 클라이언트에 전송한다
   *    -> 주문번호를 전송하는 이유는 이후 결제 프로세스에서 결제번호 기준으로 DB에 저장하기 위함이다.
   * @param orderInfoDTO
   * @return
   */
  public int orderInfo(OrderInfoDTO orderInfoDTO){

    HashMap<String,Object> departureInfoFromDB = addressDao.getAddressInfoByAddressCode(orderInfoDTO.getDepartureCode());
    HashMap<String,Object> destinationInfoFromDB = addressDao.getAddressInfoByAddressCode(orderInfoDTO.getDestinationCode());

    String departureAddress   = getRoadAddress(departureInfoFromDB);
    String destinationAddress = getRoadAddress(destinationInfoFromDB);

    CoordinatesDTO departureCoordinates = kakaoAPI.getCoordinatesByAddress(departureAddress);
    CoordinatesDTO destinationCoordinates = kakaoAPI.getCoordinatesByAddress(destinationAddress);

    double kmDistance =
        CommonUtil.kmDistanceByCoordinates(
            departureCoordinates.getLatitude(),
            departureCoordinates.getLongitude(),
            destinationCoordinates.getLatitude(),
            destinationCoordinates.getLongitude());

    orderInfoDTO.setDistance(kmDistance);
    orderDao.orderAddress(orderInfoDTO);
    orderDao.orderProduct(orderInfoDTO);

    return orderInfoDTO.getOrderNum();
  }

  /**
   * 도로명 주소 생성
   *  - HashMap Value 값을 연결하여 String 형태로 도로명 주소 생성
   * @param hashMap
   * @return
   */
  private String getRoadAddress(HashMap<String, Object> hashMap) {
    ArrayList arrayList = new ArrayList();
    StringBuilder stringBuilder = new StringBuilder();

    arrayList.addAll(hashMap.values());
    for(int i=0; i<arrayList.size(); i++) {
      stringBuilder.append(arrayList.get(i).toString());
    }
    return stringBuilder.toString();
  }

}