package com.robi.foodiy.mapper;

import java.util.List;

import com.robi.foodiy.data.dao.MenusDao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenusMapper {

    public MenusDao selectById(long id);
    public List<MenusDao> selectAllByRecordId(long recordId);
    public void insert(MenusDao recordDao);
    public void update(MenusDao recordDao);
    public void deleteById(long id);
}