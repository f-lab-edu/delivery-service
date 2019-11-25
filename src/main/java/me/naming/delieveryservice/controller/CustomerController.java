package me.naming.delieveryservice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import me.naming.delieveryservice.dto.ResponseDTO;
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

    /**
     * 고객 회원가입 메서드
     *  - ResponseEntity란 HttpEntity를 상속받은 클래스로써, Http의 Header와 Body 관련 정보를 저장 할 수 있게 해준다.
     *  - HttpStatus를 활용해 Header 값에 정확한 상태 값(ex. 200 성공, 400 에러 등)을 전달 할 수 있으며, 헤더에 추가로 데이터 값을 넣어줄 수도 있다.
     * @param userDTO 저장할 회원정보
     * @return
     */
    @PostMapping(value = "/signup" )
    public ResponseEntity<ResponseDTO> signUpUserInfo(@RequestBody UserDTO userDTO) {

        userService.insertUserInfo(userDTO);
        return new ResponseEntity<>(ResponseDTO.SUCCESS, HttpStatus.CREATED);
    }

    /**
     * ID 중복체크 메서드
     * @param id DB에서 조회할 사용자 ID
     * @return
     */
    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<ResponseDTO> checkIdDuplicate(@PathVariable String id) {

        boolean idCheck = userService.checkIdDuplicate(id);
        if(idCheck) {
            return new ResponseEntity<>(ResponseDTO.DUPLICATE, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(ResponseDTO.SUCCESS, HttpStatus.OK);
    }

    /**
     * 로그인을 하기 위한 메서드
     * @param userLoginRequest 로그인(id, password) 정보
     * @param httpSession      세션 저장
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpSession httpSession) throws Exception {
        UserDTO userDTO = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword());
        httpSession.setAttribute("USER_ID", userDTO.getUserId());
        return new ResponseEntity<>(ResponseDTO.SUCCESS, HttpStatus.OK);
    }

    /**
     * 비밀번호를 변경하기 위한 메서드
     * @param userChgPwd
     * @param httpSession
     * @return
     */
    @PatchMapping(value = "/{id}/password")
    public ResponseEntity<ResponseDTO> updatePwd(@RequestBody UserChgPwd userChgPwd, @PathVariable String id, HttpSession httpSession) {
        ResponseDTO.checkId(httpSession, id);
        userService.updatePwd(id, userChgPwd.getNewPassword());
        return new ResponseEntity<>(ResponseDTO.SUCCESS, HttpStatus.OK);
    }

    /**
     * 회원탈퇴
     * @param id
     * @param httpSession
     * @return
     */
    @DeleteMapping(value = "/{id}/info")
    public ResponseEntity<ResponseDTO> deleteUserInfo(@PathVariable String id,  HttpSession httpSession) {

        ResponseDTO.checkId(httpSession, id);
        userService.deleteUserInfo(id);
        httpSession.invalidate();
        return new ResponseEntity<>(ResponseDTO.SUCCESS, HttpStatus.OK);
    }

    /**
     * 회원정보 조회
     * @param httpSession
     * @return
     */
    @GetMapping(value = "/{id}/info")
    public Resource<UserDTO> getUserInfo(@PathVariable String id, HttpSession httpSession) {

        ResponseDTO.checkId(httpSession, id);
        UserDTO userDTO = userService.getUserInfo(id);
        Link link = ControllerLinkBuilder.
                linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).deleteUserInfo(id, httpSession))
                .withRel("DeleteUserInfo");

        return new Resource<>(userDTO, link);
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