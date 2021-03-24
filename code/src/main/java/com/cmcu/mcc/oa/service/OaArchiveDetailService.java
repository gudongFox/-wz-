package com.cmcu.mcc.oa.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaArchiveDetailMapper;
import com.cmcu.mcc.oa.dto.OaArchiveDetailDto;

import com.cmcu.mcc.oa.entity.OaArchiveDetail;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OaArchiveDetailService  {

    @Resource
    OaArchiveDetailMapper oaArchiveDetailMapper;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    CommonCodeService commonCodeService;


    public void remove(int id, String userLogin) {
        OaArchiveDetail item = oaArchiveDetailMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaArchiveDetailMapper.updateByPrimaryKey(item);

    }


    public void update(OaArchiveDetail oaArchiveDetail) {
        oaArchiveDetail.setGmtModified(new Date());
        BeanValidator.check(oaArchiveDetail);
        oaArchiveDetailMapper.updateByPrimaryKey(oaArchiveDetail);
    }


    public OaArchiveDetail getModelById(int id) {
        return oaArchiveDetailMapper.selectByPrimaryKey(id);
    }


    public int getNewModel(int archiveId, String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaArchiveDetail item=new OaArchiveDetail();
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("archiveId",archiveId);
        int seq = (int) PageHelper.count(() -> oaArchiveDetailMapper.selectAll(params));
        item.setArchiveId(archiveId);
        item.setFileType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"档案资料文件类型").toString());
        item.setSeq(seq+1);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setOnline(true);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        oaArchiveDetailMapper.insert(item);
        return item.getId();
    }


    public List<OaArchiveDetail> selectAll(String archiveId){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("archiveId",archiveId);
        List<OaArchiveDetail> list = oaArchiveDetailMapper.selectAll(params);
        return list;
    }

    //已选择资料
    public List<OaArchiveDetailDto> listDetail(String archiveId, String detailIds){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("archiveId",archiveId);
        List<OaArchiveDetail> list = oaArchiveDetailMapper.selectAll(params);
        List<OaArchiveDetailDto> dtoList= Lists.newArrayList();
        for (OaArchiveDetail oaArchiveDetail:list){
            OaArchiveDetailDto oaArchiveDetailDto=OaArchiveDetailDto.adapt(oaArchiveDetail);
            if( detailIds.contains(","+oaArchiveDetail.getId()+",")){
                oaArchiveDetailDto.setSelected(true);
            }else {
                oaArchiveDetailDto.setSelected(false);
            }
            dtoList.add(oaArchiveDetailDto);
        }
        return dtoList;
    }

}
