package me.naming.delieveryservice.controller;

import me.naming.delieveryservice.dao.UserDao;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/sign-in" )
    public Object signInUserInfo(@RequestBody UserDTO userDTO) throws Exception {
        return userService.insertUserInfo(userDTO);
    }

}