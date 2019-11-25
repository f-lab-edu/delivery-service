package me.naming.delieveryservice.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

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
        String sessionId = httpSession.getAttribute("USER_ID").toString();
        if(sessionId == null || pathVariable == null) {
            new RuntimeException("Session('USER_ID') or PathVariable is not exists");
        }
        if(!sessionId.equals(pathVariable)) {
            new RuntimeException("Sssions('USER_ID') PathVariable is not same");
        }

    }
}
