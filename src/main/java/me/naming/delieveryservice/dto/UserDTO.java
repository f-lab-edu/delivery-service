package me.naming.delieveryservice.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UserDTO")
public class UserDTO {

    private int pid_num;
    private String id;
    private String password;

}
