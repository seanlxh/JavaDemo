package com.example.demo.controller;

import com.example.demo.Config.ShiroToken;
import com.example.demo.entity.u_user;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@Controller
public class LoginController {
    //跳转到登录表单页面
    @RequestMapping("/login")
    public String login() {
        return "login1";
    }

    /**
     * ajax登录请求
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="ajaxLogin",method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitLogin(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password, Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            u_user u_user = (u_user)SecurityUtils.getSubject().getPrincipal();
            if(u_user.getRoleId()==1){
                resultMap.put("status", 200);
                resultMap.put("message", "登录成功");
            }else if(u_user.getRoleId()==2){
                resultMap.put("status", 201);
                resultMap.put("message", "登录成功");
            }
        }
         catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public Map<String,String> getUser() {
        u_user user = (u_user)SecurityUtils.getSubject().getPrincipal();
        Map<String,String> curUser = new HashMap<>();
        curUser.put("user",user.getNickname());
        return curUser;
    }


}
