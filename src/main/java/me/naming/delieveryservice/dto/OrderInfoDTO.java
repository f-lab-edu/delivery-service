package me.naming.delieveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

/**
 * 주문정보(OrderInfo)를 전달해주기 위한 DTO
 *
 * @Builder 패턴을 사용하게된 이유
 *  - 문제점
 *    처음에 코드 작성시 OrderInfoDTO를 컨트롤러 단에서 @RequestBody 형태로 받아왔었지만, 이때 userId 값을 받아오지 않기 때문에 setUserId를 통해 userId 값을 입력해주고자 했습니다.
 *    하지만, 이럴 경우 원래 생성된 객체의 불변성이 깨지며, setUserId를 다른 개발자가 id 값을 변경할 수도 있다는 위험성이 있습니다.(-> 방어적 코딩이 안된다)
 *
 *  - 해결방안 1
 *    객체 복사(Cloneable)를 활용해 원래의 객체를 보존하고 setUserId를 통해 userId 값을 입력하려고 했으나 위의 문제점과 동일하게 setUserId 때문에 언제든지 데이터가 변경될 수 있다는 단점이 있습니다.
 *    따라서 Controller에서 필수 값으로 받아와야 할 RequestBody 형태(ex. OrderInfoRequest)를 만들어주고
 *    해당 데이터와 + userId를 Builder 패턴을 통해 OrderInfoDTO에 저장하기로 했습니다.
 *    이럴 경우, 안정적인(immutable) 객체를 생성 및 전달 할 수 있습니다.
 *
 *  - 추가 참고사항
 *    String은 immutable한 자료형이나 List는 아니다. Cloneable 사용 시 고려할것(참고. https://gyrfalcon.tistory.com/entry/Java-Tip-clone%EA%B3%BC-Cloneable)
 *
 * Soft Copy 적용하게된 이유
 *  - 기존 인스턴스가 갖고 있는 데이터와 추가해줘야 할 데이터(userId)를 활용해 새롭게 객체를 생성함으로써 원본 데이터를 유지할 수 있다.
 *    즉, 객체의 불변성(Immutable)을 유지하기 위해 Soft Copy를 적용하였다.
 *
 * Q)간단히 Setter를 적용해도 되지 않을까?
 *  - 멤버변수(userId)에 대한 Setter를 적용할 경우 객체의 불변성이 깨진다.
 *
 * Q)그러면 객체의 불변성을 유지하기 위해 Setter는 무조건 사용되지 말아야하는건가? Setter는 왜 필요할까?
 *  - Setter를 사용하는 이유는 멤버 변수에 값을 변경해줄 때 특정 조건을 만족할 시 값을 변경해주기 위해 사용된다.
 *    즉, 값을 내부에서 가공해 필드에 넣어줘야 하는 경우 사용한다.
 */
@AllArgsConstructor
@Getter
@Alias("OrderInfoDTO")
public class OrderInfoDTO {
  // OrderMapper(id=orderAddress)에서 selectKey의 keyProperty를 지정하기 위해 생성
  private int orderNum;

  // 배달(출발지, 도착지)주소
  private final String userId;
  @NonNull private final int departureCode;
  @NonNull private final String departureDetail;
  @NonNull private final int destinationCode;
  @NonNull private final String destinationDetail;

  // 상품정보
  @NonNull private final String category;
  @NonNull private final String brandName;
  @NonNull private final String productName;
  private String comment;

  public OrderInfoDTO copy(String userId){
    return new OrderInfoDTO(orderNum, userId, departureCode, departureDetail, destinationCode, destinationDetail, category, brandName, productName, comment);
  }

}