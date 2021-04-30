package com.cmcu.common.service;


import com.cmcu.common.dao.CommonCategoryMapper;
import com.cmcu.common.entity.CommonCategory;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonCategoryService {

    @Resource
    CommonCategoryMapper commonCategoryMapper;


    public List<CommonCategory> selectAll(String tenetId,String businessKey){
        Assert.state(StringUtils.isNotEmpty(tenetId)&&StringUtils.isNotEmpty(businessKey),"");
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("tenetId",tenetId);
        params.put("businessKey",businessKey);
        return commonCategoryMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonCategory::getSeq)).collect(Collectors.toList());
    }


}
