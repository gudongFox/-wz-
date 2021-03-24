package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdProjectStep;
import java.util.List;
import java.util.Map;

public interface EdProjectStepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdProjectStep record);

    EdProjectStep selectByPrimaryKey(Integer id);

    List<EdProjectStep> selectAll(Map params);

    int updateByPrimaryKey(EdProjectStep record);

    List<EdProjectStep> listAll(Map params);
}