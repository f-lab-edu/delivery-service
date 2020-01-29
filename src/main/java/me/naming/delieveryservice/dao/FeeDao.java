package me.naming.delieveryservice.dao;

import java.util.List;
import me.naming.delieveryservice.dto.FeeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FeeDao {

  List<FeeDTO> selectFeeInfoList();

}
