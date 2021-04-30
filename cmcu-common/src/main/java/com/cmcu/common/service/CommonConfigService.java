package com.cmcu.common.service;

import com.cmcu.common.dao.CommonConfigMapper;
import com.cmcu.common.entity.CommonConfig;
import com.cmcu.common.util.ModelUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommonConfigService {

    @Resource
    CommonConfigMapper commonConfigMapper;


    public CommonConfig getConfig(){
        List<CommonConfig> configList=commonConfigMapper.selectAll(Maps.newHashMap());
        return configList.get(0);
    }


    public void save(CommonConfig item){
        if(item.getId()!=null&&item.getId()>0){
            commonConfigMapper.updateByPrimaryKey(item);
        }else{
            ModelUtil.setNotNullFields(item);
            commonConfigMapper.insert(item);
        }
    }

}
