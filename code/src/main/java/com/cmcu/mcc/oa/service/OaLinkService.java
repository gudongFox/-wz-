package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.service.FiveContentFileService;
import com.cmcu.mcc.five.service.FiveOaDepartmentPostService;
import com.cmcu.mcc.five.service.FiveOaDispatchService;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaLinkMapper;
import com.cmcu.mcc.oa.dao.OaNoticeMapper;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.entity.OaLink;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class OaLinkService {

    @Resource
    OaLinkMapper oaLinkMapper;

    public void remove(int id, String userLogin) {
        OaLink item = oaLinkMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        item.setGmtModified(new Date());
        item.setVisible(false);
        oaLinkMapper.updateByPrimaryKey(item);
    }

    public void update(OaLink oaLink) {
        oaLinkMapper.updateByPrimaryKey(oaLink);
    }

    public OaLink getModelById(int id,String userLogin) {
        return oaLinkMapper.selectByPrimaryKey(id);
    }
    public void add(OaLink oaLink) {
        oaLinkMapper.insert(oaLink);
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params,int pageNum, int pageSize) {
        params.put("visible", true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaLinkMapper.selectAll(params));
        return pageInfo;
    }

    public List<OaLink> selectAll() {
        Map map = new HashMap();
        map.put("visible",true);
        List<OaLink> rValue = oaLinkMapper.selectAll(map);
        return rValue;
    }




}
