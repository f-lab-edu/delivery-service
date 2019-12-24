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
}
