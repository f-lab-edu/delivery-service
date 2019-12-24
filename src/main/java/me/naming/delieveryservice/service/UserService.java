package me.naming.delieveryservice.service;

import lombok.extern.slf4j.Slf4j;
import me.naming.delieveryservice.dao.UserDao;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.utils.SHA256Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("UserService")
public class UserService {

  @Autowired private UserDao userDao;

  /**
   * 고객 회원가입 메서드 - RuntimeException은 명시적인 예외처리를 강제하지 않기 때문에 언체크 예외처리라고 불린다. - catch문이나 throws를 선언하지
   * 않아도 된다. 프로그램 오류가 발생했을 경우에만 실행된다.
   *
   * @param userDTO 저장할 회원정보
   */
  public void insertUserInfo(UserDTO userDTO) {

    userDTO.setPassword(SHA256Util.encrypt(userDTO.getPassword()));

    int insertCount = userDao.insertUserInfo(userDTO);
    if (insertCount != 1) {
      throw new RuntimeException(
          "insertUserInfo ERROR! 필수 정보를 모두 기입해주세요! \n" + "Params : " + userDTO);
    }
  }

  /**
   * 로그인 메서드
   * @param id 사용자가 입력한 ID
   * @param password 사용자가 입력한 PWD. 암호화 하기 전
   * @return
   */
  public UserDTO userLogin(String id, String password) {

    String encryptPwd = SHA256Util.encrypt(password);
    UserDTO userDTO = userDao.userLogin(id, encryptPwd);

    if (userDTO == null)
      throw new RuntimeException("User Info is not exists. Check the Id or Password");

    return userDTO;
  }

  /**
   * ID 중복확인 메서드
   * @param id 사용자가 입력한 ID
   * @return
   */
  public boolean checkIdDuplicate(String id) {
    return userDao.checkIdDuplicate(id) == 1;
  }

  /**
   * 사용자 비밀번호 update 메서드
   * @param id
   * @param newPassword
   */
  public void updatePwd(String id, String newPassword) {

    String encryptPwd = SHA256Util.encrypt(newPassword);
    int udtResult = userDao.updatePwd(id, encryptPwd);
    if (udtResult == 0) throw new RuntimeException("User ID is not exists");
  }

  public UserDTO getUserInfo(String id) {

    UserDTO userDTO = userDao.getUserInfo(id);
    return userDTO;
  }

  /**
   * 메소드는 PATCH를 사용한다. 왜냐하면 회원 탈퇴 하더라도 회원정보가 DB에서 Delete되는것이 아니고 status 칼럼의 값만 변경되는 것이기 때문이다.
   * 따라서 사용자로부터 설정하려는 status 값을 받아오고, enum을 활용해 값이 올바르게 작성되었는지 체크한다.
   * @param id
   */
  public void changeUserStatus(String id, String status) {

    if (!StringUtils.equals(status, Status.DEFAULT.toString()) && !StringUtils.equals(status, Status.DELETE.toString()))
      throw new IllegalStateException("status값이 부적절합니다.");

    int udtResult = userDao.changeUserStatus(id, status);
    if (udtResult == 0) throw new RuntimeException("User ID is not exists");
  }

  enum Status {
    DELETE,
    DEFAULT
  }
}