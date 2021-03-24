package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdFile;
import java.util.List;
import java.util.Map;

public interface EdFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdFile record);

    EdFile selectByPrimaryKey(Integer id);

    List<EdFile> selectAll(Map params);

    int updateByPrimaryKey(EdFile record);



    List<EdFile> listByBusinessId(String businessId);

    EdFile getOneByBusinessId(String businessId);

    /**
     * 得到协同相关的设计校审文件
     * 取最新的一条
     * @param cpFileId
     * @return
     */
    List<Integer> listIdByCpFile(int cpFileId);
}