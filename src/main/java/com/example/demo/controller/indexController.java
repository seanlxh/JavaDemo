package com.example.demo.controller;

import com.example.demo.entity.dataSource;
import com.example.demo.entity.inputPara;
import com.example.demo.service.Impl.dataSourceImpl;
import com.example.demo.service.Impl.inputParaImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/index")
public class indexController {
    @RequestMapping("/index")
    public String toIndex(HttpServletRequest request, Model model){
        return "ltpt";
    }
    @RequestMapping("/add")
    public String toAdd(HttpServletRequest request, Model model){
        return "sjydy";
    }
}