package com.example.demo.service.Impl;

import com.example.demo.IDao.dataSourceMapper;
import com.example.demo.IDao.inputParaMapper;
import com.example.demo.entity.inputPara;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("inputParaService")
public class inputParaImpl implements BaseService<inputPara>{

    @Resource
    private inputParaMapper inputParaDao;

    @Override
    public void save(inputPara entity) {
        inputParaDao.insert(entity);
    }

    @Override
    public void delete(inputPara entity) {
        inputParaDao.deleteByPrimaryKey(entity.getTypeId());
    }


    public void delete(int id) {
        inputParaDao.deleteByPrimaryKey(id);
    }

    @Override
    public void update(inputPara entity) {

    }

    @Override
    public inputPara findById(Long id) {
        return null;
    }

    @Override
    public List<inputPara> getAll() {
        return null;
    }

    public List<inputPara> findBytypeId(int id) {
        return inputParaDao.selectByTypeId(id);
    }


}
