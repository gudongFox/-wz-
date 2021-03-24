package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMaterialBorrow;
import java.util.List;
import java.util.Map;

public interface FiveOaMaterialBorrowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMaterialBorrow record);

    FiveOaMaterialBorrow selectByPrimaryKey(Integer id);

    List<FiveOaMaterialBorrow> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMaterialBorrow record);
}