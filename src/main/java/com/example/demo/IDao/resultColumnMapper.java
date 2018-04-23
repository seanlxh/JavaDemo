package com.example.demo.IDao;

import com.example.demo.entity.resultColumn;

public interface resultColumnMapper {
    int deleteByPrimaryKey(Long resultId);

    int insert(resultColumn record);

    int insertSelective(resultColumn record);

    resultColumn selectByPrimaryKey(Long resultId);

    int updateByPrimaryKeySelective(resultColumn record);

    int updateByPrimaryKey(resultColumn record);
}