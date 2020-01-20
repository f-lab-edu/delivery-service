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
   * 메소드는 Patch가 아닌 Delete를 사용해도 무관하다.
   * 아래 메소드의 매개변수는 id 값만 받아오며, User의 Status 값을 변경하는 것이기 때문에
   *
   * enum을 사용하는 이유
   *    - 코드가 단순해지고 가독성이 좋아진다.
   *    - 인스턴스 생성과 상속을 방지한다.
   *    - 구현의 의도가 분명하게 나타날 수 있다.
   * @param id
   */
  public void changeUserStatus(String id) {

    int udtResult = userDao.changeUserStatus(id, UserDTO.Status.DELETE);
    if (udtResult == 0) throw new RuntimeException("User ID is not exists");
  }
}