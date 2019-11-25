package me.naming.delieveryservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

@Getter
@RequiredArgsConstructor
public class ResponseDTO {
    enum ResponseStatus{
        SUCCESS, FAIL, DUPLICATE
    }

    @NonNull
    private ResponseStatus result;

    public static final ResponseDTO SUCCESS     = new ResponseDTO(ResponseStatus.SUCCESS);
    public static final ResponseDTO FAIL        = new ResponseDTO(ResponseStatus.FAIL);
    public static final ResponseDTO DUPLICATE   = new ResponseDTO(ResponseStatus.DUPLICATE);

    /**
     * Session, PathVariable ID 값을 체크하기 위한 메서드
     * @param httpSession
     * @param pathVariable
     */
    public static void checkId(HttpSession httpSession, String pathVariable) {
        if(httpSession.getAttribute("USER_ID") == null || pathVariable == null) {
            throw new RuntimeException("Session('USER_ID') or PathVariable is not exists");
        }

        if(!httpSession.getAttribute("USER_ID").toString().equals(pathVariable)) {
            throw new RuntimeException("Session('USER_ID'), PathVariable is not same");
        }
    }
}
