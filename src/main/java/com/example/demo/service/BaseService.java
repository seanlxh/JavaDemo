package com.example.demo.service;

import java.io.Serializable;

import java.util.List;

public interface BaseService<T> {

    public void save(T entity);// 保存

    public void delete(T entity);// 删除用户

    public void update(T entity);// 更新用户

    public T findById(Long id);// 根据主键查找用户

    public List<T> getAll();// 查看所有

}
