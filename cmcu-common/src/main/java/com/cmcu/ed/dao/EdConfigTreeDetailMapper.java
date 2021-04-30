package com.cmcu.ed.dao;

import com.cmcu.ed.entity.EdConfigTreeDetail;
import java.util.List;
import java.util.Map;

public interface EdConfigTreeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdConfigTreeDetail record);

    EdConfigTreeDetail selectByPrimaryKey(Integer id);

    List<EdConfigTreeDetail> selectAll(Map params);

    int updateByPrimaryKey(EdConfigTreeDetail record);
}