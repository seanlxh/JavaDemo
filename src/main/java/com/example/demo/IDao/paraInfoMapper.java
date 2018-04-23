package com.example.demo.IDao;

import com.example.demo.entity.paraInfo;

import java.util.List;

public interface paraInfoMapper {
    int deleteByPrimaryKey(Long paraId);

    int insert(paraInfo record);

    int insertSelective(paraInfo record);

    paraInfo selectByPrimaryKey(Long paraId);

    int updateByPrimaryKeySelective(paraInfo record);

    int updateByPrimaryKey(paraInfo record);

    List<paraInfo> selectByfuncid(Long func_id);
}