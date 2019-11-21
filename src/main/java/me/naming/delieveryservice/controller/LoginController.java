package me.naming.delieveryservice.controller;

import me.naming.delieveryservice.dao.UserDao;
import me.naming.delieveryservice.service.UserService;
import me.naming.delieveryservice.vo.UserVO;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/user/info")
    public List userInfo() throws Exception{
        List<UserVO> objUserInfo = userDao.userInfo();
        return objUserInfo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sign-in" )
    public String signInUserInfo(@RequestBody Map<String,String> userInfo) throws Exception {
        String result = userService.insertUserInfo(userInfo);
        return result;
    }


}