package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdProject;
import java.util.List;
import java.util.Map;

public interface EdProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdProject record);

    EdProject selectByPrimaryKey(Integer id);

    List<EdProject> selectAll(Map params);

    int updateByPrimaryKey(EdProject record);

    List<EdProject> listAttendProject(String userLogin);
}