package com.example.demo.IDao;

import com.example.demo.entity.functionLogic;

import java.util.List;

public interface functionLogicMapper {
    int deleteByPrimaryKey(Long functionId);

    int insert(functionLogic record);

    int insertSelective(functionLogic record);

    functionLogic selectByPrimaryKey(Long functionId);

    int updateByPrimaryKeySelective(functionLogic record);

    int updateByPrimaryKey(functionLogic record);

    List<functionLogic> selectByDSIDWithLogic(Long dsId);
}