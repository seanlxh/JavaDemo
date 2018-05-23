package com.example.demo.service.Impl;

import com.example.demo.IDao.u_userMapper;
import com.example.demo.IDao.useRecordMapper;
import com.example.demo.entity.useRecord;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("useRecord")
public class useRecordImpl implements BaseService<useRecord>{

    @Resource
    private useRecordMapper useRecordMapper;
    @Override
    public void save(useRecord entity) {
        useRecordMapper.insert(entity);
    }

    @Override
    public void delete(useRecord entity) {

    }

    @Override
    public void update(useRecord entity) {

    }

    @Override
    public useRecord findById(Long id) {
        return null;
    }

    @Override
    public List<useRecord> getAll() {
        return null;
    }

    public List<useRecord> selectByTime(Map<String , Timestamp> map){
        return useRecordMapper.selectByTime(map);
    }
}
