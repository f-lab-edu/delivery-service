package me.naming.delieveryservice.controller;

import java.net.URI;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.naming.delieveryservice.aop.UserIdObjParam;
import me.naming.delieveryservice.aop.UserIdParam;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.OrderService;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lombok을 활용한 생성자 자동생성
 *  - @NoArgsConstructor : 파라미터가 없는 기본 생성자 생성
 *  - @AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자 생성
 *  - @RequiredArgsConstructor : final or @NonNull인 필드 값만 파라미터로 받는 생성자 생성
 */
@RestController
@RequestMapping("/customers")
@Log4j2
public class CustomerController {

  @Autowired private UserService userService;
  @Autowired private OrderService orderService;

  /**
   * 고객 회원가입 메서드
   * - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
   * - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
   * @param userDTO 저장할 회원정보
   * @return
   */
  @PostMapping(value = "/signup")
  public ResponseEntity signUpUserInfo(@RequestBody UserDTO userDTO) {
    userService.insertUserInfo(userDTO);

    // http 헤더중 Location은 post 동작 완료 후 리다이렉션 URL을 지정해주는 정보이다. 즉, 어느 페이지로 이동할지를 알려주는 정보
    URI uri = ControllerLinkBuilder.linkTo(CustomerController.class).toUri();
    return ResponseEntity.created(uri).build();
  }

  /**
   * ID 중복체크 메서드
   * @param id DB에서 조회할 사용자 ID
   * @return
   */
  @GetMapping(value = "/{id}/exists")
  public ResponseEntity checkIdDuplicate(@PathVariable String id) {

    boolean idCheck = userService.checkIdDuplicate(id);
    if (idCheck)
      return ResponseEntity.status(HttpStatus.CONFLICT).build();

    return ResponseEntity.ok().build();
  }

  /**
   * 로그인을 하기 위한 메서드
   * @param userLoginRequest 로그인(id, password) 정보
   * @param httpSession 세션 저장
   * @return
   */
  @PostMapping(value = "/login")
  public ResponseEntity userLogin(HttpSession httpSession, @RequestBody UserLoginRequest userLoginRequest) throws Exception {

    UserDTO userDTO = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword());
    httpSession.setAttribute("USER_ID", userDTO.getUserId());

    return ResponseEntity.ok().build();
  }

  /**
   * 비밀번호를 변경하기 위한 메소드
   * @param userId
   * @param userChgPwd
   * @return
   */
  @UserIdObjParam
  @PatchMapping(value = "/password")
  public ResponseEntity updateUserInfo(String userId, @RequestBody UserChgPwd userChgPwd) {

    userService.updatePwd(userId, userChgPwd.getNewPassword());
    return ResponseEntity.ok().build();
  }

  /**
   * 회원탈퇴
   * @param httpSession
   * @return
   */
  @DeleteMapping(value = "/myinfo")
  public ResponseEntity deleteUserInfo(HttpSession httpSession) {

    String userId = httpSession.getAttribute("USER_ID").toString();
    userService.deleteUserInfo(userId);
    httpSession.invalidate();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 회원정보 조회
   * @param userId
   * @return
   */
  @UserIdParam
  @GetMapping(value = "/myinfo")
  public ResponseEntity userInfo(String userId) {

    UserDTO userDTO = userService.getUserInfo(userId);
    Link link = ControllerLinkBuilder.linkTo(CustomerController.class).slash("myinfo").withRel("DeleteUserInfo");
    userDTO.add(link);

    return ResponseEntity.ok(userDTO);
  }

  // --------------- Body로 Request 받을 데이터 지정 ---------------
  @Getter
  private static class UserLoginRequest {
    @NonNull String userId;
    @NonNull String password;
  }

  @Getter
  private static class UserChgPwd {
    @NonNull String newPassword;
  }
}
