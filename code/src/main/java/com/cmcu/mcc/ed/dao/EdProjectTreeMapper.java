package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdProjectTree;
import java.util.List;
import java.util.Map;

public interface EdProjectTreeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdProjectTree record);

    EdProjectTree selectByPrimaryKey(Integer id);

    List<EdProjectTree> selectAll(Map params);

    int updateByPrimaryKey(EdProjectTree record);

    void invalidAllTreeNode(int projectId);
}