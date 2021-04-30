package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonFormDetail;
import java.util.List;
import java.util.Map;

public interface CommonFormDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonFormDetail record);

    CommonFormDetail selectByPrimaryKey(Integer id);

    List<CommonFormDetail> selectAll(Map params);

    int updateByPrimaryKey(CommonFormDetail record);

    List<CommonFormDetail> listDataByFormId(int formId);
}