package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaStampApply;
import java.util.List;
import java.util.Map;

public interface OaStampApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaStampApply record);

    OaStampApply selectByPrimaryKey(Integer id);

    List<OaStampApply> selectAll(Map params);

    int updateByPrimaryKey(OaStampApply record);
}