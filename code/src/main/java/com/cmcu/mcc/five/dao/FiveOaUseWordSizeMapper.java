package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaUseWordSize;
import java.util.List;
import java.util.Map;

public interface FiveOaUseWordSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaUseWordSize record);

    FiveOaUseWordSize selectByPrimaryKey(Integer id);

    List<FiveOaUseWordSize> selectAll(Map params);

    int updateByPrimaryKey(FiveOaUseWordSize record);
}