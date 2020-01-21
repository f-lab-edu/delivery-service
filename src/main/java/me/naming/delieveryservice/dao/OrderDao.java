package me.naming.delieveryservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

  int reqOrder(String userId, int dprtCode, String dprtDetail, int dstinCode, String dstinDetail);
}
