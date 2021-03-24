package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaFileInstruction;
import java.util.List;
import java.util.Map;

public interface FiveOaFileInstructionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaFileInstruction record);

    FiveOaFileInstruction selectByPrimaryKey(Integer id);

    List<FiveOaFileInstruction> selectAll(Map params);

    int updateByPrimaryKey(FiveOaFileInstruction record);
}