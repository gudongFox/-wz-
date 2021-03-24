package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMaterialBorrowDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaMaterialBorrowDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMaterialBorrowDetail record);

    FiveOaMaterialBorrowDetail selectByPrimaryKey(Integer id);

    List<FiveOaMaterialBorrowDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMaterialBorrowDetail record);
}