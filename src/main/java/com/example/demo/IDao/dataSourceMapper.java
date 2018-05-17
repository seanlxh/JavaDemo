package com.example.demo.IDao;

import com.example.demo.entity.dataSource;

import java.util.List;

public interface dataSourceMapper {
    int deleteByPrimaryKey(Long dsId);

    int insert(dataSource record);

    int insertSelective(dataSource record);

    dataSource selectByPrimaryKey(Long dsId);

    int updateByPrimaryKeySelective(dataSource record);

    int updateByPrimaryKey(dataSource record);

    List<dataSource> getAll();

    int startDS(Long dsId);

    int stopDS(Long dsId);
}