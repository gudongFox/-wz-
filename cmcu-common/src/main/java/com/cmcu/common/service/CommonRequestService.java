package com.cmcu.common.service;

import com.cmcu.common.dao.CommonRequestMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonRequest;
import com.cmcu.common.util.GuavaCacheService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommonRequestService {


    @Resource
    CommonRequestMapper commonRequestMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;

    public PageInfo<CommonRequest> listPagedData(int pageNum, int pageSize ,Map<String,Object> params){

        if(params.containsKey("enLogin")){
            params.put("tenetId",commonUserService.getTenetId(params.get("enLogin").toString()));
        }

        PageInfo<CommonRequest> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->commonRequestMapper.selectAll(params));
        return pageInfo;
    }


    public List<CommonCodeDto> listRequestName(String enLogin){
        String tenetId=commonUserService.getTenetId(enLogin);
        List<CommonRequest> oList = guavaCacheService.get("IDENTIFIED_REQUEST_" + tenetId, () -> Optional.ofNullable(commonRequestMapper.listIdentifiedRequestName(tenetId)));
        List<CommonCodeDto> list= Lists.newArrayList();
        list.add(new CommonCodeDto("","全部"));
        oList.forEach(p-> list.add(new CommonCodeDto(p.getRequestName())));
        return list;
    }


}
