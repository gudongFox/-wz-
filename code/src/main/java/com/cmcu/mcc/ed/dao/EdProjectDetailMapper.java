package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdProjectDetail;
import java.util.List;

public interface EdProjectDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdProjectDetail record);

    EdProjectDetail selectByPrimaryKey(Integer id);

    List<EdProjectDetail> selectAll();

    int updateByPrimaryKey(EdProjectDetail record);
}