package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaNotice;
import java.util.List;
import java.util.Map;

public interface OaNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaNotice record);

    OaNotice selectByPrimaryKey(Integer id);

    List<OaNotice> selectAll(Map params);

    List<OaNotice> fuzzySearch(Map params);

    int updateByPrimaryKey(OaNotice record);
}