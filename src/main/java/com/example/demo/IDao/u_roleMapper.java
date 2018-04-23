package com.example.demo.IDao;

import com.example.demo.entity.u_role;

public interface u_roleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(u_role record);

    int insertSelective(u_role record);

    u_role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(u_role record);

    int updateByPrimaryKey(u_role record);
}