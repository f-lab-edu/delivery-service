package me.naming.delieveryservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import lombok.Getter;
import me.naming.delieveryservice.utils.FeeDTOSerializer;
import org.apache.ibatis.type.Alias;

/**
 * 직렬화란? - 데이터 구조, 객체 등을 저장할 수 있는 형태로 변환하여 동일 환경이나 다른 컴퓨터에서 재구성할 수 있는 것을 말한다.
 * 직렬화가 중요한 이유 - 효율적인 직렬화 형태에 따라 CPU, 네트워크, 메모리 비용을 줄일 수 있기 때문이다.
 *                  직렬화 도구(JSON, Protocol Buffer 등)에 따라 byte 크기와 속도가 다르다.
 *
 * 하지만, 이중 자바 직렬화는 사용하지 말아야 한다.
 * 자바 직렬화는 데이터 타입 구분 없이 모든 것을 직렬화 해준다.
 * 이런 경우 역직렬화 했을 때 직렬화된 모든 코드들이 위험요소가 될 수 있다.
 */
@Getter
@Alias("FeeDTO")
@JsonSerialize(using = FeeDTOSerializer.class)
public class FeeDTO implements Serializable {

  private String deliveryType;
  private float basicDistance;
  private int basicFee;
  private float extraDistance;
  private int extraFee;
}
