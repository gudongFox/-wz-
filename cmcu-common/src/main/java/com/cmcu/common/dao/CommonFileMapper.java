package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;

public interface CommonFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonFile record);

    CommonFile selectByPrimaryKey(Integer id);

    List<CommonFile> selectAll(Map params);

    int updateByPrimaryKey(CommonFile record);

    CommonFile selectLatestByBusinessKey(String businessKey);

    CommonFile selectByFileName(@Param("businessKey") String businessKey,@Param("dirId") int dirId,@Param("fileName") String fileName);

    long  selectSizeByBusinessKey(@Param("businessKey") String businessKey,@Param("dirId") int dirId);
}