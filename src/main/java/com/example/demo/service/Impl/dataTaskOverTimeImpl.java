package com.example.demo.service.Impl;

import com.example.demo.IDao.dataTaskOverTimeMapper;
import com.example.demo.IDao.functionLogicMapper;
import com.example.demo.entity.dataTask;
import com.example.demo.entity.dataTaskOverTime;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dataTaskOverTimeService")
public class dataTaskOverTimeImpl implements BaseService<dataTaskOverTime> {
    @Resource
    private dataTaskOverTimeMapper dataTaskOverTimeMapper;


    @Override
    public void save(dataTaskOverTime entity) {

    }

    @Override
    public void delete(dataTaskOverTime entity) {

    }

    @Override
    public void update(dataTaskOverTime entity) {

    }

    @Override
    public dataTaskOverTime findById(Long id) {
        return dataTaskOverTimeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<dataTaskOverTime> getAll() {
        return null;
    }
}
