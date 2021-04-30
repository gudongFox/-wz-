package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonMessage record);

    CommonMessage selectByPrimaryKey(Integer id);

    List<CommonMessage> selectAll(Map params);

    int updateByPrimaryKey(CommonMessage record);

    List<CommonMessage> listLastFiveNoReceived(@Param("receiver") String receiver);

    void markReceived(int id);

    void autoCoMarkReceived();
}