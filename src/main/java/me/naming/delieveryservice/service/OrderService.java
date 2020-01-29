package me.naming.delieveryservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.api.KakaoAPI;
import me.naming.delieveryservice.dao.AccountDao;
import me.naming.delieveryservice.dao.AddressDao;
import me.naming.delieveryservice.dao.CardDao;
import me.naming.delieveryservice.dao.FeeDao;
import me.naming.delieveryservice.dao.OrderDao;
import me.naming.delieveryservice.dao.PaymentDao;
import me.naming.delieveryservice.dto.CoordinatesDTO;
import me.naming.delieveryservice.dto.DeliveryPriceDTO;
import me.naming.delieveryservice.dto.FeeDTO;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.UserOrderListDTO;
import me.naming.delieveryservice.utils.AddressUtil;
import me.naming.delieveryservice.utils.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class OrderService {

  @Autowired private OrderDao orderDao;
  @Autowired private AddressDao addressDao;
  @Autowired private KakaoAPI kakaoAPI;
  @Autowired private FeeDao feeDao;
  @Autowired private PaymentDao paymentDao;
  @Autowired private CardDao cardDao;
  @Autowired private AccountDao accountDao;

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

    String departureAddress = AddressUtil.getRoadAddress(departureInfoFromDB);
    String destinationAddress = AddressUtil.getRoadAddress(destinationInfoFromDB);

    CoordinatesDTO departureCoordinates = kakaoAPI.getCoordinatesByAddress(departureAddress);
    CoordinatesDTO destinationCoordinates = kakaoAPI.getCoordinatesByAddress(destinationAddress);

    double kmDistance =
        DistanceUtil.kmDistanceByCoordinates(
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
   * 배달 거리에 따라 지불 금액 정보 제공
   * @param orderNum
   * @return
   */
  public List<DeliveryPriceDTO> getDeliveryPriceList(int orderNum) {

    float distance = orderDao.selectOrderDistance(orderNum);
    List<FeeDTO> feeDTOList = feeDao.selectFeeInfoList();
    FeeDTO generalFeeInfo = feeDTOList.get(0);      // '일괄배송'에 관한 요금 정보 get
    FeeDTO fastFeeInfo = feeDTOList.get(1);         // '빠른배송'에 관한 요금 정보 get

    DeliveryPriceDTO generalFee = calculateDeliveryFee(generalFeeInfo, distance);
    DeliveryPriceDTO fastFee = calculateDeliveryFee(fastFeeInfo, distance);

    List<DeliveryPriceDTO> paymentInfoList = new ArrayList<>();
    paymentInfoList.add(generalFee);
    paymentInfoList.add(fastFee);

    return paymentInfoList;
  }

  /**
   * 결제 금액 저장
   * - 배달종류 저장(ex. 빠른배송 / 일괄배송)
   * - Payment 테이블에 결제 정보 저장 후 생성된 pk(auto_increment)값을 리턴 받는다.
   * - 전달 받은 값(PAYMENT의 payment_num(pk))과 함께 Card or Account 테이블에 결제 정보를 저장합니다.
   *
   * [1/29(수)]
   * 유지보수 편의성을 위해(결제 수단이 늘어날 경우를 대비해) 코드 재수정
   * 결제라는 공통된 관심 영역에서 '배달타입 변경'과 '결제정보 입력'이라는 구체적인 관심사를 하나의 메서드(addPaymentInfo)에서 실행시킬 수 있도록 작성했습니다.
   * 하지만, 매개변수 타입인 결제 종류(ex. 카드, 계좌이체)에 따라 메서드가 추가되어 어떻게 모델링 할 것인지 고민이었습니다.
   * 따라서 제네릭 타입으로 매개변수를 받아올 수 있도록 변경하고 Mapper에서 parameterType을 지정하여 코드를 수정했습니다.
   *
   * @param paymentType
   * @param <T>
   */
  @Transactional
  public <T> void addPaymentInfo(T paymentType) {
    orderDao.updateDeliveryType(paymentType);
    paymentDao.insertPaymentInfo(paymentType);

    switch (paymentType.getClass().getSimpleName()) {
      case "Account":
        accountDao.insertAccountTransferPayment(paymentType);
        break;

      case "Card":
        cardDao.insertCardPayment(paymentType);
        break;

      default:
        throw new IllegalArgumentException("존재하지 않는 Class 입니다");
    }
  }

  /**
   * 배송가격 계산
   * @param feeDTO
   * @param distance
   * @return
   */
  private DeliveryPriceDTO calculateDeliveryFee(FeeDTO feeDTO, float distance) {

    // 거리가 기본거리(5km)이내인 경우
    if(distance <= feeDTO.getBasicDistance()) {
      return new DeliveryPriceDTO(feeDTO.getDeliveryType(), feeDTO.getBasicFee());
    }

    // 거리가 기본거리(5km)이상인 경우 추가 요금 계산
    int extraDistanceCount = (int)Math.ceil((distance - feeDTO.getBasicDistance()) / feeDTO.getExtraDistance());
    int extraPrice = extraDistanceCount * feeDTO.getExtraFee();
    int deliveryPrice = extraPrice + feeDTO.getBasicFee();

    return new DeliveryPriceDTO(feeDTO.getDeliveryType(), deliveryPrice);
  }
}