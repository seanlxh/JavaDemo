package com.example.demo.service.Impl;

import com.example.demo.IDao.resultColumnMapper;
import com.example.demo.entity.paraInfo;
import com.example.demo.entity.resultColumn;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("resultColumnservice")
public class resultColumnImpl implements BaseService<paraInfo>{

    @Resource
    private resultColumnMapper resultColumnDao;

    @Override
    public void save(paraInfo entity) {

    }

    @Override
    public void delete(paraInfo entity) {

    }

    @Override
    public void update(paraInfo entity) {

    }

    @Override
    public paraInfo findById(Long id) {
        return null;
    }

    @Override
    public List<paraInfo> getAll() {
        return null;
    }

    public resultColumn selectByPrimaryKey(Long id){
        return resultColumnDao.selectByPrimaryKey(id);
    }
}
