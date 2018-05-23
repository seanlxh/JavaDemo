package com.example.demo.service.Impl;

import com.example.demo.entity.u_user;
import com.example.demo.entity.u_user_role;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("u_user_roleService")
public class u_user_roleImpl implements BaseService<u_user_role> {
    @Override
    public void save(u_user_role entity) {

    }

    @Override
    public void delete(u_user_role entity) {

    }

    @Override
    public void update(u_user_role entity) {

    }

    @Override
    public u_user_role findById(Long id) {
        return null;
    }

    @Override
    public List<u_user_role> getAll() {
        return null;
    }
}
