package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface UserDao {

    List<UserDTO> userInfo();
    void insertUserInfo(String id, String password);

}
