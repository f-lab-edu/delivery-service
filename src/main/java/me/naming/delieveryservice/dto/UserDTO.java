package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Getter
@Setter
@ToString(exclude = "password")
@Alias("UserDTO")
public class UserDTO extends ResourceSupport {

  @NonNull private String userId;
  @NonNull private String password;
  @NonNull private String mobileNum;

  private String name;
  private Date birthdate;

  public enum Status {
    DELETE("삭제"),
    DEFAULT("재사용");

    public String krName;

    Status(String krName) {
      this.krName = krName;
    }

    public String getKrName() {
      return krName;
    }

    public static Status check(String krName) {
      if (krName.equals(Status.DEFAULT.getKrName())) {
        return Status.DEFAULT;
      } else if (krName.equals(Status.DELETE.getKrName())) {
        return Status.DELETE;
      } else {
        throw new IllegalArgumentException("userStatus 값이 올바르지 않습니다.");
      }
    }
  }
}
