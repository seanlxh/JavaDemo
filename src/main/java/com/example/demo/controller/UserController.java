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
import java.util.*;

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
            dataSourceDTO tmp = new dataSourceDTO(user.get(i).getDsId(),user.get(i).getDsName(),user.get(i).getDsDesc(),user.get(i).getType(),result,user.get(i).getState());
            res.add(tmp);
        }
        return res;
    }


    @RequestMapping("/showDataSourceByID")
    @ResponseBody
    public dataSourceDTO dsID(HttpServletRequest request, Model model){
        Long dsId = Long.parseLong(request.getParameter("id"));
        dataSource user = this.dataSourceService.findById(dsId);
        String result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(user.getTimestamp());
        Date date = new Date(lt);
        result = simpleDateFormat.format(date);
        dataSourceDTO tmp = new dataSourceDTO(user.getDsId(),user.getDsName(),user.getDsDesc(),user.getType(),result,user.getState());
        return tmp;
    }

    @RequestMapping("/deleteDataSourceByID")
    @ResponseBody
    public Map<String,String> delete(HttpServletRequest request, Model model){
        Long dsId = Long.parseLong(request.getParameter("id"));
        int result = this.dataSourceService.deleteById(dsId);
        Map<String,String> res = new HashMap<String, String>();
        if(result != 0)
            res.put("code","删除成功");
        else
            res.put("code","删除失败");

        return res;
    }

    @RequestMapping("/startDataSourceByID")
    @ResponseBody
    public Map<String,String> startDS(HttpServletRequest request, Model model){
        Long dsId = Long.parseLong(request.getParameter("id"));
        int result = this.dataSourceService.startDS(dsId);
        Map<String,String> res = new HashMap<String, String>();
        if(result != 0)
            res.put("code","启动成功");
        else
            res.put("code","启动失败");

        return res;
    }

    @RequestMapping("/stopDataSourceByID")
    @ResponseBody
    public Map<String,String> stopDS(HttpServletRequest request, Model model){
        Long dsId = Long.parseLong(request.getParameter("id"));
        int result = this.dataSourceService.stopDS(dsId);
        Map<String,String> res = new HashMap<String, String>();
        if(result != 0)
            res.put("code","停用成功");
        else
            res.put("code","停用失败");

        return res;
    }
}


