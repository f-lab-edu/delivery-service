package me.naming.delieveryservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@Getter
@Alias("Payment")
public class Payment {
  @NotNull private DeliveryPriceDTO.DeliveryType deliveryType;

  private int paymentNum;
  @NotNull private PaymentType paymentType;
  @NotNull @Positive private int amount;
  @Setter private int orderNum;

  public enum PaymentType {
    ACCOUNT,
    CARD
  }

  public Payment(@NotNull PaymentType paymentType, @NotNull int amount, int orderNum) {
    this.paymentType = paymentType;
    this.amount = amount;
    this.orderNum = orderNum;
  }

  @Getter
  public static class Card extends Payment {
    @NotNull private String cardType;
    @NotNull private long cardNum;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul)")
    private LocalDate validDate;

    @NotNull @Positive private int cvcNum;
  }

  @Getter
  public static class Account extends Payment {
    @NotNull private String bankName;
    @NotNull private long accountNum;

    @NotNull
    @Size(max = 10)
    private String accountName;
  }
}
