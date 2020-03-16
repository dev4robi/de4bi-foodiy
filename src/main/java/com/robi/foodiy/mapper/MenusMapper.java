package com.robi.foodiy.mapper;

import java.util.List;

import com.robi.foodiy.data.dao.MenusDao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenusMapper {

    public MenusDao selectById(long id);
    public List<MenusDao> selectAllByRecordId(long recordId);
    public List<MenusDao> selectAllByWriterIdWithPage(
        @Param("writerId")      long writerId,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);
    public List<MenusDao> selectAllByWriterIdAndMenuNameWithPage(
        @Param("writerId")      long writerId,
        @Param("menuName")      String menuName,
        @Param("menuBgnIdx")    int menuBgnIdx,
        @Param("menuPerPage")   int menuPerPage);

        // 태그, 장소, 누구랑으로 조회 추가부터 계속... @@

    public void insert(MenusDao recordDao);

    public void update(MenusDao recordDao);

    public void deleteById(long id);
}