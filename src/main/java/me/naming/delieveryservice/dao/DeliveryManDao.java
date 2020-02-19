package me.naming.delieveryservice.dao;

import java.time.LocalDate;
import me.naming.delieveryservice.dto.DeliveryManDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Mapper는 매핑파일(...Mapper.xml)에 기재된 SQL문을 호출하기 위한 인터페이스이며, Mybatis 3.0부터 사용되었다.
 */
@Mapper
@Repository
public interface DeliveryManDao {

  void insertDeliveryManInfo(String id, String password, String  name, String mobileNum, LocalDate birthday);
  boolean isExistsId(String id);
  DeliveryManDTO selectDeliveryManInfo(String id, String password);

}
