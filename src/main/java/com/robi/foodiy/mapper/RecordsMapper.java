package com.robi.foodiy.mapper;

import java.util.List;

import com.robi.foodiy.data.dao.RecordsDao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordsMapper {

    public RecordsDao selectById(long id);
    public List<RecordsDao> selectAllByWriteUserId(long writeUserId);
    public void insert(RecordsDao recordDao);
    public void update(RecordsDao recordDao);
    public void deleteById(long id);
}