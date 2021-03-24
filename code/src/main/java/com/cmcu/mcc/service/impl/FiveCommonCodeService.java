package com.cmcu.mcc.service.impl;

import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.mcc.comm.MccConst;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveCommonCodeService {
    @Autowired
    CommonCodeService commonCodeService;
    @Resource
    CommonCodeMapper commonCodeMapper;


    public CommonCode selectByCodeName(String codeCatalog,String name){
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE, codeCatalog);
        for (CommonCode dto:commonCodes){
            if (dto.getName().equals(name)){
                return dto;
            }
        }
        return null;
    }

    public List<CommonCode> listDataByCodeAndCodeCatalog(String code, String codeCatalog) {
        Map map = new HashMap();
        map.put("codeCatalog",codeCatalog);
        map.put("code",code);
        List<CommonCode> list = commonCodeMapper.selectAll(map);
        return list;
    }

    public List<String> listAllMajorName() {
        List<String> list= Lists.newArrayList();
        List<CommonCodeDto> codeDtoList = commonCodeService.listDataByCatalog(MccConst.APP_CODE, "设计专业");
        for (CommonCodeDto dto:codeDtoList){
            list.add(dto.getName());
        }
        return list;
    }

}
