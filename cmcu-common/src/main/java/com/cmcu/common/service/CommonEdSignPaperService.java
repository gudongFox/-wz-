package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdSignPaperMapper;
import com.cmcu.common.entity.CommonEdSignPaper;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CommonEdSignPaperService {

    @Resource
    CommonEdSignPaperMapper commonEdSignPaperMapper;

    @Resource
    CommonUserService commonUserService;

    public List<String>  getSignPaperIdList(String tenetId,int fileId,String paperData,String enLogin) {
        List<String> resultIdList = Lists.newArrayList();
        List<String> paperList = Arrays.asList(StringUtils.split(paperData, ","));
        Date now = new Date();
        String cnName = commonUserService.getCnNames(enLogin);
        for (String paper : paperList) {
            String uuid = DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS");
            CommonEdSignPaper model = new CommonEdSignPaper();
            String[] info = paper.split("_");
            model.setProjectName(info[0]);
            model.setDrawingName(info[1]);
            model.setDrawingNo(info[2]);
            if(info.length>3) {
                model.setChargeName(info[3]);
            }
            if(info.length>4){
                model.setAuditName(info[4]);
            }
            if(info.length>5){
                model.setProofreadName(info[5]);
            }
            model.setOtherData(paperData);
            model.setGmtCreate(now);
            model.setGmtModified(now);
            model.setDeleted(false);
            model.setCreator(enLogin);
            model.setCreatorName(cnName);
            model.setTenetId(tenetId);
            model.setSignId(uuid);
            model.setFileId(fileId);
            ModelUtil.setNotNullFields(model);
            commonEdSignPaperMapper.insert(model);
            resultIdList.add(model.getSignId());
        }
        return resultIdList;
    }


    public CommonEdSignPaper getModelBySignId(String signId){
        if(StringUtils.isNotEmpty(signId)) {
            Map params = Maps.newHashMap();
            params.put("signId", signId);
            if(PageHelper.count(()->commonEdSignPaperMapper.selectAll(params))>0){
                return commonEdSignPaperMapper.selectAll(params).get(0);
            }
        }
        return new CommonEdSignPaper();
    }

}
