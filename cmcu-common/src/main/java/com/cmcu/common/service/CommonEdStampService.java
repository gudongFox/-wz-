package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdStampMapper;
import com.cmcu.common.entity.CommonEdStamp;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonEdStampService {

    @Resource
    CommonEdStampMapper commonEdStampMapper;


    public List<CommonEdStamp> selectAll(String tenetId){
        Map params= Maps.newHashMap();
        params.put("tenetId",tenetId);
        params.put("deleted",false);
        return commonEdStampMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonEdStamp::getSeq)).collect(Collectors.toList());
    }

    public CommonEdStamp getModelById(int id){
        return commonEdStampMapper.selectByPrimaryKey(id);
    }

    public void remove(int id){
        CommonEdStamp item=commonEdStampMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        commonEdStampMapper.updateByPrimaryKey(item);
    }

    public CommonEdStamp  getNewModel(String tenetId){
        Map params= Maps.newHashMap();
        params.put("tenetId",tenetId);
        params.put("deleted",false);
        long seq= PageHelper.count(()->commonEdStampMapper.selectAll(params))+1;
        CommonEdStamp item=new CommonEdStamp();
        item.setStampType("一级注册建筑师");
        item.setTenetId(tenetId);
        item.setDeleted(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setSeq((int)seq);
        return item;

    }



    public int save(CommonEdStamp latest) {
        CommonEdStamp item = new CommonEdStamp();
        if (latest.getId() != null && latest.getId() > 0) {
            item = commonEdStampMapper.selectByPrimaryKey(latest.getId());
        }
        item.setTenetId(latest.getTenetId());
        item.setGmtCreate(latest.getGmtCreate());
        item.setStampType(latest.getStampType());
        item.setUserName(latest.getUserName());
        item.setAttachId(latest.getAttachId());
        item.setSeq(latest.getSeq());
        item.setGmtModified(new Date());
        item.setRemark(latest.getRemark());
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        if (latest.getId() != null && latest.getId() > 0) {
            commonEdStampMapper.updateByPrimaryKey(item);
        } else {
            item.setGmtCreate(new Date());
            commonEdStampMapper.insert(item);
        }
        return item.getId();
    }





}
