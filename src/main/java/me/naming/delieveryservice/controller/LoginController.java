package me.naming.delieveryservice.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  -
     * @param userDTO 저장할 회원정보
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sign-up" )
    public ResponseEntity<SignUpResponse> signUpUserInfo(@RequestBody UserDTO userDTO) {
        ResponseEntity<SignUpResponse> responseEntity = null;
        SignUpResponse result;
        userService.insertUserInfo(userDTO);
        result = SignUpResponse.SUCCESS;
        responseEntity = new ResponseEntity<SignUpResponse>(result, HttpStatus.CREATED);
        return responseEntity;
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

}