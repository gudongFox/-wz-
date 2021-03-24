package com.cmcu.mcc.five.service;

import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaDecisionMakingMapper;
import com.cmcu.mcc.five.dao.FiveOaDecisionMarkingDetailMapper;
import com.cmcu.mcc.five.dto.FiveOaDecisionMakingDetailDto;
import com.cmcu.mcc.five.dto.FiveOaDecisionMakingDto;
import com.cmcu.mcc.five.dto.FiveOaDecisionMarkingDetailDto;
import com.cmcu.mcc.five.dto.FiveOaSignQuoteDto;
import com.cmcu.mcc.five.entity.FiveActRelevance;
import com.cmcu.mcc.five.entity.FiveOaDecisionMaking;
import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaDecisionMakingDetailService {
    @Resource
    FiveOaDecisionMakingMapper fiveOaDecisionMakingMapper;
    @Resource
    FiveOaDecisionMarkingDetailMapper fiveOaDecisionMarkingDetailMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    FiveOaSignQuoteService fiveOaSignQuoteService;
    @Autowired
    MyActService myActService;
    @Autowired
    ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveOaDecisionMakingDetailService fiveOaDecisionMarkingDetail;
    @Autowired
    ActService actService;
    public void remove(int id){
        fiveOaDecisionMarkingDetailMapper.deleteByPrimaryKey(id);
    }

    public void add(FiveOaDecisionMarkingDetail item){

        item.setDeleted(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaDecisionMarkingDetailMapper.insert(item);
        item.setBusinessKey("fiveOaDecisionMarkingDetail_"+item.getId());
        fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(item);
    }

    public void update(FiveOaDecisionMakingDetailDto fiveOaDecisionMakingDetailDto){

       FiveOaDecisionMarkingDetail model = fiveOaDecisionMarkingDetailMapper.selectByPrimaryKey(fiveOaDecisionMakingDetailDto.getId());
       BeanUtils.copyProperties(fiveOaDecisionMakingDetailDto, model);
        BeanValidator.check(model);

       fiveOaDecisionMarkingDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaDecisionMarkingDetail getModelById(int id){
        return fiveOaDecisionMarkingDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaDecisionMarkingDetailDto> getModelByMainBusiness(String  mainBusiness)
    {
        Map param=new HashMap();
        param.put("mainBusiness",mainBusiness);
        List<FiveOaDecisionMarkingDetailDto> list=Lists.newArrayList();
        fiveOaDecisionMarkingDetailMapper.selectAll(param).forEach(p->{
            list.add(getDto(p));
        });
        return list;
    }
    public List<FiveOaDecisionMarkingDetailDto> getModelByMainBusiness(String  mainBusiness,String userLogin)
    {
        Map param=new HashMap();
        param.put("mainBusiness",mainBusiness);
        param.put("containUser",userLogin);
        List<FiveOaDecisionMarkingDetailDto> list=Lists.newArrayList();
        fiveOaDecisionMarkingDetailMapper.selectAll(param).forEach(p->{
            list.add(getDto(p));
        });
        return list;
    }
    public PageInfo<Object> getNotCompleted(Map params,int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDecisionMarkingDetailMapper.selectAll(params));
        return pageInfo;
    }

    public PageInfo<Object> listPageDate(Map params,int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDecisionMarkingDetailMapper.selectAll(params));
        return pageInfo;
    }

    public FiveOaDecisionMarkingDetail getNewModelDetail(String mainBusiness,String userLogin){
        Map param=new HashMap();
        param.put("mainBusiness",mainBusiness);
        param.put("deleted",false);
        int seq=fiveOaDecisionMarkingDetailMapper.selectAll(param).size()+1;
        FiveOaDecisionMarkingDetail item=new FiveOaDecisionMarkingDetail();
        item.setMainBusiness(mainBusiness);
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
        item.setDeptId(0);
        item.setDeptName("");
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setId(0);
        item.setSeq(seq);
        item.setIssueStatus("待办中");
        add(item);
        return item;
    }


    public FiveOaDecisionMarkingDetail getNewModelDetailByType(String userLogin){
        Map param=new HashMap();
        param.put("issueStatus","待办中");
        param.put("deleted",false);
        param.put("creator",userLogin);
        int seq=fiveOaDecisionMarkingDetailMapper.selectAll(param).size()+1;
        FiveOaDecisionMarkingDetail oldDetail=new FiveOaDecisionMarkingDetail();
        if (seq>1){
             oldDetail = fiveOaDecisionMarkingDetailMapper.selectAll(param).get(0);
        }
        FiveOaDecisionMarkingDetail item=new FiveOaDecisionMarkingDetail();
        item.setMainBusiness("");
        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
        item.setDeptId(0);
        item.setDeptName("");
        item.setDetailType(oldDetail.getDetailType());
        item.setIssueStatus("待办中");
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setId(0);
        item.setSeq(seq);
        return item;
    }

    public List<FiveOaDecisionMarkingDetail> getModelByLinkBusiness(String linkedBusiness) {
        Map param=new HashMap();
        param.put("deleted",false);
        param.put("linkedBusiness",linkedBusiness);
        return  fiveOaDecisionMarkingDetailMapper.selectAll(param);
    }

    @Resource
    CommonFileService commonFileService;
    public FiveOaDecisionMarkingDetailDto getDto(FiveOaDecisionMarkingDetail detail){
        FiveOaDecisionMarkingDetailDto item = FiveOaDecisionMarkingDetailDto.adapt(detail);

        List<CommonFileDto> commonFileDtos = commonFileService.listData(item.getBusinessKey(), 0, "");
        item.setFileList(commonFileDtos);

        return item;
    }

}
