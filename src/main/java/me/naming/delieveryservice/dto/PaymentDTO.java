package me.naming.delieveryservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * [1/27(금)]
 * 1. 클래스 설계 내용
 * 객체(ex. 카드, 계좌이체)의 공통된 속성을 하나의 추상 클래스(ex. 결제)에서 관리하고 상속시키기 위해 아래 코드와 같이 작성했습니다.
 * 상속을 통해 공통된 속성을 재사용 할 수 있으며, 확장성있게 사용 가능합니다.
 * 참고 : '스프링 입문을 위한 자바 객체 지향의 원리와 이해'(p.107, 상속 : 재사용 + 확장)
 *
 * 2. Validation 내용
 * Bean Validation은 애플리케이션에서 사용하는 객체 검증용 인터페이스이다.
 * JSR-380은 Bean Validation을 위한 자바 API이다. 요구사항으로는 Java 8버전 이상이 필요하며 다양한 타입(ex. Optional, LocalDate)을 지원해준다.
 * 각각의 인터페이스마다 사용 할 수 있는 데이터 타입이 다르니 아래 참고 사이트 중 'JSR-380 내용'을 확인해볼 것!!!
 * (ex. @NotEmpty는 int에서 사용 X)
 *
 * 참고
 *  Java Bean Validation : https://www.baeldung.com/javax-validation
 *  JSR-380 내용 : https://beanvalidation.org/2.0/spec/#builtinconstraints-notempty
 *  추가로 @NotNull, @NotEmpty, @NotBlank 차이 : https://www.baeldung.com/java-bean-validation-not-null-empty-blank
 *
 */
@Getter
@Alias("PaymentDTO")
public abstract class PaymentDTO {
  @NotNull private DeliveryPriceDTO.DeliveryType deliveryType;

  private int paymentNum;
  @NotNull private PaymentType paymentType;
  @NotNull @Positive private int amount;
  @Setter private int orderNum;

  public enum PaymentType {
    ACCOUNT,
    CARD
  }

  @Getter
  public static class Card extends PaymentDTO {
    @NotNull private String cardType;
    @NotNull private long cardNum;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul)")
    private LocalDate validDate;

    @NotNull @Pattern(regexp = "[0-9]{3}", message = "3자리의 숫자만 입력가능합니다")
    private String cvcNum;
  }

  @Getter
  public static class Account extends PaymentDTO {
    @NotNull private String bankName;
    @NotNull private long accountNum;

    @NotNull
    @Size(max = 10)
    private String accountName;
  }
}
