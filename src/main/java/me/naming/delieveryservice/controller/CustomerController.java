package me.naming.delieveryservice.controller;


import jdk.internal.org.objectweb.asm.commons.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.List;

/**
 * Lombok을 활용한 생성자 자동생성
 *  - @NoArgsConstructor 파라미터가 없는 기본 생성자 생성
 *  - @AllArgsConstructor 모든 필드 값을 파라미터로 받는 생성자 생성
 *  - @RequiredArgsConstructor final or @NonNull인 필드 값만 파라미터로 받는 생성자 생성
 */
@Slf4j
@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    @Autowired
    private UserService userService;

    private static final ResponseEntity createSuccess = new ResponseEntity<String>("SUCCESS", HttpStatus.CREATED);

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
     * @param userDTO 저장할 회원정보
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/signup" )
    public ResponseEntity<SignUpResponse> signUpUserInfo(@RequestBody UserDTO userDTO) {
        ResponseEntity<SignUpResponse> responseEntity = null;
        userService.insertUserInfo(userDTO);
        responseEntity = createSuccess;
        return responseEntity;
    }

    /**
     * ID 중복체크 메서드
     * @param id DB에서 조회할 사용자 ID
     * @return
     */
    @GetMapping(value = "/duplicate")
    public ResponseEntity<CustomerIdDuplResponse> checkIdDuplicate(@RequestParam String id) {

        ResponseEntity<CustomerIdDuplResponse> responseResponseEntity;
        CustomerIdDuplResponse customerIdDuplResponse;
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

        ResponseEntity<LoginResponse> rspResponseEntity;
        LoginResponse loginResponse;

        UserDTO userDTO = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword());
        loginResponse   = LoginResponse.success(userDTO);
        httpSession.setAttribute("USER_ID", userDTO.getUserId());
        rspResponseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        return rspResponseEntity;
    }

    /**
     * 비밀번호를 변경하기 위한 메소드
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
     * 사용자 정보 변경
     * @param httpSession
     * @return
     */
    @DeleteMapping(value = "/info")
    public ResponseEntity<DbResponse> deleteUserInfo(HttpSession httpSession) {
        ResponseEntity<DbResponse> responseEntity;
        DbResponse dbResponse;

        String id = httpSession.getAttribute("USER_ID").toString();
        userService.deleteUserInfo(id);

        dbResponse = DbResponse.SUCCESS;
        httpSession.invalidate();
        responseEntity = new ResponseEntity<>(dbResponse, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/info")
    public Resource<UserDTO> getUserInfo(HttpSession httpSession) {

        String id = httpSession.getAttribute("USER_ID").toString();
        UserDTO userDTO = userService.getUserInfo(id);

        Link link = ControllerLinkBuilder.
                linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).deleteUserInfo(httpSession))
                .withRel("DeleteUserInfo");

        Resource<UserDTO> result = new Resource<>(userDTO, link);
        return result;
    }

    @Getter
    @RequiredArgsConstructor
    private static class DbResponse {
        enum DbStatus {
            SUCCESS, FAIL, ERROR, NO_DATA
        }

        @NonNull
        private DbStatus result;

        private static final DbResponse SUCCESS = new DbResponse(DbStatus.SUCCESS);
        private static final DbResponse FAIL    = new DbResponse(DbStatus.FAIL);
        private static final DbResponse NO_DATA    = new DbResponse(DbStatus.NO_DATA);
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