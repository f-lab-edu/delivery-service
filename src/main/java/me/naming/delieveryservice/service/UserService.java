package me.naming.delieveryservice.service;

import lombok.extern.slf4j.Slf4j;
import me.naming.delieveryservice.RspObj;
import me.naming.delieveryservice.dao.UserDao;
import me.naming.delieveryservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("UserService")
public class UserService {

    @Autowired
    private UserDao userDao;

    public RspObj insertUserInfo(UserDTO userInfo) throws Exception{
        String id = userInfo.getId();
        String password = userInfo.getPassword();
        userDao.insertUserInfo(id, password);
        return RspObj.SUCCESS;
    }
}

