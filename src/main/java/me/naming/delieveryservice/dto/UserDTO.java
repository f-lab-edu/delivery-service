package me.naming.delieveryservice.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(exclude = "password")
@Alias("UserDTO")
public class UserDTO {

    private int pidNum;

    @NonNull
    private String id;
    @NonNull
    private String password;
    @NonNull
    private String mobileNum;

    private String name;
    private int birthdate;

}
