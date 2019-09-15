package me.naming.delieveryservice.vo;

import org.apache.ibatis.type.Alias;

@Alias("UserVO")
public class UserVO {

    private int pid_num;
    private String id;
    private String password;

    @Override
    public String toString() {
        return "UserVO{" +
                "pid_num=" + pid_num +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getPid_num() {
        return pid_num;
    }

    public void setPid_num(int pid_num) {
        this.pid_num = pid_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
