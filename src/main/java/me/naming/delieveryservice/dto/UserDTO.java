package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Getter
@Setter
@Alias("UserDTO")
public class UserDTO extends ResourceSupport {
  private int userNum;
  @NonNull private String userId;
  @NonNull private String password;
  @NonNull private String mobileNum;

  private String name;
  private Date birthday;
}
