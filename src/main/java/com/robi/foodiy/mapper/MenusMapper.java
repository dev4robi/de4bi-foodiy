package com.robi.foodiy.mapper;

import java.util.List;

import com.robi.foodiy.data.dao.MenusDao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenusMapper {

    public MenusDao selectById(long id);
    public List<MenusDao> selectAllByRecordId(long recordId);
    public List<MenusDao> selectAllByWriterIdToPage(
        @Param("writerId")      long writerId,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);
    public List<MenusDao> selectAllByWriterIdAndMenuNameToPage(
        @Param("writerId")      long writerId,
        @Param("menuName")      String menuName,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);
    public List<MenusDao> selectAllByWriterIdAndTagToPage(
        @Param("writerId")      long writerId,
        @Param("tag")           String tag,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);
    public List<MenusDao> selectAllByWriterIdAndPlaceToPage(
        @Param("writerId")      long writerId,
        @Param("place")           String place,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);
    public List<MenusDao> selectAllByWriterIdAndWhoToPage(
        @Param("writerId")      long writerId,
        @Param("who")           String who,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);

    public void insert(MenusDao recordDao);

    public void update(MenusDao recordDao);

    public void deleteById(long id);
}