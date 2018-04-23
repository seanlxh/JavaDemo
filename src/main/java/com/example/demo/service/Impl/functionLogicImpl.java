package com.example.demo.service.Impl;

import com.example.demo.IDao.dataSourceMapper;
import com.example.demo.IDao.functionLogicMapper;
import com.example.demo.entity.functionLogic;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("functionLogicService")
public class functionLogicImpl implements BaseService<functionLogic> {

    @Resource
    private functionLogicMapper functionLogicDao;

    @Override
    public void save(functionLogic entity) {

    }

    @Override
    public void delete(functionLogic entity) {

    }

    @Override
    public void update(functionLogic entity) {

    }

    @Override
    public functionLogic findById(Long id) {
        return null;
    }

    @Override
    public List<functionLogic> getAll() {
        return null;
    }

    public List<functionLogic> selectByDSIDWithLogic(Long id) {
        return functionLogicDao.selectByDSIDWithLogic(id);
    }
}
