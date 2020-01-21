package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

  int insertUserInfo(UserDTO userDTO);

  int checkIdDuplicate(String id);

  UserInfoDTO userLogin(String id, String password);

  int updatePwd(String id, String newPassword);

  int deleteUserInfo(String id);

  UserDTO getUserInfo(String id);
}
