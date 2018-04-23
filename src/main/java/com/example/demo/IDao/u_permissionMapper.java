package com.example.demo.IDao;

import com.example.demo.entity.u_permission;

public interface u_permissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(u_permission record);

    int insertSelective(u_permission record);

    u_permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(u_permission record);

    int updateByPrimaryKey(u_permission record);
}