package me.naming.delieveryservice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

/**
 * Lombok을 활용한 생성자 자동생성
 *  - @NoArgsConstructor        : 파라미터가 없는 기본 생성자 생성
 *  - @AllArgsConstructor       : 모든 필드 값을 파라미터로 받는 생성자 생성
 *  - @RequiredArgsConstructor  : final or @NonNull인 필드 값만 파라미터로 받는 생성자 생성
 */
@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private UserService userService;

    private static final ResponseEntity CREATE_SUCCESS = new ResponseEntity<String>("SUCCESS", HttpStatus.CREATED);

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
     * @param userDTO 저장할 회원정보
     * @return
     */
    @PostMapping(value = "/signup" )
    public ResponseEntity<SignUpResponse> signUpUserInfo(@RequestBody UserDTO userDTO) {

        userService.insertUserInfo(userDTO);
        return CREATE_SUCCESS;
    }

    /**
     * ID 중복체크 메서드
     * @param id DB에서 조회할 사용자 ID
     * @return
     */
    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<CustomerIdDuplResponse> checkIdDuplicate(@PathVariable String id) {

        ResponseEntity<CustomerIdDuplResponse>  responseResponseEntity;
        CustomerIdDuplResponse                  customerIdDuplResponse;

        boolean idCheck = userService.checkIdDuplicate(id);
        if(idCheck) {
            customerIdDuplResponse = CustomerIdDuplResponse.DUPLICATED;
            responseResponseEntity = new ResponseEntity<>(customerIdDuplResponse, HttpStatus.CONFLICT);
            return responseResponseEntity;
        }

        customerIdDuplResponse = CustomerIdDuplResponse.SUCCESS;
        responseResponseEntity = new ResponseEntity<>(customerIdDuplResponse, HttpStatus.OK);
        return responseResponseEntity;
    }

    /**
     * 로그인을 하기 위한 메서드
     * @param userLoginRequest 로그인(id, password) 정보
     * @param httpSession      세션 저장
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpSession httpSession) throws Exception {

        UserDTO userDTO             = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword());
        LoginResponse loginResponse = LoginResponse.success(userDTO);
        httpSession.setAttribute("USER_ID", userDTO.getUserId());

        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
    }

    /**
     * 비밀번호를 변경하기 위한 메서드
     * @param userChgPwd
     * @param httpSession
     * @return
     */
    @PatchMapping(value = "/password")
    public ResponseEntity<DbResponse> updatePwd(@RequestBody UserChgPwd userChgPwd, HttpSession httpSession) {

        ResponseEntity<DbResponse> responseEntity;
        DbResponse dbResponse;

        String userId = httpSession.getAttribute("USER_ID").toString();
        if(userId == null) {
            dbResponse = DbResponse.NO_DATA;
            responseEntity = new ResponseEntity<>(dbResponse, HttpStatus.UNAUTHORIZED);
            return responseEntity;
        }

        userService.updatePwd(userId, userChgPwd.getNewPassword());
        dbResponse = DbResponse.SUCCESS;
        responseEntity = new ResponseEntity<>(dbResponse, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 회원탈퇴
     * @param id
     * @param httpSession
     * @return
     */
    @DeleteMapping(value = "/{id}/info")
    public ResponseEntity<DbResponse> deleteUserInfo(@PathVariable String id,  HttpSession httpSession) {

        ResponseEntity<DbResponse> responseEntity;
        String sessionId = httpSession.getAttribute("USER_ID").toString();

        if(!sessionId.equals(id)) {
            responseEntity = new ResponseEntity<>(DbResponse.ERROR, HttpStatus.FORBIDDEN);
            return responseEntity;
        }

        userService.deleteUserInfo(id);
        httpSession.invalidate();

        responseEntity = new ResponseEntity<>(DbResponse.SUCCESS, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 회원정보 조회
     * @param httpSession
     * @return
     */
    @GetMapping(value = "/{id}/info")
    public Resource<UserDTO> getUserInfo(@PathVariable String id, HttpSession httpSession) {

        String sessionId    = httpSession.getAttribute("USER_ID").toString();
        UserDTO userDTO     = userService.getUserInfo(id);

        // 여기서도 위의 탈퇴(DeleteMapping)와 같이 PathVariable을 통해 받아온 id 값을 Session 값과 비교하고 싶은데,
        // Return 값( Resource<UserDTO> )을 무엇으로 지정해야 할지 모르겠습니다. 이럴 경우 어떻게 처리해야 하는게 좋을까요? 단순히 return 값을 Object로 하는 것이 아니라 다른 일관된 방법이 있을까요?

        Link link = ControllerLinkBuilder.
                linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).deleteUserInfo(id, httpSession))
                .withRel("DeleteUserInfo");

        return new Resource<>(userDTO, link);
    }

    @Getter
    @RequiredArgsConstructor
    private static class DbResponse {
        enum DbStatus {
            SUCCESS, FAIL, ERROR, NO_DATA
        }

        @NonNull
        private DbStatus result;

        private static final DbResponse SUCCESS     = new DbResponse(DbStatus.SUCCESS);
        private static final DbResponse FAIL        = new DbResponse(DbStatus.FAIL);
        private static final DbResponse NO_DATA     = new DbResponse(DbStatus.NO_DATA);
        private static final DbResponse ERROR       = new DbResponse(DbStatus.ERROR);
    }

    @Getter
    @RequiredArgsConstructor
    private static class SignUpResponse {
        enum SignUpStatus {
            SUCCESS, ID_DUPLICATED, ERROR, NULL_ARGUMENT
        }

        @NonNull
        private SignUpStatus result;

        private static final SignUpResponse SUCCESS         = new SignUpResponse(SignUpStatus.SUCCESS);
        private static final SignUpResponse ID_DUPLICATED   = new SignUpResponse(SignUpStatus.ID_DUPLICATED);
    }

    @Getter
    @RequiredArgsConstructor
    private static class CustomerIdDuplResponse {
        enum DuplStatus {
            SUCCESS, ID_DUPLICATED, ERROR
        }

        @NonNull
        private DuplStatus result;

        private static final CustomerIdDuplResponse SUCCESS     = new CustomerIdDuplResponse(DuplStatus.SUCCESS);
        private static final CustomerIdDuplResponse DUPLICATED  = new CustomerIdDuplResponse(DuplStatus.ID_DUPLICATED);
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    private static class LoginResponse {
        enum LogInStatus {
            SUCCESS, FAIL, DELETED, ERROR
        }

        @NonNull
        private LogInStatus result;
        private UserDTO memberInfo;

        // success의 경우 memberInfo의 값을 set해줘야 하기 때문에 new 하도록 해준다.

        private static final LoginResponse FAIL = new LoginResponse(LogInStatus.FAIL);
        private static final LoginResponse DELETED = new LoginResponse(LogInStatus.DELETED);

        private static LoginResponse success(UserDTO userDTO) {
            return new LoginResponse(LogInStatus.SUCCESS, userDTO);
        }
    }

    //--------------- Body로 Request 받을 데이터 지정 ---------------
    @Getter
    @Setter
    private static class UserLoginRequest {
        @NonNull
        String userId;
        @NonNull
        String password;
    }

    @Getter
    @Setter
    private static class UserChgPwd {
        @NonNull
        String newPassword;
    }

}