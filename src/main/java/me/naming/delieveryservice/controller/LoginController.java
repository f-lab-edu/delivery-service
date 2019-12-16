package me.naming.delieveryservice.controller;


import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lombok을 활용한 생성자 자동생성
 *  - @NoArgsConstructor 파라미터가 없는 기본 생성자 생성
 *  - @AllArgsConstructor 모든 필드 값을 파라미터로 받는 생성자 생성
 *  - @RequiredArgsConstructor final or @NonNull인 필드 값만 파라미터로 받는 생성자 생성
 */
@RestController
@RequestMapping("/customers")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
     *
     * http 헤더중 Location은 post 동작 완료 후 리다이렉션 URL을 지정해주는 정보이다. 즉, 어느 페이지로 이동할지를 알려주는 헤더
     * @param userDTO 저장할 회원정보
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/signup" )
    public ResponseEntity signUpUserInfo(@RequestBody UserDTO userDTO) {
        userService.insertUserInfo(userDTO);
        URI uri = ControllerLinkBuilder.linkTo(LoginController.class).toUri();
        return ResponseEntity.created(uri).build();
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


        UserDTO userDTO = userService.userLogin(userLoginRequest.getId(), userLoginRequest.getPassword());
        httpSession.setAttribute("USER_ID", userDTO.getId());

        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호를 변경하기 위한 메소드
     * @param userChgPwd
     * @param httpSession
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/password")
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

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/info")
    public ResponseEntity changeUserStatus(@PathVariable String id, HttpSession httpSession) {

        userService.changeUserStatus(id);
        httpSession.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
        String id;
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