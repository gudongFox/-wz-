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
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

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
        //部门公告转 通知公告触发
        if (item.getNoticeType().equals("部门空间")&&oaNoticeDto.getFirm()){
            item.setFirm(oaNoticeDto.getFirm());
            item.setNoticeType(item.getNoticeLevel()+",通知公告");
        }else if (item.getNoticeType().contains("部门空间")){
            item.setFirm(oaNoticeDto.getFirm());
            item.setNoticeType("部门空间");
        }
        BeanValidator.check(item);

        oaNoticeMapper.updateByPrimaryKey(item);
    }

    //企业微信 根据通告的发布范围 发送
    public void sendToWx(int oaNoticeId){
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeId);
        //发送企业微信
        List<String> wxIds = selectEmployeeService.getUserWxIdListByDeptIds(item.getViewMans());
        MessageService.sendPicMessage(wxIds,"/h5/noticeDetail.html?businessKey="+item.getBusinessKey(),item.getNoticeTitle(),item.getDeptName(),item.getPicUrl());


    }
    //企业微信 根据发送给 创建人 和 2863  发送预览
    public void sendToWxTest(int oaNoticeId){
        OaNotice item = oaNoticeMapper.selectByPrimaryKey(oaNoticeId);
        HrEmployeeSys creator = hrEmployeeSysMapper.selectByUserLogin(item.getCreator());
        //发送企业微信
        MessageService.sendPicMessage(Arrays.asList(creator.getWxId(),"sunflowermore"),"/h5/noticeDetail.html?businessKey="+item.getBusinessKey(),"【预览】"+item.getNoticeTitle(),item.getDeptName(),item.getPicUrl());
    }


    /**
     * 查询数据、并记录已读状态、增加阅读次数
     * 2020-12-25之前的强制不标记为新
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
        Assert.state(PageHelper.count(() -> oaNoticeMapper.selectAll(params)) > 0, "该信息不存在!");
        OaNotice oaNotice = oaNoticeMapper.selectAll(params).get(0);
        return getModelById(oaNotice.getId(), enLogin);
    }

    /**
     * 集团制度 公司制度 通知公告 公司新闻 文件简报 类型新增
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
        if(!"公司制度".equals(type)){
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
        if ("通知公告".equals(type)){
            item.setFirm(true);
        }
        if("文件简报".contains(type)){//文件简报默认图片
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //通知公告图片默认
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
        dto.setProcessName(item.getPublish() ? "已发布" : "审核中");
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
     * 各部门新增信息发布
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
        item.setNoticeType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,hrEmployeeDto.getDeptName()+"_电子公告").toString());

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel("部门空间");
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
     * 发文转  公司发文转文件简报  部门发文转通知公告 默认发布范围全公司
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
             Assert.state(modelById.getProcessEnd(),"流程未结束，转新闻失败!请确认流程是否完结");
        }else {
            FiveOaDepartmentPostDto modelById = fiveOaDepartmentPostService.getModelById(id);
            typeList.add(modelById.getDeptName());
            title=modelById.getTitle();
            creator=modelById.getCreator();
            Assert.state(modelById.getProcessEnd(),"流程未结束，转新闻失败!请确认流程是否完结");
        }
        for (String type:typeList){
            if ("文件简报,通知公告".contains(type)){
                int noticeId = getNewModelByKeyLevel(creator, type, title);
                if (noticeId!=0){
                    getFile(businessKey,EdConst.OA_NOTICE+"_"+noticeId);
                }
            }else if ("公司制度".contains(type)){
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
     * 转发 公司制度
     * @param userLogin 发起人
     * @param type 大分类
     * @param title 标题
     * @return
     */
    public int getNewModelByKeyLevel(String userLogin,String type,String title,String noticeLevel,String noticeSystemType) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("noticeTitle",title);
        map.put("noticeType",type);
        //判断是否已经新增了 如果是直接返回
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
        item.setViewMansName("中国五洲工程设计有限公司");//转公告 默认发布范围
        item.setNoticeDesc(title);//公告描述
        item.setViewMans(",1,");
        item.setNoticeTitle(title);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);//置顶
        item.setPublish(true);//发布
        item.setFirm(false);//转公告
        item.setPass(true);//新闻转
        item.setNoticeLevel(noticeLevel);
        item.setNoticeSystemType(noticeSystemType);
        item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        if ("通知公告".equals(type)){
            item.setFirm(true);
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * 转发 按分类转  例如：通知公告，文件简报
     * @param userLogin 发起人
     * @param type 大分类
     * @param title 标题
     * @return
     */
    public int getNewModelByKeyLevel(String userLogin,String type,String title) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        HrDeptDto hrDeptDto = hrDeptService.getModelById(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
        Map map=Maps.newHashMap();
         map.put("deleted",false);
         map.put("noticeTitle",title);
         map.put("noticeType",type);
         //判断是否已经新增了 如果是直接返回
         if (oaNoticeMapper.selectAll(map).size()>0){
             return 0;
         }
        OaNotice item=new OaNotice();
        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrDeptDto.getName());//发布部门默认为二级单位
        //发布单位 应该是二级单位
        item.setDeptId(hrDeptDto.getId());
        item.setDeptName(hrDeptDto.getName());
        item.setNoticeType(type);
        item.setNoticeLevel(type);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setNoticeDesc(title);//公告描述
        //2020-12-24HNZ 如果是设计单位 默认范围为发起人二级单位  职能部门默认为全公司
        if (hrDeptDto.getDeptType().equals("设计")){
            item.setViewMansName(hrDeptDto.getName());//转
            item.setViewMans(","+hrDeptDto.getId()+",");
        }else {
            item.setViewMansName("中国五洲工程设计有限公司");//转公告 默认发布范围
            item.setViewMans(",1,");
        }


        item.setNoticeTitle(title);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);//置顶
        item.setPublish(true);//发布
        item.setFirm(false);//转公告
        item.setPass(true);//新闻转
        if("文件简报".contains(item.getNoticeType())){//文件简报默认图片
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //通知公告图片默认
            item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        }
        if ("通知公告".equals(type)){
            item.setFirm(true);
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * 转发 部门空间
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
        map.put("noticeLevel","部门发文");
        map.put("noticeType","部门空间");
        //判断是否已经新增了 如果是直接返回
        if (oaNoticeMapper.selectAll(map).size()>0){
            return 0;
        }
        OaNotice item=new OaNotice();

        item.setPublishUserName(hrEmployeeDto.getUserName());
        //二级单位
        item.setDeptId(hrDeptDto.getId());
        item.setDeptName(hrDeptDto.getName());

        item.setNoticeType("部门空间");//大分类

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel("部门发文");//小分类
        item.setNoticeTitle(title);
        item.setPublishDeptName(hrDeptDto.getName());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeDesc(title);//公告描述
        //2020-12-24HNZ 如果是设计单位 默认范围为发起人二级单位  职能部门默认为全公司
        if (hrDeptDto.getDeptType().equals("设计")){
            item.setViewMansName(hrDeptDto.getName());
            item.setViewMans(","+hrDeptDto.getId()+",");
        }else {
            item.setViewMansName("中国五洲工程设计有限公司");//转公告 默认发布范围
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
     * 附件转储
     * 将businessKey 的附件备份存储到 newKey
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
            //保存发文正文内容 设置到 attachIds
            int attchId = commonFileService.insert(multipartFile, newKey, 0, fileName, 0, 0, "发文转", fileDto.getCreator());
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
            System.err.println("附件转换失败"+e.getMessage());
        }

    }


    /**
     * 首页 企业动态 查看更多
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
     * 首页  企业动态 公司集团制度 总共6条
     * @param type
     * @return
     */
    public List<OaNoticeDto> listFirmDateByType(String type,String enLogin){
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
        int number=8;
        //2021-01-04HNZ 企业动态8条显示 制度6条显示
        if (type!=""&&type.contains("制度")){
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
            //判断发布是否在发布范围
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
                //判断发布是否在发布范围
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
     * 首页 通过部门板块 公告信息 总共6条
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
        map.put("noticeType","部门空间");
        List<OaNotice> oaNotices = oaNoticeMapper.selectAll(map);
        List<OaNoticeDto> list = Lists.newArrayList();
        for (OaNotice dto:oaNotices){
            //判断发布是否在发布范围
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
                //判断发布是否在发布范围
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
        result.put("partyOfficeList",listDateByDeptName(59,enLogin));//公司办公室
        result.put("partyBuildingList",listDateByDeptName(100,enLogin));//保密办公室
        result.put("newsBusinessList",listDateByDeptName(72,enLogin));//党群工作部
        result.put("newsMessageList",listDateByDeptName(48,enLogin));//经营发展部
        result.put("newsSecrecyList",listDateByDeptName(11,enLogin));//信息化建设与管理部
        result.put("newsFinanceList",listDateByDeptName(18,enLogin));//财务金融部
        result.put("newsPoliticsList",listDateByDeptName(38,enLogin));//人力资源部
        result.put("newsLogisticsList",listDateByDeptName(29,enLogin));//工程管理部
        result.put("newsTrainList",listDateByDeptName(9,enLogin));//纪检工作部、审计与风险管理部
        result.put("laborUnionList",listDateByDeptName(67,enLogin));//行政事务部
        result.put("laborScientificList",listDateByDeptName(101,enLogin));//科研与技术质量部
        //登录人所属部门所属设计单位
        HrDeptDto deptDto = hrDeptService.getModelById(hrEmployeeDto.getDeptId());
        if (deptDto.getDeptType().equals("设计")){
            result.put("designNoticeList",listDateByDeptName(deptDto.getId(),enLogin));//设计单位
        }

        return result;
    }



    //信息中心 公司新闻 文件简报 通知公告
    public PageInfo<Object> listPagedDataByType(Map<String,Object> params, String type, String enLogin, int pageNum, int pageSize) {
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(enLogin);
        //params.put("selfCreator",enLogin);//selfCreator  判断可见自己创建的
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

    //模糊查询
    public PageInfo<Object> fuzzySearch(Map<String,Object> params, String enLogin, int pageNum, int pageSize) {
        params.put("selfCreator", enLogin);//selfCreator  判断可见自己创建的
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
        allType.add("公司新闻");
        allType.add("文件简报");
        allType.add("通知公告");
        return allType;
    }

    public List<OaNoticeDto> getNewsPhoto(){
        Map params = new HashMap();
        params.put("deleted",false);
        params.put("noticeType","图片新闻");
        //取最新的 图片新闻前5个 有图片的新闻
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
     * 各个部门查询 新闻公告-更多
     * @param params
     * @param enLogin 当前登录人
     * @param deptName 查询部门
     * @param pageNum 分页
     * @param pageSize 分页大小
     * @return
     */
    public  PageInfo<Object> listPagedDataByUserLogin(Map<String,Object> params, String enLogin, String deptName , int pageNum, int pageSize) {
        HrEmployeeDto hrEmployeeSys = selectEmployeeService.selectByUserLogin(enLogin);
        params.put("deptName",deptName);
        if (!deptName.equals(hrEmployeeSys.getDeptName())){
        params.put("publish",true);
        }
        params.put("deleted", false);
        params.put("noticeType", "部门空间");



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
     * 各个部门查询 新闻公告-类型
     * @param params
     * @param enLogin 当前登录嗯
     * @param deptName 查询部门
     * @param pageNum 分页
     * @param pageSize 分页大小
     * @return
     */
    public  List<Map> listPagedDataOrderType(Map<String,Object> params, String enLogin, String deptName, int pageNum, int pageSize) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(enLogin);
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE, deptName + "_电子公告");
        List<Map> list=Lists.newArrayList();
        params.put("deptName",deptName);
        if (!deptName.equals(hrEmployeeDto.getDeptName())){
            params.put("publish",true);
        }
        params.put("deleted", false);
        params.put("noticeLevels", "部门空间");

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
            users.add("蔡雪溦(sunflowermore)");
        }

        return users.toString();
    }
}
