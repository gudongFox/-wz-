package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdLayerExtraction;
import java.util.List;
import java.util.Map;

public interface CommonEdLayerExtractionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdLayerExtraction record);

    CommonEdLayerExtraction selectByPrimaryKey(Integer id);

    List<CommonEdLayerExtraction> selectAll(Map params);

    int updateByPrimaryKey(CommonEdLayerExtraction record);
}