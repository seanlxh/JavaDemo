package com.example.demo.IDao;

import com.example.demo.entity.inputPara;

import java.util.List;

public interface inputParaMapper {
    int insert(inputPara record);

    int insertSelective(inputPara record);

    List<inputPara> selectByTypeId(int id);
}