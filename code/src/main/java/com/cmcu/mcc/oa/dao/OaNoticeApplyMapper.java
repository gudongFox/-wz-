package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaNoticeApply;
import java.util.List;
import java.util.Map;

public interface OaNoticeApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaNoticeApply record);

    OaNoticeApply selectByPrimaryKey(Integer id);

    List<OaNoticeApply> selectAll(Map params);

    int updateByPrimaryKey(OaNoticeApply record);
}