package com.cmcu.common.service;

import com.cmcu.common.dao.CommonEdQuestionMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonEdQuestion;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class CommonEdQuestionService {

    @Resource
    CommonEdQuestionMapper commonEdQuestionMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    GuavaCacheService guavaCacheService;

    public PageInfo<CommonEdQuestion> listPagedData(int pageNum, int pageSize, Map<String, Object> params) {
        PageInfo<CommonEdQuestion> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonEdQuestionMapper.selectAll(params));
        return pageInfo;
    }

    public List<CommonEdQuestion> selectAll(String tenetId, String majorName) {
        List<CommonEdQuestion> list = guavaCacheService.get(getCacheKey(tenetId, majorName), () -> {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("tenetId", tenetId);
            params.put("majorName", majorName);
            List<CommonEdQuestion> all = commonEdQuestionMapper.selectAll(params);
            return Optional.ofNullable(all);
        });
        return list;
    }

    public CommonEdQuestion getNewModel(String enLogin) {
        String tenetId = commonUserService.getTenetId(enLogin);
        Date now = new Date();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("creator", enLogin);
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonEdQuestionMapper.selectAll(params)) > 0) {
            CommonEdQuestion model = commonEdQuestionMapper.selectAll(params).get(0);
            model.setId(0);
            model.setGmtCreate(now);
            model.setGmtModified(now);
            model.setQuestionContent("");
            model.setQuestionNo("");
            model.setStandardDesc("");
            model.setStandardNo("");
            return model;
        }
        String questionLv = commonCodeService.selectDefaultCodeValue(tenetId, "校审意见类型").toString();
        CommonEdQuestion model = new CommonEdQuestion();
        model.setId(0);
        model.setQuestionLv(questionLv);
        model.setTenetId(tenetId);
        model.setCreator(enLogin);
        model.setGmtCreate(now);
        model.setGmtModified(now);
        model.setDeleted(false);
        ModelUtil.setNotNullFields(model);
        return model;
    }

    public CommonEdQuestion getModelById(int id){
        return commonEdQuestionMapper.selectByPrimaryKey(id);
    }

    public void remove(int id, String enLogin) {
        CommonEdQuestion item = commonEdQuestionMapper.selectByPrimaryKey(id);
        if (item != null && !item.getDeleted()) {
            Assert.state(enLogin.equalsIgnoreCase(item.getCreator()), "创建人" + item.getCreatorName() + "才可以删除!");
            item.setDeleted(true);
            item.setGmtModified(new Date());
            commonEdQuestionMapper.updateByPrimaryKey(item);
            guavaCacheService.invalidate(getCacheKey(item.getTenetId(), item.getMajorName()));
        }
    }

    public void save(CommonEdQuestion item) {
        if (item.getId() > 0) {
            CommonEdQuestion pre = commonEdQuestionMapper.selectByPrimaryKey(item.getId());
            pre.setGmtModified(new Date());
            pre.setQuestionContent(item.getQuestionContent());
            pre.setQuestionLv(item.getQuestionLv());
            pre.setQuestionNo(item.getQuestionNo());
            pre.setStandardNo(item.getStandardNo());
            pre.setStandardDesc(item.getStandardDesc());
            pre.setKeyWord(item.getKeyWord());
            pre.setMajorName(item.getMajorName());
            pre.setQuestionScope(item.getQuestionScope());
            pre.setQuestionCategory(item.getQuestionCategory());
            BeanValidator.check(pre);
            commonEdQuestionMapper.updateByPrimaryKey(pre);
        } else {
            item.setCreatorName(commonUserService.getCnNames(item.getCreator()));
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            ModelUtil.setNotNullFields(item);
            BeanValidator.check(item);
            commonEdQuestionMapper.insert(item);
        }
        guavaCacheService.invalidate(getCacheKey(item.getTenetId(), item.getMajorName()));
    }

    public void insert(String majorName, String questionLv, String questionContent, String enLogin) {
        UserDto user = commonUserService.selectByEnLogin(enLogin);
        CommonEdQuestion item = new CommonEdQuestion();
        item.setCreatorName(user.getCnName());
        item.setCreator(enLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setQuestionLv(questionLv);
        item.setQuestionContent(questionContent);
        item.setMajorName(majorName);
        ModelUtil.setNotNullFields(item);
        commonEdQuestionMapper.insert(item);
        guavaCacheService.invalidate(getCacheKey(item.getTenetId(), item.getMajorName()));
    }

    @Transactional
    public void upload(List<Map> data,String enLogin) throws ParseException {

        Assert.state(data.size() > 0,"数据为空!");
        Date now = new Date();
        UserDto user = commonUserService.selectByEnLogin(enLogin);
        Assert.state(data.get(0).size()>=12,"每行数据应为12列(请严格按照模板填写数据)!");

        List<CommonCodeDto> questionLvList = commonCodeService.listDataByCatalog(user.getTenetId(), "校审意见类型");
        List<String> lvWords = Arrays.asList("轻微", "严重", "安全", "强", "一般");

        if (data.size() > 0) {

            for (int i = 1; i < data.size(); i++) {
                Map map = data.get(i);
                String majorName = map.get(1).toString();
                String questionScope = map.get(2).toString();
                String questionCategory = map.get(3).toString();
                String questionLv = map.get(4).toString();
                String questionContent = map.get(5).toString();
                String keyWord = map.get(6).toString();
                String creatorName = map.get(7).toString();
                String createTime = map.get(8).toString();
                String questionNo = map.get(9).toString();
                String standardNo = map.get(10).toString();
                String standardDesc = map.get(11).toString();


                CommonEdQuestion item = new CommonEdQuestion();
                if (StringUtils.isNotEmpty(majorName) && StringUtils.isNotEmpty(questionContent)) {

                    Map params = Maps.newHashMap();
                    params.put("majorName", majorName);
                    params.put("questionContent", questionContent);
                    params.put("deleted", false);
                    if (PageHelper.count(() -> commonEdQuestionMapper.selectAll(params)) > 0) {
                        item = commonEdQuestionMapper.selectAll(params).get(0);
                    }

                    for (String lvWord : lvWords) {
                        if (questionLv.contains(lvWord)) {
                            if (questionLvList.stream().anyMatch(p -> p.getCode().contains(lvWord))) {
                                questionLv = questionLvList.stream().filter(p -> p.getCode().contains(lvWord)).findFirst().get().getCode();
                                break;
                            }
                        }
                    }

                    item.setTenetId(user.getTenetId());
                    item.setMajorName(majorName);
                    item.setQuestionScope(questionScope);
                    item.setQuestionCategory(questionCategory);
                    item.setQuestionLv(questionLv);
                    item.setQuestionContent(questionContent);
                    item.setKeyWord(keyWord);
                    item.setQuestionNo(questionNo);
                    item.setStandardNo(standardNo);
                    item.setStandardDesc(standardDesc);
                    item.setDeleted(false);
                    if(StringUtils.isEmpty(questionScope)) item.setQuestionScope(item.getQuestionCategory());
                    if(StringUtils.isEmpty(questionCategory)) item.setQuestionCategory(item.getQuestionScope());

                    if (item.getId() == null || item.getId() == 0) {
                        if (StringUtils.isEmpty(creatorName)) {
                            item.setCreator(enLogin);
                            item.setCreatorName(user.getCnName());
                        } else {
                            item.setCreator(creatorName);
                            item.setCreator(enLogin);
                        }

                        if (StringUtils.isEmpty(createTime)) {
                            item.setGmtModified(now);
                            item.setGmtCreate(now);
                        } else {
                            Date time = DateUtils.parseDate(createTime, "yyyy-MM-dd");
                            item.setGmtCreate(time);
                            item.setGmtModified(time);
                        }
                        ModelUtil.setNotNullFields(item);
                        commonEdQuestionMapper.insert(item);
                    } else {
                        commonEdQuestionMapper.updateByPrimaryKey(item);
                    }
                }
            }
        }
    }


    public List<Map> listData(String tenetId){
        List<Map> list=Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("tenetId", tenetId);
        List<CommonEdQuestion> all = commonEdQuestionMapper.selectAll(params);
        for(CommonEdQuestion item:all){
            LinkedHashMap map=Maps.newLinkedHashMap();
            map.put("专业",item.getMajorName());
            map.put("一级类别",item.getQuestionScope());
            map.put("二级类别",item.getQuestionCategory());
            map.put("意见类型",item.getQuestionLv());
            map.put("意见内容",item.getQuestionContent());
            map.put("关键字",item.getKeyWord());
            map.put("创建人",item.getCreatorName());
            map.put("创建时间", DateFormatUtils.format(item.getGmtCreate(),"yyyy-MM-dd"));
            map.put("条文号",item.getQuestionNo());
            map.put("规范号",item.getStandardNo());
            map.put("规范名称",item.getStandardDesc());
            list.add(map);
        }
        return list;
    }




    private String getCacheKey(String tenetId, String majorName) {
        return "commonEdQuestion_" + tenetId + "_" + majorName;
    }

}
