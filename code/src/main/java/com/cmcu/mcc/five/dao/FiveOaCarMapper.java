package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaCar;
import java.util.List;
import java.util.Map;

public interface FiveOaCarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaCar record);

    FiveOaCar selectByPrimaryKey(Integer id);

    List<FiveOaCar> selectAll(Map params);

    int updateByPrimaryKey(FiveOaCar record);

}