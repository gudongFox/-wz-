package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaCarApply;
import java.util.List;
import java.util.Map;

public interface OaCarApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaCarApply record);

    OaCarApply selectByPrimaryKey(Integer id);

    List<OaCarApply> selectAll(Map params);

    int updateByPrimaryKey(OaCarApply record);
}