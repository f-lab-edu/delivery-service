package me.naming.delieveryservice.dao;

import java.time.LocalDate;
import me.naming.delieveryservice.dto.DeliveryManDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeliveryManDao {

  void insertDeliveryManInfo(String id, String password, String  name, String mobileNum, LocalDate birthday);
  int selectCountById(String id);
  DeliveryManDTO selectDeliveryManInfo(String id, String password);

}
