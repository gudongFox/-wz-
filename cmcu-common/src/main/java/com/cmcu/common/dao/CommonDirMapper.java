package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonDir;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonDirMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CommonDir record);

    CommonDir selectByPrimaryKey(Integer id);

    List<CommonDir> selectAll(Map params);

    int updateByPrimaryKey(CommonDir record);

    CommonDir selectByCnName(@Param("businessKey") String businessKey, @Param("parentId") int parentId, @Param("cnName") String cnName);

    long  selectChildSizeByBusinessKey(@Param("businessKey") String businessKey,@Param("parentId") int parentId);
}