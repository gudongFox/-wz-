package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdProjectTreeState;
import java.util.List;
import java.util.Map;

public interface EdProjectTreeStateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdProjectTreeState record);

    EdProjectTreeState selectByPrimaryKey(Integer id);

    List<EdProjectTreeState> selectAll(Map params);

    int updateByPrimaryKey(EdProjectTreeState record);
}