package me.naming.delieveryservice.utils;

import javax.servlet.http.HttpSession;
import me.naming.delieveryservice.dto.UserInfoDTO;

public class SessionUtil {

  public static int getUserNum(HttpSession httpSession){
    UserInfoDTO userInfoDTO = (UserInfoDTO) httpSession.getAttribute("UserInfo");
    return userInfoDTO.getUserNum();
  }

  public static String getUserId(HttpSession httpSession){
    UserInfoDTO userInfoDTO = (UserInfoDTO) httpSession.getAttribute("UserInfo");
    return userInfoDTO.getUserId();
  }

}
