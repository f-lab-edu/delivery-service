package me.naming.delieveryservice.dto;

import lombok.Data;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Data
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
