package me.naming.delieveryservice.dao;

import java.util.List;
import me.naming.delieveryservice.dto.FeeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@EnableCaching
public interface FeeDao {

  @Cacheable
  List<FeeDTO> getFeeInfo();

}
