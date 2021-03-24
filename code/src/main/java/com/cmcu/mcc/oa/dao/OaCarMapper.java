package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaCar;
import java.util.List;
import java.util.Map;

public interface OaCarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaCar record);

    OaCar selectByPrimaryKey(Integer id);

    List<OaCar> selectAll(Map params);

    int updateByPrimaryKey(OaCar record);
}