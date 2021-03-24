package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaLink;
import java.util.List;
import java.util.Map;

public interface OaLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaLink record);

    OaLink selectByPrimaryKey(Integer id);

    List<OaLink> selectAll(Map params);

    int updateByPrimaryKey(OaLink record);
}