package com.cmcu.common.service;

import com.cmcu.common.dao.CommonEdDwgStdMapper;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonEdDwgStd;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonEdDwgStdService {
    @Resource
    CommonEdDwgStdMapper commonEdDwgStdMapper;
    @Autowired
    CommonCodeService commonCodeService;
    @Autowired
    CommonUserService commonUserService;


    public PageInfo<CommonEdDwgStd> listPagedData(Map<String,Object> params,String enLogin, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("tenetId",commonUserService.getTenetId(enLogin));
        PageInfo<CommonEdDwgStd> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonEdDwgStdMapper.selectAll(params));
        return pageInfo;
    }

    public List<CommonEdDwgStd> selectAll(String enLogin){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("tenetId",commonUserService.getTenetId(enLogin));
        return commonEdDwgStdMapper.selectAll(params);
    }

    public CommonEdDwgStd getNewModel(String enLogin) {
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("tenetId",userDto.getTenetId());
        CommonEdDwgStd item=new CommonEdDwgStd();
        item.setSeq((int)PageHelper.count(()->commonEdDwgStdMapper.selectAll(params))+1);
        item.setMajorName(commonCodeService.selectDefaultCodeValue(userDto.getTenetId(),"设计专业").toString());
        item.setTenetId(userDto.getTenetId());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setStandard(true);
        item.setCreator(enLogin);
        item.setCreatorName(userDto.getCnName());
        item.setDeptId(userDto.getDeptId());
        item.setDeptName(userDto.getDeptName());
        item.setId(0);
        ModelUtil.setNotNullFields(item);
        return item;
    }

    public void update(CommonEdDwgStd commonEdDwgStd){
        if (commonEdDwgStd.getId()==0){
            commonEdDwgStd.setGmtModified(new Date());
            commonEdDwgStdMapper.insert(commonEdDwgStd);
        }else {
            CommonEdDwgStd item = commonEdDwgStdMapper.selectByPrimaryKey(commonEdDwgStd.getId());
            item.setStandard(commonEdDwgStd.getStandard());
            item.setAttachId(commonEdDwgStd.getAttachId());
            item.setMajorName(commonEdDwgStd.getMajorName());
            item.setStdDesc(commonEdDwgStd.getStdDesc());
            item.setStdName(commonEdDwgStd.getStdName());
            item.setSeq(commonEdDwgStd.getSeq());
            item.setRemark(commonEdDwgStd.getRemark());
            item.setGmtModified(new Date());
            BeanValidator.check(item);
            commonEdDwgStdMapper.updateByPrimaryKey(item);
        }

    }

    public void remove(int id) {
        CommonEdDwgStd item = commonEdDwgStdMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        commonEdDwgStdMapper.updateByPrimaryKey(item);
    }


    public CommonEdDwgStd getModelById(int id) {
        return commonEdDwgStdMapper.selectByPrimaryKey(id);
    }



}
