package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonAttach;
import java.util.List;
import java.util.Map;

public interface CommonAttachMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonAttach record);

    CommonAttach selectByPrimaryKey(Integer id);

    List<CommonAttach> selectAll(Map params);

    int updateByPrimaryKey(CommonAttach record);


}