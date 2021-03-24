package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaSecretSystem;
import java.util.List;
import java.util.Map;

public interface FiveOaSecretSystemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSecretSystem record);

    FiveOaSecretSystem selectByPrimaryKey(Integer id);

    List<FiveOaSecretSystem> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSecretSystem record);
}