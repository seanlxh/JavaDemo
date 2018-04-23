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
@RequestMapping("/inputPara")
public class inputParaController {
    @Resource
    private inputParaImpl inputParaService;
    @Resource
    private dataSourceImpl dataSourceService;
    @RequestMapping("/inputPara")
    @ResponseBody
    public List<inputPara> toIndex(HttpServletRequest request, Model model){
        Long id = Long.parseLong(request.getParameter("id"));
        List<inputPara> inputParas = this.inputParaService.findBytypeId(dataSourceService.findById(id).getType());
        return inputParas;
    }

}