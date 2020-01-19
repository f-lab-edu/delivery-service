package me.naming.delieveryservice.dto;

import java.io.Serializable;
import lombok.Getter;

/**
 * 직렬화 사용이유 : Redis에 UserInfoDTO를 저장시키기 위해 직렬화 사용
 * 직렬화란?
 *    객체의 상태 혹은 데이터 구조를 기록할 수 있는 포맷(파일 또는 메모리 버퍼, 또는 네트워크 연결 링크를 통해 전송될 수 있는 형태)로
 *    변환하여 나중에 동일 혹은 다른 컴퓨터 환경에서 재구성할 수 있게끔 하는 절차이다.
 *
 * 참고
 *   - https://www.slideshare.net/sunnykwak90/java-serialization-46382579?qid=0373d162-1b85-4c11-b708-2f9b17f5d4b4&v=&b=&from_search=1
 *   - http://woowabros.github.io/experience/2017/10/17/java-serialize.html
 */
@Getter
public class UserInfoDTO implements Serializable {
  private int userNum;
  private String userId;
  private String mobileNum;
}
