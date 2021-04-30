package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdDwgPicture;
import java.util.List;
import java.util.Map;

public interface CommonEdDwgPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdDwgPicture record);

    CommonEdDwgPicture selectByPrimaryKey(Integer id);

    List<CommonEdDwgPicture> selectAll(Map params);

    int updateByPrimaryKey(CommonEdDwgPicture record);

    CommonEdDwgPicture selectLatestOne();
}