package com.example.demo.service.Impl;

import com.example.demo.IDao.inputParaMapper;
import com.example.demo.IDao.para_typeMapper;
import com.example.demo.entity.inputPara;
import com.example.demo.entity.para_type;
import com.example.demo.service.BaseService;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("para_typeImpl")
public class para_typeImpl implements BaseService<para_type> {

    @Resource
    private para_typeMapper para_typeMapper;
    @Override
    public void save(para_type entity) {
        para_typeMapper.insert(entity);
    }

    @Override
    public void delete(para_type entity) {

    }

    public void delete(Integer id) {
        para_typeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(para_type entity) {
        para_typeMapper.updateByPrimaryKey(entity);
    }

    @Override
    public para_type findById(Long id) {
        return para_typeMapper.selectByPrimaryKey(Integer.valueOf(String.valueOf(id)));
    }

    @Override
    public List<para_type> getAll() {
        return para_typeMapper.getAll();
    }
}
