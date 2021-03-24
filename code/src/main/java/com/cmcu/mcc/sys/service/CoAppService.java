package com.cmcu.mcc.sys.service;

import com.cmcu.mcc.sys.dao.CoAppMapper;
import com.cmcu.mcc.sys.entity.CoApp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CoAppService {

    @Resource
    CoAppMapper coAppMapper;



    public CoApp getLatest(){
        return coAppMapper.getLatest();
    }

}
