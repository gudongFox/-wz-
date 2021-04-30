package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonWxMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonWxMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonWxMessage record);

    CommonWxMessage selectByPrimaryKey(Integer id);

    List<CommonWxMessage> selectAll(Map params);

    int updateByPrimaryKey(CommonWxMessage record);


    CommonWxMessage getLatestTryMessage(@Param("msgType")String msgType, @Param("msgTitle") String msgTitle,@Param("msgUrl") String msgUrl,@Param("toUser") String toUser);
}