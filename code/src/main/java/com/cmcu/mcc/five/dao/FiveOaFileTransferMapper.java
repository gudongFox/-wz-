package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaFileTransfer;
import java.util.List;
import java.util.Map;

public interface FiveOaFileTransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaFileTransfer record);

    FiveOaFileTransfer selectByPrimaryKey(Integer id);

    List<FiveOaFileTransfer> selectAll(Map params);

    int updateByPrimaryKey(FiveOaFileTransfer record);
}