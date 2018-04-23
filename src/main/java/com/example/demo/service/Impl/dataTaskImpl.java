package com.example.demo.service.Impl;

import com.example.demo.IDao.dataSourceMapper;
import com.example.demo.IDao.dataTaskMapper;
import com.example.demo.entity.dataSource;
import com.example.demo.entity.dataTask;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Service("dataTaskService")
public class dataTaskImpl implements BaseService<dataTask> {

    @Resource
    private dataTaskMapper dataTaskDao;

    @Override
    public void save(dataTask entity) {
        dataTaskDao.insert(entity);
    }

    @Override
    public void delete(dataTask entity) {

    }

    @Override
    public void update(dataTask entity) {

    }

    @Override
    public dataTask findById(Long id) {
        return dataTaskDao.selectByPrimaryKey(id);
    }

    @Override
    public List<dataTask> getAll() {
        return dataTaskDao.getAll();
    }
}