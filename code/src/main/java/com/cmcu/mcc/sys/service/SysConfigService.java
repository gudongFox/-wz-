package com.cmcu.mcc.sys.service;

import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.sys.dao.SysConfigMapper;
import com.cmcu.mcc.sys.entity.SysConfig;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysConfigService {


    @Resource
    SysConfigMapper sysConfigMapper;

    public SysConfig getConfig(){
        return sysConfigMapper.selectAll(Maps.newHashMap()).get(0);
    }

    public void update(SysConfig model) {
        BeanValidator.check(model);
        MccConst.DEFAULT_URL = model.getDefaultUrl();
        CommonAttachService.BASE_PATH = model.getDirPath();
        sysConfigMapper.updateByPrimaryKey(model);
    }


}
