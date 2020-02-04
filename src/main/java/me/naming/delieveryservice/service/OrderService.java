package me.naming.delieveryservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.api.KakaoAPI;
import me.naming.delieveryservice.dao.AddressDao;
import me.naming.delieveryservice.dao.FeeDao;
import me.naming.delieveryservice.dao.OrderDao;
import me.naming.delieveryservice.dao.PaymentDao;
import me.naming.delieveryservice.dto.CoordinatesDTO;
import me.naming.delieveryservice.dto.DeliveryPriceDTO;
import me.naming.delieveryservice.dto.FeeDTO;
import me.naming.delieveryservice.dto.OrderInfoDTO;
import me.naming.delieveryservice.dto.PaymentDTO;
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
   * [2/2(일)]
   * PaymentService(결제) 인터페이스로 추상화.
   * 결제라는 행위를 추상화시킴으로써 상태 값 비교 코드(ex. 카드, 계좌이체 등 결제 방식 비교 코드)를 삭제해 코드 가독성을 높일 수 있습니다.
   * if ~ else문, switch ~ case문과 같이 조건문을 사용하지 않고 인터페이스를 사용하는 이유는
   * 카드, 계좌이체 이외에 휴대폰, 포인트 결제 등 다양한 기능이 추가되었을 경우 해당 경우에 따라
   * 달라지는 비지니스 로직을 조건문 내에서 처리한다면 코드 가독성이 떨어지고,
   * 단일 책임 원칙을 지키지 못해 변경사항이 생길 경우 연쇄적인 코드 변경이 일어나
   * 유지보수 측면에서 비효율적이기 때문입니다.
   *
   * 따라서 아래 코드 내용과 같이 Controller에서 전략(Strategy)를 설정해주고,
   * PaymentService 컨텍스트를 통해 실행함으로써 코드 가독성을 높여주고,
   * 특정 상태 및 전략에 변경이 생길 경우 해당 클래스만 수정함으로써 보다 유연하게 대처 할 수 있도록 리팩토링 했습니다.
   *
   * @Transactional 동작방식
   * Transactional은 AOP 프록시에 의해서 처리됩니다.
   * 예외의 경우 TransactionInterceptor로 구성되어 있으며,
   * 비지니스 로직 실행 전 before에서는 트랜잭션 매니저(Transaction Manager)로부터 새로운 트랜잭션 생성여부를 결정하고,(비지니스 로직 존재 X)
   * 비지니스 로직 실행 후 after에서는 commit or roll back을 결정합니다.
   *
   * 추가적으로 스프링에서는 트랜잭션을 관리하기 위한 추상화된 개념을 제공(PlatformTransactionManager)함으로써
   * JTA, JPA, Hibernate 등 다양한 트랜잭션 API에 일관된 프로그래밍 모델링을 제공합니다.
   */
  @Transactional
  public void payment(PaymentService paymentService, PaymentDTO paymentDTO){
    orderDao.updateDeliveryType(paymentDTO);
    paymentDao.insertPaymentInfo(paymentDTO);
    paymentService.pay(paymentDTO);
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