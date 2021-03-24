package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaNoticeMember;
import java.util.List;
import java.util.Map;

public interface OaNoticeMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaNoticeMember record);

    OaNoticeMember selectByPrimaryKey(Integer id);

    List<OaNoticeMember> selectAll(Map params);

    int updateByPrimaryKey(OaNoticeMember record);
}