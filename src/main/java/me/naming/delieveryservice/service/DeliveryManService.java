package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.DeliveryManDao;
import me.naming.delieveryservice.dto.DeliveryManDTO;
import me.naming.delieveryservice.utils.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryManService {

  @Autowired private DeliveryManDao deliveryManDao;

  public void addDeliveryManInfo(DeliveryManDTO deliveryManDTO){
    String encryptPassword = SHA256Util.encrypt(deliveryManDTO.getPassword());
    deliveryManDTO.changeToEnryptPassword(encryptPassword);
    deliveryManDao.insertDeliveryManInfo(deliveryManDTO.getId(), deliveryManDTO.getPassword(), deliveryManDTO.getName(), deliveryManDTO.getMobileNum(), deliveryManDTO.getBirthday());
  }

  public boolean checkIdDuplicate(String id){
    return deliveryManDao.isExistsId(id);
  }

  public DeliveryManDTO getDeliveryManInfo(String id, String password){
    String encryptPwd = SHA256Util.encrypt(password);
    DeliveryManDTO deliveryManDTO = deliveryManDao.selectDeliveryManInfo(id, encryptPwd);

    if(deliveryManDTO == null) {
      throw new RuntimeException("사용자 정보가 존재하지 않습니다. 아이디 또는 비밀번호를 확인하십시오");
    }
    return deliveryManDTO;
  }

}
