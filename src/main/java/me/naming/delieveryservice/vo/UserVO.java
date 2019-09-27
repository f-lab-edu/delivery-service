package me.naming.delieveryservice.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UserVO")
public class UserVO {

    private int pid_num;
    private String id;
    private String password;

}
