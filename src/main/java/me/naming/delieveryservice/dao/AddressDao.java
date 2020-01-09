package me.naming.delieveryservice.dao;

import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressDao {
  HashMap<String,Object> getAddressInfoByAddressCode(int addressCode);
}
