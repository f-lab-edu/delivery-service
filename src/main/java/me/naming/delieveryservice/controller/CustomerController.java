package me.naming.delieveryservice.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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
public class CustomerController {

  @Autowired private UserService userService;

  private static final ResponseEntity<ResponseResult> CREATE_SUCCESS = new ResponseEntity<>(ResponseResult.SUCCESS, HttpStatus.CREATED);
  private static final ResponseEntity<ResponseResult> OK_SUCCESS = new ResponseEntity<>(ResponseResult.SUCCESS, HttpStatus.OK);
  private static final ResponseEntity<ResponseResult> CONFLICT_DUPLICATE = new ResponseEntity<>(ResponseResult.DUPLICATE, HttpStatus.CONFLICT);

  /**
   * 고객 회원가입 메서드
   * - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
   * - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
   * @param userDTO 저장할 회원정보
   * @return
   */
  @PostMapping(value = "/signup")
  public ResponseEntity<ResponseResult> signUpUserInfo(@RequestBody UserDTO userDTO) {
    userService.insertUserInfo(userDTO);
    return CREATE_SUCCESS;
  }

  /**
   * ID 중복체크 메서드
   * @param id DB에서 조회할 사용자 ID
   * @return
   */
  @GetMapping(value = "/{id}/exists")
  public ResponseEntity<ResponseResult> checkIdDuplicate(@PathVariable String id) {

    boolean idCheck = userService.checkIdDuplicate(id);
    if (idCheck)
      return CONFLICT_DUPLICATE;

    return OK_SUCCESS;
  }

  /**
   * 로그인을 하기 위한 메서드
   * @param userLoginRequest 로그인(id, password) 정보
   * @param httpSession 세션 저장
   * @return
   */
  @PostMapping(value = "/login")
  public ResponseEntity<ResponseResult> userLogin(
      @RequestBody UserLoginRequest userLoginRequest, HttpSession httpSession) throws Exception {

    UserDTO userDTO = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword());
    httpSession.setAttribute("USER_ID", userDTO.getUserId());
    return OK_SUCCESS;
  }

  /**
   * 비밀번호를 변경하기 위한 메서드
   * @param userChgPwd
   * @param httpSession
   * @return
   */
  @PatchMapping(value = "/{id}/password")
  public ResponseEntity<ResponseResult> updatePwd(
      @RequestBody UserChgPwd userChgPwd, @PathVariable String id, HttpSession httpSession) {

    checkUserId(httpSession, id);
    userService.updatePwd(id, userChgPwd.getNewPassword());
    return OK_SUCCESS;
  }

  /**
   * 회원탈퇴
   * @param id
   * @param httpSession
   * @return
   */
  @DeleteMapping(value = "/{id}/info")
  public ResponseEntity<ResponseResult> deleteUserInfo(
      @PathVariable String id, @RequestBody ChangeUserStatus changeUserStatus, HttpSession httpSession) {

    checkUserId(httpSession, id);
    userService.changeUserStatus(id, changeUserStatus.getStatus());
    httpSession.invalidate();
    return OK_SUCCESS;
  }

  /**
   * 회원정보 조회
   * @param httpSession
   * @return
   */
  @GetMapping(value = "/{id}/info")
  public Resource<UserDTO> getUserInfo(@PathVariable String id, HttpSession httpSession) {

    checkUserId(httpSession, id);
    UserDTO userDTO = userService.getUserInfo(id);

    return new Resource<>(userDTO);
  }


  /**
   * 사용자 ID 체크
   * @param httpSession
   * @param id
   */
  private void checkUserId(HttpSession httpSession, String id) throws NullPointerException{

    if(httpSession.getAttribute("USER_ID") ==  null)
      throw new NullPointerException("Session('USER_ID') is not exists");

    if (!StringUtils.equals(httpSession.getAttribute("USER_ID").toString(), id))
      throw new IllegalArgumentException("Session('USER_ID') & ID is not same");
  }

  @RequiredArgsConstructor
  private static class ResponseResult {
    enum ResponseStatus{
      SUCCESS, FAIL, DUPLICATE
    }

    @NonNull
    private ResponseStatus result;

    private static ResponseResult SUCCESS = new ResponseResult(ResponseStatus.SUCCESS);
    private static ResponseResult FAIL = new ResponseResult(ResponseStatus.FAIL);
    private static ResponseResult DUPLICATE = new ResponseResult(ResponseStatus.DUPLICATE);
  }

  /**
   * 지정된 Body로 Request값을 설정할 때 장단점
   *
   * 장점
   *  - 지정한 Key 값이 존재하지 않을 때 NullPointerException 발생. -> 지정한 파라미터의 유효성 검사
   *  - UserDTO를 활용한 것이 아니라 따로 Request 타입을 생성함으로써 불필요한 변수 사용을 줄일 수 있다.
   *  - Value 값을 지정한 데이터 타입으로 받아올 수 있다.
   *    (ex. Client에서 "password"=123 으로 요청 시 자동적으로 형변환(int -> String)이 이뤄진다.)
   *  - 각 Request별 클래스를 따로 만들어 관리하지 않아 불필요한 인스턴스 생성을 방지한다.
   *  - 최초 JVM 실행 시 메모리 영역(static, heap, stack) 중 static 영역에 위치하며,
   *    외부 클래스와 독립적인 형태로 heap 영역에 인스턴스를 생성하고 GC에 의해 자동적으로 수거될 수 있다.
   *
   * 단점
   *  - 매번 각각의 Request 타입을 지정해야 한다.
   *
   * 기타내용
   * Inner Class(Nested Class) 종류는 총 4가지이다.
   * 정적 멤버 클래스(Static Inner class) / 멤버 클래스(Member class) / 지역 클래스(Local Class) / 익명 클래스(Anonymous class)이며,
   * 크게 정적 클래스(static)와 비정적 클래스(non-static)로 나뉘게 된다. (정적 클래스는 '정적 멤버 클래스', 비정적 클래스는 '정적 멤버 클래스'외 3가지)
   * 이 두가지의 차이점은
   *  1) 멤버 클래스 생성 방법과
   *  2) 외부 클래스의 변수와 메소드에 대한 접근이다.
   *
   * 정적 멤버 클래스(Static Inner class)는 외부 클래스의 인스턴스에 접근할 일이 없다면 무조건 사용한다.
   * 왜냐하면, static을 생략하게 될 경우 외부 인스턴스로의 숨은 참조를 갖게 되어 시간과 공간이 소비되고, 가비지 컬렉션에서 클래스의 인스턴스를 수거하지 못해 메모리 누수가 생길 수 있다.
   *
   * 참고. 이펙티브 자바(아이템24), https://itdoer.tistory.com/113
   */
  @Getter
  private static class UserLoginRequest {
    @NonNull String userId;
    @NonNull String password;
  }

  /**
   * 속성이 1개임에도 클래스로 감싸야 하는 이유.
   *  - 속성이 1개이지만, RequestBody 받을 때 Body 값을 Object나 JSON으로 하지 않고 클래스로 선엄함으로써 명확하게 어떤 데이터를 받아오는지 알 수 있다.
   */
  @Getter
  private static class UserChgPwd {
    @NonNull String newPassword;
  }

  @Getter
  private static class ChangeUserStatus {
    @NonNull
    String status;
  }
}
