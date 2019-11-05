package me.naming.delieveryservice.controller;

import lombok.*;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/customer")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
     * @param userDTO 저장할 회원정보
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sign-up" )
    public ResponseEntity<SignUpResponse> signUpUserInfo(@RequestBody UserDTO userDTO) {
        ResponseEntity<SignUpResponse> responseEntity = null;
        userService.insertUserInfo(userDTO);
        SignUpResponse result = SignUpResponse.SUCCESS;
        responseEntity = new ResponseEntity<SignUpResponse>(result, HttpStatus.CREATED);
        return responseEntity;
    }

    /**
     * ID 중복체크 메서드
     * @param id DB에서 조회할 사용자 ID
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/duplicate")
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
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody  UserLoginRequest userLoginRequest, HttpSession httpSession) {

        ResponseEntity<LoginResponse> rspResponseEntity;
        LoginResponse loginResponse;

        UserDTO userDTO = userService.userLogin(userLoginRequest.getId(), userLoginRequest.getPassword());
        if(userDTO == null) {
            loginResponse = LoginResponse.FAIL;
            rspResponseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.UNAUTHORIZED);
            return rspResponseEntity;
        }

        loginResponse = LoginResponse.success(userDTO);
        httpSession.setAttribute("PID_NUM", userDTO.getPidNum());
        rspResponseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        return rspResponseEntity;
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

    @Getter
    @Setter
    private static class UserLoginRequest {
        @NonNull
        String id;
        @NonNull
        String password;
    }

}