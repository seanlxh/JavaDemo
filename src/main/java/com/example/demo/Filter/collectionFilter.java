package com.example.demo.Filter;

import com.example.demo.entity.u_user;
import com.example.demo.entity.useRecord;
import com.example.demo.service.Impl.dataSourceImpl;
import com.example.demo.service.Impl.useRecordImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Order(1)
//重点
@WebFilter(filterName = "collectionFilter", urlPatterns = "/collection/collection")
public class collectionFilter implements Filter {
    @Resource
    private useRecordImpl useRecordService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Long time = System.currentTimeMillis();
//        u_user user = (u_user) SecurityUtils.getSubject().getPrincipal();
        Timestamp date = new Timestamp(time);
        useRecordService.save(new useRecord(time,date,1,null));
        System.out.println(date.toString());
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
