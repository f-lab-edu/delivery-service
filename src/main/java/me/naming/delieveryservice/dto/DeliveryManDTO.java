package me.naming.delieveryservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import me.naming.delieveryservice.utils.SHA256Util;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("DeliveryManDTO")
public class DeliveryManDTO {

  private int delieveryNum;

  @NotNull
  @Size(min = 5, max = 10)
  private String id;

  private String status;

  /**
   * 정규표현식 주요 내용
   * ^ : 행의 시작 / () : 여러 식을 묶을 수 있다.  (?=) : 전방탐색
   * 참고 - https://medium.com/@originerd/정규표현식-좀-더-깊이-알아보기-5bd16027e1e0
   */
  @NotNull
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$",
      message = "영문 대소문자, 특수문자, 숫자를 조합한 9~12자리의 비밀번호를 생성하세요")
  private String password;

  @NotNull
  @Size(min = 2, max = 5)
  private String name;

  @NotNull
  @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
  private String mobileNum;

  @NotNull
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate birthday;

  public void changeToEnryptPassword(String encryptPassword) {
    this.password = encryptPassword;
  }
}
