package me.naming.delieveryservice.dao;

import me.naming.delieveryservice.dto.AddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressDao {
  AddressDTO getAddressInfoByAddressCode(int addressCode);
}
