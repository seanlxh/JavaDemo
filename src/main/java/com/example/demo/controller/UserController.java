package com.example.demo.controller;

import com.example.demo.DTO.dataSourceDTO;
import com.example.demo.entity.dataSource;
import com.example.demo.service.Impl.dataSourceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/DataSource")
public class UserController {
    @Resource
    private dataSourceImpl dataSourceService;

    @RequestMapping("/showDataSource")
    @ResponseBody
    public List<dataSourceDTO> toIndex(HttpServletRequest request, Model model){
        //int userId = Integer.parseInt(request.getParameter("id"));
        List<dataSource> user = this.dataSourceService.getAll();
        List<dataSourceDTO> res = new ArrayList<dataSourceDTO>();
        for(int i = 0 ; i < user.size(); i ++) {
            String result;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(user.get(i).getTimestamp());
            Date date = new Date(lt);
            result = simpleDateFormat.format(date);
            dataSourceDTO tmp = new dataSourceDTO(user.get(i).getDsId(),user.get(i).getDsName(),user.get(i).getDsDesc(),user.get(i).getType(),result);
            res.add(tmp);
        }
        return res;
    }

}


