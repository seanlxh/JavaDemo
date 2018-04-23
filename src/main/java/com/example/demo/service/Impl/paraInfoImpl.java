package com.example.demo.service.Impl;

import com.example.demo.IDao.paraInfoMapper;
import com.example.demo.entity.paraInfo;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("paraInfoservice")
public class paraInfoImpl implements BaseService<paraInfo>{

    @Resource
    private paraInfoMapper paraInfoDao;

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


    public List<paraInfo> getParaInfoByFuncId(Long func_id){
        return paraInfoDao.selectByfuncid(func_id);
    }
}
