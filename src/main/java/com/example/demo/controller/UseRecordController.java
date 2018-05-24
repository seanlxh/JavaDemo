package com.example.demo.controller;

import com.example.demo.entity.dataSource;
import com.example.demo.entity.useRecord;
import com.example.demo.service.Impl.dataSourceImpl;
import com.example.demo.service.Impl.useRecordImpl;
import com.example.demo.util.dateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class UseRecordController {
    @Resource
    private useRecordImpl useRecordService;

    @Resource
    private dataSourceImpl dataSourceService;

    @RequestMapping("/getMonth")
    @ResponseBody
    public List<String> getMonth(HttpServletRequest request, Model model){
        Date date = dateUtil.getStartMonthofDay(new Date());
        List<String> result = new ArrayList<>();
        String[] months = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        for(int i = 6 ; i >=0 ; i --){
            Date startDate = dateUtil.subMonth(date,i);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int month = cal.get(Calendar.MONTH);
            result.add(months[month]);
        }
        return result;
    }

    @RequestMapping("/getMonthData")
    @ResponseBody
    public List<Integer> getMonthData(HttpServletRequest request, Model model){
        Date date = dateUtil.getStartTimeofDay(new Date());
        List<Integer> result = new ArrayList<>();
        for(int i = 6 ; i >=0 ; i --){
            Date startDate = dateUtil.subMonth(date,i);
            Date endDate = dateUtil.subMonth(date,i-1);
            Map<String, Long> map = new HashMap<>();
            map.put("startTime",(startDate.getTime()));
            map.put("endTime",(endDate.getTime()));
            List<dataSource> res = dataSourceService.getTime(map);
            int size = res.size();
            result.add(size);
        }
        return result;
    }

    @RequestMapping("/getUseRecord")
    @ResponseBody
    public List<Integer> getUseRecord(HttpServletRequest request, Model model){
          Date date = dateUtil.getStartTimeofDay(new Date());
          List<Integer> result = new ArrayList<>();
          for(int i = 6 ; i >=0 ; i --){
              Date startDate = dateUtil.subDay(date,i);
              Date endDate = dateUtil.subDay(date,i-1);
              Map<String, Timestamp> map = new HashMap<>();
              map.put("startTime",new Timestamp(startDate.getTime()));
              map.put("endTime",new Timestamp(endDate.getTime()));
              List<useRecord> res = useRecordService.selectByTime(map);
              int size = res.size();
              result.add(size);
          }
        return result;
    }


    @RequestMapping("/getDate")
    @ResponseBody
    public List<String> getDate(HttpServletRequest request, Model model){
        Date date = dateUtil.getStartTimeofDay(new Date());
        List<String> result = new ArrayList<>();
        for(int i = 6 ; i >=0 ; i --){
            Date startDate = dateUtil.subDay(date,i);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DATE);
            String curDay = String.valueOf(month) +"-"+ String.valueOf(day);
            result.add(curDay);
        }
        return result;
    }


}
