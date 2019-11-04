package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

    int insertUserInfo(UserDTO userDTO);

}
