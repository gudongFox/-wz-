package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dto.FiveContentFileDto;
import com.cmcu.mcc.five.dto.FiveOaDepartmentPostDto;
import com.cmcu.mcc.five.dto.FiveOaDispatchDto;
import com.cmcu.mcc.five.service.FiveContentFileService;
import com.cmcu.mcc.five.service.FiveOaDepartmentPostService;
import com.cmcu.mcc.five.service.FiveOaDispatchService;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaNoticeMapper;
import com.cmcu.mcc.oa.dto.OaNoticeDto;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.cmcu.mcc.service.ActService;

import com.common.wx.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.text.ParseException;
import java.util.*;

@Service
public class OaNoticeService  {

    @Resource
    OaNoticeMapper oaNoticeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    ActService actService;
    @Autowired
    MyActService myActService;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    FiveOaDispatchService fiveOaDispatchService;
    @Autowired
    FiveOaDepartmentPostService fiveOaDepartmentPostService;
    @Autowired
    FiveContentFileService fiveContentFileService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    HrEmployeeSysMapper hrEmployeeSysMapper;

    public void remove(int id, String userLogin) {
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");

        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaNoticeMapper.updateByPrimaryKey(item);
    }



    public void update(OaNoticeDto oaNoticeDto) {
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeDto.getId());
        item.setNoticeType(oaNoticeDto.getNoticeType());
        item.setNoticeTitle(oaNoticeDto.getNoticeTitle());
        //xxin
        item.setNoticeDesc(oaNoticeDto.getNoticeDesc());
        item.setPicUrl(oaNoticeDto.getPicUrl());

        item.setDeptName(oaNoticeDto.getDeptName());
        item.setPublishUserName(oaNoticeDto.getPublishUserName());
        item.setTop(oaNoticeDto.getTop());
        item.setPublish(oaNoticeDto.getPublish());
        item.setRemark(oaNoticeDto.getRemark());
        item.setNoticeContent(oaNoticeDto.getNoticeContent());
        item.setGmtModified(new Date());
        item.setPublishDeptName(oaNoticeDto.getPublishDeptName());
        item.setDeptId(oaNoticeDto.getDeptId());
        item.setViewMans(oaNoticeDto.getViewMans());
        item.setViewMansName(oaNoticeDto.getViewMansName());
        item.setPhotoAttachId(oaNoticeDto.getPhotoAttachId());
        item.setPhotoAttachName(oaNoticeDto.getPhotoAttachName());
        item.setAttachIds(oaNoticeDto.getAttachIds());
        item.setAttachName(oaNoticeDto.getAttachName());
        //??????????????? ??????????????????
        if (item.getNoticeType().equals("????????????")&&oaNoticeDto.getFirm()){
            item.setFirm(oaNoticeDto.getFirm());
            item.setNoticeType(item.getNoticeLevel()+",????????????");
        }else if (item.getNoticeType().contains("????????????")){
            item.setFirm(oaNoticeDto.getFirm());
            item.setNoticeType("????????????");
        }
        BeanValidator.check(item);

        oaNoticeMapper.updateByPrimaryKey(item);
    }

    //???????????? ??????????????????????????? ??????
    public void sendToWx(int oaNoticeId){
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeId);
        //??????????????????
        List<String> wxIds = selectEmployeeService.getUserWxIdListByDeptIds(item.getViewMans());
        MessageService.sendPicMessage(wxIds,"/h5/noticeDetail.html?businessKey="+item.getBusinessKey(),item.getNoticeTitle(),item.getDeptName(),item.getPicUrl());


    }
    //???????????? ??????????????? ????????? ??? 2863  ????????????
    public void sendToWxTest(int oaNoticeId){
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeId);
        HrEmployeeSys creator = hrEmployeeSysMapper.selectByUserLogin(item.getCreator());
        //??????????????????
        MessageService.sendPicMessage(Arrays.asList(creator.getWxId(),"sunflowermore"),"/h5/noticeDetail.html?businessKey="+item.getBusinessKey(),"????????????"+item.getNoticeTitle(),item.getDeptName(),item.getPicUrl());
    }


    /**
     * ?????????????????????????????????????????????????????????
     * 2020-12-25??????????????????????????????
     * @param id
     * @param enLogin
     * @return
     */
    public OaNoticeDto getModelById(int id,String enLogin) {
        OaNotice oaNotice = oaNoticeMapper.selectByPrimaryKey(id);
        OaNoticeDto dto = getDto(oaNotice,enLogin);
        if(StringUtils.isNotEmpty(enLogin)) {
            List<String> readUserList = MyStringUtil.getStringList(dto.getReadUser());
            if (!readUserList.contains(enLogin)) {
                readUserList.add(enLogin);
                dto.setReadUser(StringUtils.join(readUserList, ","));
            }
        }
        dto.setPageLoad(dto.getPageLoad()+1);
        oaNoticeMapper.updateByPrimaryKey(dto);
        return dto;
    }


    public OaNotice getModelByBusinessKey(String businessKey,String enLogin) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        Assert.state(PageHelper.count(() -> oaNoticeMapper.selectAll(params)) > 0, "??????????????????!");
        OaNotice oaNotice = oaNoticeMapper.selectAll(params).get(0);
        return getModelById(oaNotice.getId(), enLogin);
    }

    /**
     * ???????????? ???????????? ???????????? ???????????? ???????????? ????????????
     * @param userLogin
     * @param type
     * @return
     */
    public int getNewModelByType(String userLogin,String type) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaNotice item=new OaNotice();
        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        if(!"????????????".equals(type)){
            item.setNoticeType(type);
        }
        item.setNoticeLevel("");
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(false);
        item.setFirm(false);
        item.setPass(false);
       // item.setViewMans(hrEmployeeDto.getDeptId());
       // item.setViewMans(hrEmployeeDto.getUserName());
        if ("????????????".equals(type)){
            item.setFirm(true);
        }
        if("????????????".contains(type)){//????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        String businessKey= EdConst.OA_NOTICE+"_"+item.getId();
        item.setBusinessKey(businessKey);
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public OaNoticeDto getDto(OaNotice item,String enLogin) {
        OaNoticeDto dto = OaNoticeDto.adapt(item);
        dto.setLatest(false);
        dto.setProcessName(item.getPublish() ? "?????????" : "?????????");
        if (!" ".equals(dto.getAttachIds()) && !"".equals(dto.getAttachIds()) && dto.getAttachIds() != null) {
            dto.setAttachId(Integer.parseInt(dto.getAttachIds().split(",")[0]));
        }

        try {
            if (DateUtils.parseDate("2020-12-25", "yyyy-MM-dd").after(dto.getGmtCreate())) {
                dto.setLatest(false);
            } else if (StringUtils.isNotEmpty(enLogin)) {
                List<String> readUserList = MyStringUtil.getStringList(dto.getReadUser());
                dto.setLatest(!readUserList.contains(enLogin));
            }
        } catch (Exception ex) {

        }
        return dto;
    }

    /**
     * ???????????????????????????
     * @param userLogin
     * @return
     */
    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaNotice item=new OaNotice();

        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setNoticeType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,hrEmployeeDto.getDeptName()+"_????????????").toString());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel("????????????");
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(false);
        item.setFirm(false);
        item.setPass(false);
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * ?????????  ???????????????????????????  ??????????????????????????? ???????????????????????????
     * @param businessKey
     * @param types
     */
    public void getNewModelByDispatch(String businessKey,String types,String noticeLevel,String noticeSystemType){
        List<String> typeList = MyStringUtil.getStringList(types);
        String[] split = businessKey.split("_");
        String key=split[0];
        int id= Integer.parseInt(split[1]);

        String title="";
        String creator="";

        if ("fiveOaDispatch".equals(key)){
            FiveOaDispatchDto modelById = fiveOaDispatchService.getModelById(id);
            typeList.add(modelById.getDeptName());
             title=modelById.getDispatchTitle();
             creator=modelById.getCreator();
             Assert.state(modelById.getProcessEnd(),"?????????????????????????????????!???????????????????????????");
        }else {
            FiveOaDepartmentPostDto modelById = fiveOaDepartmentPostService.getModelById(id);
            typeList.add(modelById.getDeptName());
            title=modelById.getTitle();
            creator=modelById.getCreator();
            Assert.state(modelById.getProcessEnd(),"?????????????????????????????????!???????????????????????????");
        }
        for (String type:typeList){
            if ("????????????,????????????".contains(type)){
                int noticeId = getNewModelByKeyLevel(creator, type, title);
                if (noticeId!=0){
                    getFile(businessKey,EdConst.OA_NOTICE+"_"+noticeId);
                }
            }else if ("????????????".contains(type)){
                int noticeId = getNewModelByKeyLevel(creator, type, title,noticeLevel,noticeSystemType);
                if (noticeId!=0){
                    getFile(businessKey,EdConst.OA_NOTICE+"_"+noticeId);
                }
                break;
            } else {
                int noticeId = getNewModelByKeyDept(creator, type, title);
                if (noticeId!=0){
                    getFile(businessKey,EdConst.OA_NOTICE+"_"+noticeId);
                }
            }
        }
    }

    /**
     * ?????? ????????????
     * @param userLogin ?????????
     * @param type ?????????
     * @param title ??????
     * @return
     */
    public int getNewModelByKeyLevel(String userLogin,String type,String title,String noticeLevel,String noticeSystemType) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("noticeTitle",title);
        map.put("noticeType",type);
        //??????????????????????????? ?????????????????????
        if (oaNoticeMapper.selectAll(map).size()>0){
            return 0;
        }
        OaNotice item=new OaNotice();
        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setNoticeType(type);
        item.setNoticeLevel(type);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setViewMansName("????????????????????????????????????");//????????? ??????????????????
        item.setNoticeDesc(title);//????????????
        item.setViewMans(",1,");
        item.setNoticeTitle(title);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);//??????
        item.setPublish(true);//??????
        item.setFirm(false);//?????????
        item.setPass(true);//?????????
        item.setNoticeLevel(noticeLevel);
        item.setNoticeSystemType(noticeSystemType);
        item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        if ("????????????".equals(type)){
            item.setFirm(true);
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * ?????? ????????????  ????????????????????????????????????
     * @param userLogin ?????????
     * @param type ?????????
     * @param title ??????
     * @return
     */
    public int getNewModelByKeyLevel(String userLogin,String type,String title) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        HrDeptDto hrDeptDto = hrDeptService.getModelById(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        Map map=Maps.newHashMap();
         map.put("deleted",false);
         map.put("noticeTitle",title);
         map.put("noticeType",type);
         //??????????????????????????? ?????????????????????
         if (oaNoticeMapper.selectAll(map).size()>0){
             return 0;
         }
        OaNotice item=new OaNotice();
        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrDeptDto.getName());//?????????????????????????????????
        //???????????? ?????????????????????
        item.setDeptId(hrDeptDto.getId());
        item.setDeptName(hrDeptDto.getName());
        item.setNoticeType(type);
        item.setNoticeLevel(type);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setNoticeDesc(title);//????????????
        //2020-12-24HNZ ????????????????????? ????????????????????????????????????  ??????????????????????????????
        if (hrDeptDto.getDeptType().equals("??????")){
            item.setViewMansName(hrDeptDto.getName());//???
            item.setViewMans(","+hrDeptDto.getId()+",");
        }else {
            item.setViewMansName("????????????????????????????????????");//????????? ??????????????????
            item.setViewMans(",1,");
        }


        item.setNoticeTitle(title);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);//??????
        item.setPublish(true);//??????
        item.setFirm(false);//?????????
        item.setPass(true);//?????????
        if("????????????".contains(item.getNoticeType())){//????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        }
        if ("????????????".equals(type)){
            item.setFirm(true);
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * ?????? ????????????
     * @param userLogin
     * @param deptName
     * @param title
     * @return
     */
    public int getNewModelByKeyDept(String userLogin,String deptName,String title ) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        HrDeptDto hrDeptDto = hrDeptService.getModelById(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("noticeTitle",title);
        map.put("noticeLevel","????????????");
        map.put("noticeType","????????????");
        //??????????????????????????? ?????????????????????
        if (oaNoticeMapper.selectAll(map).size()>0){
            return 0;
        }
        OaNotice item=new OaNotice();

        item.setPublishUserName(hrEmployeeDto.getUserName());
        //????????????
        item.setDeptId(hrDeptDto.getId());
        item.setDeptName(hrDeptDto.getName());

        item.setNoticeType("????????????");//?????????

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel("????????????");//?????????
        item.setNoticeTitle(title);
        item.setPublishDeptName(hrDeptDto.getName());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeDesc(title);//????????????
        //2020-12-24HNZ ????????????????????? ????????????????????????????????????  ??????????????????????????????
        if (hrDeptDto.getDeptType().equals("??????")){
            item.setViewMansName(hrDeptDto.getName());
            item.setViewMans(","+hrDeptDto.getId()+",");
        }else {
            item.setViewMansName("????????????????????????????????????");//????????? ??????????????????
            item.setViewMans(",1,");
        }
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(true);
        item.setFirm(false);
        item.setPass(true);
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    @Resource
    CommonFileMapper commonFileMapper;

    /**
     * ????????????
     * ???businessKey ???????????????????????? newKey
     * @param businessKey
     * @param newKey
     */
    public void getFile(String businessKey,String newKey){
        FiveContentFileDto fileDto = fiveContentFileService.getModelByBusinessKey(businessKey, 0);
        String localPath = fileDto.getLocalPath();
        String[] split = fileDto.getFileName().split("\\.");

        String fileName=fileDto.getFileName().substring(0,fileDto.getFileName().lastIndexOf("."))+"_"+businessKey.split("_")[1]+"."+split[split.length-1];
        FileInputStream inputStream = null;
        try {
            File file = new File(localPath);
            inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            //???????????????????????? ????????? attachIds
            int attchId = commonFileService.insert(multipartFile, newKey, 0, fileName, 0, 0, "?????????", fileDto.getCreator());
            CommonFileDto modelById = commonFileService.getModelById(attchId);
            int id= Integer.parseInt(newKey.split("_")[1]);
            OaNotice oaNotice = oaNoticeMapper.selectByPrimaryKey(id);
            oaNotice.setAttachIds(modelById.getAttachId()+"");
            oaNotice.setAttachName(fileName);
            oaNoticeMapper.updateByPrimaryKey(oaNotice);


            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            List<CommonFile> oList = commonFileMapper.selectAll(params);
            oList.forEach(p->{
                p.setBusinessKey(newKey);
                p.setGmtModified(new Date());
                p.setGmtCreate(new Date());
                commonFileMapper.insert(p);
            });

        }catch (Exception e){
            System.err.println("??????????????????"+e.getMessage());
        }

    }


    /**
     * ?????? ???????????? ????????????
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Object> listPagedData(Map<String,Object> params, String enLogin, int pageNum, int pageSize) {
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
        params.put("deleted", false);
        params.put("publish", true);
        if (params.get("noticeTypeList")!=null){
            params.put("types",MyStringUtil.getStringList(params.get("noticeTypeList").toString()));
        }
        List<OaNotice> pageInfoList = oaNoticeMapper.selectAll(params);
        List<Object> list = Lists.newArrayList();
        pageInfoList.forEach(p -> {
            List<Integer> deptIds = MyStringUtil.getIntList(p.getViewMans());
            if(deptIds.contains(hrEmployeeSys.getDeptId())||
                    deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                list.add(getDto(p,enLogin));
            }

        });
        PageInfo<Object> objectPageInfo = PageInfoUtils.list2PageInfo(list, pageNum, pageSize);
        return objectPageInfo;
    }

    public List<OaNotice> selectAll() {
        Map map = new HashMap();
        map.put("deleted",false);
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(map);
        return oaNotices;
    }

    /**
     * ??????  ???????????? ?????????????????? ??????6???
     * @param type
     * @return
     */
    public List<OaNoticeDto> listFirmDateByType(String type,String enLogin){
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
        int number=8;
        //2021-01-04HNZ ????????????8????????? ??????6?????????
        if (type!=""&&type.contains("??????")){
            number=6;
        }
        List<String> noticeTypeList = MyStringUtil.getStringList(type);
        List<OaNoticeDto> list = Lists.newArrayList();
        if (noticeTypeList.size()==0){
            return null;
        }
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("publish",true);
        map.put("top",true);
        map.put("types",noticeTypeList);
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(map);
        for (OaNotice dto:oaNotices){
            //?????????????????????????????????
            List<Integer> deptIds = MyStringUtil.getIntList(dto.getViewMans());
            if(deptIds.contains(hrEmployeeSys.getDeptId())||
                    deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                list.add(getDto(dto,enLogin));
                number--;

            }
            if (number==0) break;
        }
        if (list.size()<6){
            map.put("top",false);
            List<OaNotice> pageList = oaNoticeMapper.selectAll(map);
            for (OaNotice dto:pageList){
                //?????????????????????????????????
                List<Integer> deptIds = MyStringUtil.getIntList(dto.getViewMans());
                if(deptIds.contains(hrEmployeeSys.getDeptId())||
                        deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                    list.add(getDto(dto,enLogin));
                    number--;
                }
                if (number==0) break;
            }
        }
        return list;
    }

    /**
     * ?????? ?????????????????? ???????????? ??????6???
     * @param deptId
     * @return
     */
    public List<OaNoticeDto> listDateByDeptName(int deptId,String enLogin){
        HrEmployeeDto hrEmployeeSys = selectEmployeeService.selectByUserLogin(enLogin);
        int number=6;
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("publish",true);
        map.put("deptId", deptId);
        map.put("top",true);
        map.put("noticeType","????????????");
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(map);
        List<OaNoticeDto> list = Lists.newArrayList();
        for (OaNotice dto:oaNotices){
            //?????????????????????????????????
            List<Integer> deptIds = MyStringUtil.getIntList(dto.getViewMans());
            if(deptIds.contains(hrEmployeeSys.getDeptId())||
                    deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                list.add(getDto(dto,enLogin));
                number--;

            }
            if (number==0) break;
        }
        if (list.size()<6){
            map.put("top",false);
            List<OaNotice> pageList = oaNoticeMapper.selectAll(map);
            for (OaNotice dto:pageList){
                //?????????????????????????????????
                List<Integer> deptIds = MyStringUtil.getIntList(dto.getViewMans());
                if(deptIds.contains(hrEmployeeSys.getDeptId())||
                        deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                    list.add(getDto(dto,enLogin));
                    number--;
                }
                if (number==0) break;
            }

        }
        return list;
    }

    public Map  listDateByDeptName(String enLogin){
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(enLogin);

        Map result=Maps.newHashMap();
        result.put("partyOfficeList",listDateByDeptName(59,enLogin));//???????????????
        result.put("partyBuildingList",listDateByDeptName(100,enLogin));//???????????????
        result.put("newsBusinessList",listDateByDeptName(72,enLogin));//???????????????
        result.put("newsMessageList",listDateByDeptName(48,enLogin));//???????????????
        result.put("newsSecrecyList",listDateByDeptName(11,enLogin));//???????????????????????????
        result.put("newsFinanceList",listDateByDeptName(18,enLogin));//???????????????
        result.put("newsPoliticsList",listDateByDeptName(38,enLogin));//???????????????
        result.put("newsLogisticsList",listDateByDeptName(29,enLogin));//???????????????
        result.put("newsTrainList",listDateByDeptName(9,enLogin));//??????????????????????????????????????????
        result.put("laborUnionList",listDateByDeptName(67,enLogin));//???????????????
        result.put("laborScientificList",listDateByDeptName(101,enLogin));//????????????????????????
        //???????????????????????????????????????
        HrDeptDto deptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());
        if (deptDto.getDeptType().equals("??????")){
            result.put("designNoticeList",listDateByDeptName(deptDto.getId(),enLogin));//????????????
        }

        return result;
    }



    //???????????? ???????????? ???????????? ????????????
    public PageInfo<Object> listPagedDataByType(Map<String,Object> params, String type, String enLogin, int pageNum, int pageSize) {
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
        //params.put("selfCreator",enLogin);//selfCreator  ???????????????????????????
        params.put("deleted", false);
        params.put("noticeType", type);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaNoticeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaNotice item = (OaNotice) p;
            List<Integer> deptIds = MyStringUtil.getIntList(item.getViewMans());
            if(deptIds.contains(hrEmployeeSys.getDeptId())||
                    deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                list.add(getDto(item,enLogin));
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    //????????????
    public PageInfo<Object> fuzzySearch(Map<String,Object> params, String enLogin, int pageNum, int pageSize) {
        params.put("selfCreator", enLogin);//selfCreator  ???????????????????????????
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaNoticeMapper.fuzzySearch(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaNotice item = (OaNotice) p;
            list.add(getDto(item,enLogin));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<String> getOtherType(){
        List<String> allType=new ArrayList<>();
        allType.add("????????????");
        allType.add("????????????");
        allType.add("????????????");
        return allType;
    }

    public List<OaNoticeDto> getNewsPhoto(){
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("noticeType","????????????");
        //???????????? ???????????????5??? ??????????????????
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(params);
        List<OaNoticeDto> list=Lists.newArrayList();
        for (OaNotice oaNotice:oaNotices){
            if (list.size()<5){
                OaNoticeDto dto = getDto(oaNotice,"");
                if (!"".equals(dto.getPhotoAttachId())){
                    list.add(dto);
                }
            }
        }
        return list;
    }

    /**
     * ?????????????????? ????????????-??????
     * @param params
     * @param enLogin ???????????????
     * @param deptName ????????????
     * @param pageNum ??????
     * @param pageSize ????????????
     * @return
     */
    public  PageInfo<Object> listPagedDataByUserLogin(Map<String,Object> params, String enLogin, String deptName , int pageNum, int pageSize) {
        HrEmployeeDto hrEmployeeSys = selectEmployeeService.selectByUserLogin(enLogin);
        params.put("deptName",deptName);
        if (!deptName.equals(hrEmployeeSys.getDeptName())){
        params.put("publish",true);
        }
        params.put("deleted", false);
        params.put("noticeType", "????????????");



        List<OaNotice> noticeList = oaNoticeMapper.selectAll(params);
        List<Object> list = Lists.newArrayList();
        noticeList.forEach(p -> {
            List<Integer> deptIds = MyStringUtil.getIntList(p.getViewMans());
            if(deptIds.contains(hrEmployeeSys.getDeptId())||
                    deptIds.contains(selectEmployeeService.getHeadDeptId(hrEmployeeSys.getDeptId()))||deptIds.contains(1)){
                list.add(getDto(p,enLogin));
            }
        });

        PageInfo<Object> objectPageInfo = PageInfoUtils.list2PageInfo(list, pageNum, pageSize);
        return objectPageInfo;
    }

    /**
     * ?????????????????? ????????????-??????
     * @param params
     * @param enLogin ???????????????
     * @param deptName ????????????
     * @param pageNum ??????
     * @param pageSize ????????????
     * @return
     */
    public  List<Map> listPagedDataOrderType(Map<String,Object> params, String enLogin, String deptName, int pageNum, int pageSize) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(enLogin);
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE, deptName + "_????????????");
        List<Map> list=Lists.newArrayList();
        params.put("deptName",deptName);
        if (!deptName.equals(hrEmployeeDto.getDeptName())){
            params.put("publish",true);
        }
        params.put("deleted", false);
        params.put("noticeLevels", "????????????");

        for (CommonCode code:commonCodes){
            params.put("noticeType", code.getName());
            PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, 5).doSelectPageInfo(() -> oaNoticeMapper.selectAll(params));
            List<Object> listtype = Lists.newArrayList();
            pageInfo.getList().forEach(p -> {
                OaNotice item = (OaNotice) p;
                listtype.add(getDto(item,enLogin));
            });
            Map<String,Object> mapList=Maps.newHashMap();
            mapList.put("noticeType",code.getName());
            mapList.put("noticeList",listtype);

            list.add(mapList);
        }
        return list;
    }


    public String getAcceptUser(int oaNoticeId, String type) {
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeId);
        List<String> users = new ArrayList<>();
        if(type.equalsIgnoreCase("publish")){
            List<Integer> deptLists = MyStringUtil.getIntList(item.getViewMans());
            for(int deptId:deptLists) {
                List<HrEmployeeDto> hrEmployeeDtos = hrEmployeeService.listEmployeeByDeptId(deptId, true);
                for(HrEmployeeDto dto:hrEmployeeDtos){
                    HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(dto.getUserLogin());
                    if(!Strings.isNullOrEmpty(hrEmployeeSys.getWxId())){
                        users.add(dto.getUserName()+"("+hrEmployeeSys.getWxId()+")");
                    }
                }
            }
        }else{
            HrEmployeeSys creator = hrEmployeeSysMapper.selectByUserLogin(item.getCreator());
            if(!Strings.isNullOrEmpty(creator.getWxId())){
                users.add(item.getCreatorName()+"("+creator.getWxId()+")");
            }
            users.add("?????????(sunflowermore)");
        }

        return users.toString();
    }
}
