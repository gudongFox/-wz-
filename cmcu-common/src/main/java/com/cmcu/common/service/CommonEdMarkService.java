package com.cmcu.common.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.*;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.CommonEdMarkDto;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.*;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log
public class CommonEdMarkService {

    @Resource
    CommonEdMarkMapper commonEdMarkMapper;

    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonFileMapper commonFileMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Resource
    CommonUserService commonUserService;
    @Resource
    ProcessQueryService processQueryService;
    @Resource
    CommonBaseService commonBaseService;
    @Resource
    IHandleFormService handleFormService;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonFormService commonFormService;

    @Resource
    CommonEdMarkMoreMapper commonEdMarkMoreMapper;

    static String co_="co_";

    public CommonEdMarkDto getModelById(int id, String enLogin) {
        CommonEdMark commonEdMark = commonEdMarkMapper.selectByPrimaryKey(id);
        CustomSimpleProcessInstance customProcessInstance=processQueryService.getCustomSimpleProcessInstance("",commonEdMark.getBusinessKey(),enLogin);
        String majorName=getMajorName(commonEdMark.getBusinessKey(),commonEdMark.getCommonFileId());
        return  getDto(commonEdMark,majorName,customProcessInstance,enLogin);
    }

    public CommonEdMarkDto getNewModel(String businessKey, String enLogin) {
        Date now = new Date();
        UserDto user = commonUserService.selectByEnLogin(enLogin);
        CommonEdMark commonEdMark = new CommonEdMark();
        commonEdMark.setId(0);
        commonEdMark.setBusinessKey(businessKey);
        commonEdMark.setCommonFileId(0);
        commonEdMark.setCommonFileName("??????.doc");
        commonEdMark.setSourceFileId(0);
        commonEdMark.setCreator(enLogin);
        commonEdMark.setCreatorName(user.getCnName());
        commonEdMark.setGmtModified(now);
        commonEdMark.setGmtCreate(now);
        commonEdMark.setDeleted(false);
        commonEdMark.setQuestionLevel(commonCodeService.selectDefaultCodeValue(user.getTenetId(), "??????????????????").toString());
        commonEdMark.setQuestionColor("Black");
        CommonEdMark pre = commonEdMarkMapper.selectPre(businessKey, enLogin);
        if (pre != null) {
            commonEdMark.setCommonFileId(pre.getCommonFileId());
            commonEdMark.setCommonFileName(pre.getCommonFileName());
        }
        if (businessKey.contains(co_)) {
            commonEdMark.setRoleName("??????");
        } else {
            TplConfigDto tplConfigDto = handleFormService.getTplConfigDto("", businessKey, enLogin);
            Assert.state(tplConfigDto.getMarkRoleNames().size()>0,"??????????????????????????????!");
            Assert.state(StringUtils.isNotEmpty(tplConfigDto.getMarkAddRoleName()), "???????????????????????????????????????("+StringUtils.join(tplConfigDto.getMarkRoleNames(),",")+")");
            commonEdMark.setRoleName(tplConfigDto.getMarkAddRoleName());
        }

        String majorName=getMajorName(commonEdMark.getBusinessKey(),commonEdMark.getCommonFileId());
        CommonEdMarkDto dto = CommonEdMarkDto.adapt(commonEdMark);
        dto.setEditAble(true);
        dto.setMajorName(majorName);
        ModelUtil.setNotNullFields(dto);
        return dto;
    }

    public CommonEdMarkDto getNewModelByFileId(int fileId, String enLogin) {
        CommonFile commonFile = commonFileMapper.selectByPrimaryKey(fileId);
        CommonEdMarkDto commonEdMark = getNewModel(commonFile.getBusinessKey(), enLogin);
        commonEdMark.setCommonFileId(commonFile.getId());
        commonEdMark.setCommonFileName(commonFile.getFileName());
        String majorName = getMajorName(commonEdMark.getBusinessKey(), commonEdMark.getCommonFileId());
        commonEdMark.setMajorName(majorName);
        return commonEdMark;
    }

    public void setLocation(int id,String drawNo,String drawName,String cloudLocation,String enLogin) {
        CommonEdMark item = commonEdMarkMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(enLogin),"?????????????????????"+item.getCreatorName());
        item.setCloudLocation(cloudLocation);
        item.setDrawNo(drawNo);
        item.setDrawName(drawName);
        item.setGmtModified(new Date());
        item.setDeleted(false);
        BeanValidator.check(item);
        commonEdMarkMapper.updateByPrimaryKey(item);
    }

    public void setColor(int id,String questionColor,String enLogin){
        CommonEdMark item = commonEdMarkMapper.selectByPrimaryKey(id);
        if(!item.getQuestionColor().equalsIgnoreCase(questionColor)) {
            Assert.state(item.getCreator().equalsIgnoreCase(enLogin), "?????????????????????" + item.getCreatorName());
            item.setQuestionColor(questionColor);
            item.setGmtModified(new Date());
            commonEdMarkMapper.updateByPrimaryKey(item);
        }
    }

    public void setPicAttachId(int id,int picAttachId,String enLogin){
        CommonEdMark item = commonEdMarkMapper.selectByPrimaryKey(id);
        item.setPicAttachId(picAttachId);
        item.setGmtModified(new Date());
        commonEdMarkMapper.updateByPrimaryKey(item);
    }

    public int setAnswer(int id,String questionAnswer,String enLogin){
        CommonEdMark item = commonEdMarkMapper.selectByPrimaryKey(id);
        Assert.state(StringUtils.isEmpty(item.getQuestionAnswer())||enLogin.equalsIgnoreCase(item.getAnswer()),"?????????????????????"+item.getAnswerName()+"??????");
        item.setQuestionAnswer(questionAnswer);
        item.setAnswerTime(new Date());
        item.setAnswer(enLogin);
        item.setAnswerName(commonUserService.getCnNames(enLogin));
        commonEdMarkMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public void remove(int id,String enLogin){
        CommonEdMarkDto item = getModelById(id,enLogin);
        Assert.state(item.isEditAble(),"?????????????????????????????????!");
        item.setDeleted(true);
        item.setGmtModified(new Date());
        commonEdMarkMapper.updateByPrimaryKey(item);
        commonBaseService.clearCache(item.getBusinessKey(),0);
    }

    public int updateMark(int id, int fileId, String drawNo, String drawName, String questionLevel, String questionContent, String enLogin) {
        CommonEdMark item = commonEdMarkMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(enLogin), "?????????????????????" + item.getCreatorName());
        item.setDrawName(drawName);
        item.setDrawNo(drawNo);
        item.setQuestionLevel(questionLevel);
        item.setQuestionContent(questionContent);
        item.setGmtModified(new Date());
        item.setCommonFileId(fileId);
        item.setCommonFileName(commonFileMapper.selectByPrimaryKey(fileId).getFileName());
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        commonEdMarkMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public int insertMark(int fileId, String drawNo, String drawName, String questionLevel, String questionContent, String enLogin) {
        CommonEdMark item =getNewModelByFileId(fileId,enLogin);
        item.setDrawNo(drawNo);
        item.setDrawName(drawName);
        item.setQuestionLevel(questionLevel);
        item.setQuestionContent(questionContent);
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        commonEdMarkMapper.insert(item);
        commonBaseService.clearFileCache(item.getBusinessKey());
        return item.getId();
    }


    public long getCount(String businessKey, Boolean onlyCloud,Boolean onlyAnswered) {
        if(StringUtils.isEmpty(businessKey)) return 0;
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("onlyCloud", onlyCloud);
        params.put("onlyAnswered", onlyAnswered);
        params.put("deleted", false);
        return PageHelper.count(()->commonEdMarkMapper.selectAll(params));
    }



    /**
     * ?????????????????????,??????????????????
     * @param businessKey
     * @param onlyCloud
     * @param enLogin
     * @return
     */
    public List<CommonEdMarkDto> listData(String businessKey, Boolean onlyCloud,String enLogin) {
        if(StringUtils.isEmpty(businessKey)) return Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("onlyCloud", onlyCloud);
        params.put("deleted", false);
        List<CommonEdMark> all = commonEdMarkMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonEdMark::getGmtModified)).collect(Collectors.toList());
        return getDtoList(all,businessKey,enLogin);
    }



    public List<CommonEdMarkDto> listDataByFileId(int fileId, boolean onlyCloud,String enLogin) {

        List<CommonEdMarkDto> list = Lists.newArrayList();
        CommonFile commonFile = commonFileMapper.selectByPrimaryKey(fileId);
        if(commonFile!=null) {
            Map params = Maps.newHashMap();
            params.put("commonFileId", fileId);
            params.put("onlyCloud", onlyCloud);
            params.put("deleted", false);
            list.addAll(getDtoList(commonEdMarkMapper.selectAll(params), commonFile.getBusinessKey(), enLogin));
            int targetId = getTargetId(fileId);
            if (targetId != fileId) {
                CommonFile targetFile = commonFileMapper.selectByPrimaryKey(targetId);
                if(!targetFile.getBusinessKey().startsWith("co_")) {
                    params.put("commonFileId", targetId);
                    list.addAll(getDtoList(commonEdMarkMapper.selectAll(params), targetFile.getBusinessKey(), enLogin));
                }
            }
        }
        return list;
    }

    public void save(CommonEdMarkDto commonMark){
        String enLogin=commonMark.getEnLogin();
        Assert.state(StringUtils.isNotEmpty(enLogin),"?????????????????????!");

        ModelUtil.setNotNullFields(commonMark);
        BeanValidator.check(commonMark);
        if(commonMark.getId()==0){
            if(commonMark.getCommonFileId()==0){
                commonMark.setCommonFileName("???????????????");
            }else {
                CommonFile commonFile = commonFileMapper.selectByPrimaryKey(commonMark.getCommonFileId());
                commonMark.setCommonFileName(commonFile.getFileName());
            }
           commonEdMarkMapper.insert(commonMark);
        }else{

            CommonEdMarkDto pre=getModelById(commonMark.getId(),enLogin);

            if(pre.isAnswerAble()){

                pre.setAnswer(enLogin);
                pre.setAnswerName(commonUserService.getCnNames(enLogin));
                pre.setQuestionAnswer(commonMark.getQuestionAnswer());
                pre.setAnswerTime(new Date());
                commonEdMarkMapper.updateByPrimaryKey(pre);

            }else if(pre.isEditAble()){
                pre.setQuestionLevel(commonMark.getQuestionLevel());
                pre.setQuestionContent(commonMark.getQuestionContent());
                pre.setQuestionColor(commonMark.getQuestionColor());
                pre.setDrawNo(commonMark.getDrawNo());
                pre.setDrawName(commonMark.getDrawName());
                pre.setCommonFileId(commonMark.getCommonFileId());
                if(commonMark.getCommonFileId()==0){
                    pre.setCommonFileName("???????????????");
                }else {
                    CommonFile commonFile = commonFileMapper.selectByPrimaryKey(commonMark.getCommonFileId());
                    pre.setCommonFileName(commonFile.getFileName());
                }
                pre.setGmtModified(new Date());
                commonEdMarkMapper.updateByPrimaryKey(pre);
            }


        }

    }

    //????????????????????????
    public List<CommonEdMark> getHaveAnswer(String businessKey,boolean onlyCloud){
        Map params=Maps.newHashMap();
        params.put("businessKey",businessKey);
        params.put("onlyCloud",onlyCloud);
        params.put("deleted",false);
        List<CommonEdMark> all= commonEdMarkMapper.selectAll(params);
        List<CommonEdMark> list = new ArrayList<>();
        for(CommonEdMark commonEdMark : all){
            if(!Strings.isNullOrEmpty(commonEdMark.getAnswer())){
                list.add(commonEdMark);
            }
        }
        return list;
    }

    private String getMajorName(String businessKey,int fileId) {
        try {
            if (businessKey.startsWith(co_)) {
                if (fileId > 0) {
                    CommonFile commonFile = commonFileMapper.selectByPrimaryKey(fileId);
                    CommonDir commonDir = commonDirMapper.selectByPrimaryKey(commonFile.getDirId());
                    return commonDir.getMajorName();
                }
            } else {
                CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
                Map map = JsonMapper.string2Map(commonFormData.getFormData());
                return map.getOrDefault("majorName", "").toString();
            }
        } catch (Exception ex) {
            log.warning(ex.getMessage());
        }
        return "";
    }

    private CommonEdMarkDto getDto(CommonEdMark commonEdMark,String majorName,CustomSimpleProcessInstance customProcessInstance,String enLogin) {
        CommonEdMarkDto dto = CommonEdMarkDto.adapt(commonEdMark);
        Map params = Maps.newHashMap();
        params.put("markId", dto.getId());
        params.put("deleted", false);
        dto.setMoreList(commonEdMarkMoreMapper.selectAll(params));
        dto.setMajorName(majorName);
        if (StringUtils.isEmpty(dto.getCommonFileName()) || dto.getCommonFileId() == 0) {
            commonEdMark.setCommonFileName("???????????????");
        }
        if (dto.getMoreList().size() == 0 && StringUtils.isNotEmpty(enLogin)) {
            if (customProcessInstance == null || !customProcessInstance.isFinished()) {
                dto.setProcessAble(true);
            }
            if(customProcessInstance!=null&&customProcessInstance.isFirstTask()){
                dto.setAnswerAble(true);
            }else {
                //???????????????????????????????????????
                if (commonEdMark.getCreator().equalsIgnoreCase(enLogin) && StringUtils.isEmpty(commonEdMark.getQuestionAnswer())) {
                    dto.setEditAble(customProcessInstance == null || StringUtils.isNotEmpty(customProcessInstance.getMyRunningTaskName()));
                }
                if (!dto.isEditAble()) {
                    if (enLogin.equalsIgnoreCase(commonEdMark.getAnswer()) || StringUtils.isEmpty(commonEdMark.getQuestionAnswer())) {
                        dto.setAnswerAble(true);
                        if (!commonEdMark.getBusinessKey().startsWith(co_)) {
                            dto.setAnswerAble(false);
                            if (customProcessInstance != null && !customProcessInstance.isFinished()) {
                                dto.setAnswerAble(true);
                            }
                        }
                    }
                }
            }
        }
        ModelUtil.setNotNullFields(dto);
        return dto;
    }

    private List<CommonEdMarkDto> getDtoList(List<CommonEdMark> all,String businessKey,String enLogin){
        List<CommonEdMarkDto> list = Lists.newArrayList();
        CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance("", businessKey, enLogin);
        if (businessKey.startsWith(co_)) {
            for(Integer fileId :all.stream().map(CommonEdMark::getCommonFileId).distinct().collect(Collectors.toList())) {
                String majorName = getMajorName(businessKey, fileId);
                all.stream().filter(p -> p.getCommonFileId().equals(fileId)).forEach(p -> list.add(getDto(p, majorName, customProcessInstance, enLogin)));
            }
        } else {
            String majorName = getMajorName(businessKey, 0);
            all.forEach(p -> list.add(getDto(p, majorName, customProcessInstance, enLogin)));
        }
        return list;
    }

    public int  getTargetId(int commonFileId) {
        int sourceId = commonFileId;
        CommonFile commonFile = commonFileMapper.selectByPrimaryKey(commonFileId);
        if (commonFile != null && commonFile.getSourceId() == 0&&commonFile.getBusinessKey().startsWith("co_")) {
            while (true) {
                Map targetParams = Maps.newHashMap();
                targetParams.put("sourceId", sourceId);
                targetParams.put("deleted", false);
                if (PageHelper.count(() -> commonFileMapper.selectAll(targetParams)) == 0) {
                    return sourceId;
                }
                CommonFile latestFile = commonFileMapper.selectAll(targetParams).stream().sorted(Comparator.comparing(CommonFile::getGmtModified).reversed()).findFirst().get();
                sourceId = latestFile.getId();
            }
        }
        return sourceId;
    }

    public long getMarkCount(int fileId){
        Map params = Maps.newHashMap();
        params.put("commonFileId", fileId);
        params.put("deleted", false);
        long total=PageHelper.count(()->commonEdMarkMapper.selectAll(params));
        int targetFileId=getTargetId(fileId);
        if(targetFileId!=fileId){
            params.put("commonFileId", targetFileId);
            CommonFile latestFile =commonFileMapper.selectByPrimaryKey(targetFileId);
            if(!latestFile.getBusinessKey().startsWith("co_")) {
                total = PageHelper.count(() -> commonEdMarkMapper.selectAll(params));
            }
        }
        return total;
    }

    public List<Map> listMapData(String businessKey, String enLogin) {
        List<Map> list = new ArrayList<>();
        Map params = new HashMap();
        params.put("deleted", false);
        params.put("businessKey", businessKey);
        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        Map formData = com.common.util.JsonMapper.string2Map(commonFormData.getFormData());
        List<CommonEdMark> commonEdMarks = commonEdMarkMapper.selectAll(params);
        String projectName = formData.getOrDefault("projectName", "").toString();
        String stageName = formData.getOrDefault("stageName", "").toString();
        String majorName = formData.getOrDefault("majorName", "").toString();
        String stepName = formData.getOrDefault("stepName", "").toString();
        if (commonEdMarks.size() == 0) {
            CommonEdMark temp = new CommonEdMark();
            ModelUtil.setNotNullFields(temp);
            commonEdMarks.add(temp);
        }

        for (CommonEdMark edMark : commonEdMarks) {
            Map map = new LinkedHashMap();
            map.put("????????????", edMark.getRoleName());
            map.put("????????????", edMark.getCreatorName());
            map.put("????????????", edMark.getCommonFileName());
            map.put("????????????", StringUtils.isNotEmpty(edMark.getDrawName()) ? (edMark.getDrawName() + "|") : "" + edMark.getDrawNo());
            map.put("????????????", edMark.getQuestionLevel());
            map.put("????????????", edMark.getQuestionContent());
            map.put("????????????", edMark.getQuestionAnswer());
            map.put("?????????", edMark.getAnswerName());
            map.put("????????????", edMark.getAnswerTime() == null ? "" : DateFormatUtils.format(edMark.getAnswerTime(), "yyyy-MM-dd HH:mm"));
            map.put("????????????", edMark.getGmtCreate() == null ? "" : DateFormatUtils.format(edMark.getGmtCreate(), "yyyy-MM-dd HH:mm"));
            list.add(map);
        }

        return list;
    }

    @Transactional
    public void upload(List<Map> dataList, String businessKey, String enLogin) {
        Assert.state(dataList.size() > 0, "????????????????????????!");
        Assert.state(StringUtils.isNotEmpty(enLogin), "?????????????????????!");
        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        Assert.state(commonFormData != null, "??????????????????!");
        CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getFormKey(), commonFormData.getFormVersion());
        Assert.state(commonForm != null, "??????????????????!");
        TplConfigDto tplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
        Assert.state(tplConfigDto.getMarkRoleNames().size() > 0, "???????????????????????????!");
        CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance("", businessKey, enLogin);
        Assert.state(customProcessInstance.myRunningTaskNameList.size() > 0, "??????????????????????????????!");

        boolean replyAble = commonFormData.getCreator().equals(enLogin);
        List<String> taskNameList=customProcessInstance.getMyRunningTaskNameList().stream().map(p->StringUtils.split(p,"(")[0]).collect(Collectors.toList());
        List<String> markRoleNames = taskNameList.stream().filter(p -> tplConfigDto.getMarkRoleNames().stream().anyMatch(t -> t.equals(p))).collect(Collectors.toList());
        boolean addAble = markRoleNames.size() > 0;
        if (addAble) replyAble = false;

        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Date now = new Date();
        List<CommonEdMarkDto> list = Lists.newArrayList();
        int seq = 1;
        for (Map data : dataList) {
            String roleName = data.getOrDefault("????????????", "").toString().replace("??????","");
            String fileName = data.getOrDefault("????????????", "").toString();
            String drawNameNo = data.getOrDefault("????????????", "").toString();
            String questionLevel = data.getOrDefault("????????????", "").toString();
            String questionContent = data.getOrDefault("????????????", "").toString();
            String questionAnswer = data.getOrDefault("????????????", "").toString();
            String creatorName=data.getOrDefault("????????????","").toString();

            CommonEdMarkDto item = new CommonEdMarkDto();
            //????????????,????????????
            item.setId(seq);
            item.setRoleName(roleName);
            item.setCommonFileName(fileName);
            item.setDrawNo(drawNameNo);
            item.setQuestionLevel(questionLevel);
            item.setQuestionContent(questionContent);
            item.setQuestionAnswer(questionAnswer);
            item.setCreatorName(creatorName);
            ModelUtil.setNotNullFields(item);
            Assert.state(StringUtils.isNotEmpty(item.getQuestionContent()), "???" + item.getId() + "???????????????????????????!");
            Assert.state(tplConfigDto.getMarkRoleNames().contains(item.getRoleName()), "???" + item.getId() + "???????????????????????????");
            list.add(item);
            seq++;
        }

        List<CommonCodeDto> levelCodes = Lists.newArrayList();
        List<CommonFile> commonFiles = Lists.newArrayList();
        if (addAble) {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("businessKey", businessKey);
            commonFiles = commonFileMapper.selectAll(params);
            levelCodes = commonCodeService.listDataByCatalog(commonFormData.getTenetId(), "??????????????????");
            Assert.state(levelCodes.size() > 0, "????????????????????????????????????!");
        }


        for (CommonEdMark item : list) {
            if (replyAble) {
                Map params = Maps.newHashMap();
                params.put("roleName", item.getRoleName());
                params.put("questionContent", item.getQuestionContent());
                params.put("businessKey", item.getBusinessKey());
                params.put("deleted", false);
                params.put("commonFileName", item.getCommonFileName());
                params.put("questionLevel", item.getQuestionLevel());
                params.put("drawNo", item.getDrawNo());
                params.put("creatorName", item.getCreatorName());
                Assert.state(PageHelper.count(() -> commonEdMarkMapper.selectAll(params)) > 0, "???" + item.getId() + "????????????????????????");
                List<CommonEdMark> preList = commonEdMarkMapper.selectAll(params);
                CommonEdMark pre = preList.stream().sorted(Comparator.comparing(p -> p.getQuestionAnswer().length())).findFirst().get();
                if (StringUtils.isNotEmpty(item.getQuestionAnswer())) {
                    pre.setQuestionAnswer(item.getQuestionAnswer());
                    pre.setAnswer(userDto.getEnLogin());
                    pre.setAnswerName(userDto.getCnName());
                    pre.setAnswerTime(now);
                    commonEdMarkMapper.updateByPrimaryKey(pre);
                }
            } else {
                CommonEdMark pre = new CommonEdMark();
                pre.setCreator(userDto.getEnLogin());
                pre.setCreatorName(userDto.getCnName());
                pre.setRoleName(item.getRoleName());
                pre.setBusinessKey(businessKey);
                pre.setQuestionColor("Black");
                pre.setPicAttachId(0);
                pre.setDeleted(false);
                pre.setGmtModified(now);
                pre.setGmtCreate(now);
                pre.setCommonFileId(0);
                pre.setSourceFileId(0);
                if (StringUtils.isNotEmpty(item.getCommonFileName())) {
                    Optional<CommonFile> commonFile = commonFiles.stream().filter(p -> p.getFileName().equalsIgnoreCase(item.getCommonFileName())).findFirst();
                    if (commonFile.isPresent()) {
                        pre.setCommonFileId(commonFile.get().getId());
                        pre.setCommonFileName(commonFile.get().getFileName());
                    }
                }

                if (levelCodes.stream().anyMatch(p -> p.getCode().equalsIgnoreCase(item.getQuestionLevel()))) {
                    pre.setQuestionLevel(item.getQuestionLevel());
                } else {
                    pre.setQuestionLevel(levelCodes.stream().sorted(Comparator.comparing(CommonCode::getDefaulted).reversed()).findFirst().get().getCode());
                }
                pre.setQuestionContent(item.getQuestionContent());
                pre.setDrawNo(item.getDrawNo());
                ModelUtil.setNotNullFields(pre);
                commonEdMarkMapper.insert(pre);
            }
        }
    }

}
