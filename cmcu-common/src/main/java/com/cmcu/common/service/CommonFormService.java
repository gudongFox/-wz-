package com.cmcu.common.service;

import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dao.CommonFormMapper;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonFormService {
    @Resource
    CommonFormMapper commonFormMapper;
    @Resource
    CommonUserService commonUserService;
    @Resource
    CommonFormDetailMapper commonFormDetailMapper;

    public void remove(int id, String enLogin) {
        CommonForm item = commonFormMapper.selectByPrimaryKey(id);
        if (item.getCreator().equals(enLogin)||enLogin.equals("2863")) {
            item.setDeleted(true);
            item.setGmtModified(new Date());
            commonFormMapper.updateByPrimaryKey(item);
        } else {
            Assert.state(false, "您不是创建者：" + item.getCreatorName());
        }
    }

    /**
     * 用户新增数据 返回初始化数据
     *
     * @param enLogin
     * @return
     */
    public int getNewModel(String enLogin) {
        CommonForm item = new CommonForm();
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        item.setCreator(userDto.getEnLogin());
        item.setCreatorName(userDto.getCnName());
        item.setTenetId(userDto.getTenetId());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setPublished(true);
        item.setFormVersion(1);
        item.setFormIcon("icon-note");
        ModelUtil.setNotNullFields(item);
        commonFormMapper.insert(item);
        return item.getId();
    }

    public CommonForm getModelById(int id) {
        return commonFormMapper.selectByPrimaryKey(id);
    }


    /**
     * id==0 or id==null 新增
     * id>0  更新
     * @param commonForm
     */
    public void update(CommonForm commonForm) {
        if (commonForm.getId() == null || commonForm.getId() == 0) {
            commonFormMapper.insert(commonForm);
        } else {
            commonForm.setGmtModified(new Date());
            commonFormMapper.updateByPrimaryKey(commonForm);
        }
    }

    public PageInfo<CommonForm> listPagedData(Map<String, Object> params, String tenetId, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("tenetId", tenetId);
        PageInfo<CommonForm> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonFormMapper.selectAll(params));
        return pageInfo;
    }

    /**
     * formDetail子表删除
     * 判断当前操作人为主表创建者 可以删除
     *
     * @param id
     * @param enLogin
     */
    public void removeDetail(int id, String enLogin) {
        CommonFormDetail detail = commonFormDetailMapper.selectByPrimaryKey(id);
        CommonForm item = commonFormMapper.selectByPrimaryKey(detail.getFormId());


        //2863 的账号可以删除
        if (item.getCreator().equals(enLogin)||enLogin.equals("2863")) {
            detail.setDeleted(true);
            detail.setGmtModified(new Date());
            commonFormDetailMapper.updateByPrimaryKey(detail);
        } else {
            Assert.state(false, "您不是创建者：" + item.getCreatorName());
        }
    }

    /**
     * 详情 新建默认数据
     *
     * @param formId
     * @return
     */
    public CommonFormDetail getNewModelDetail(int formId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("formId", formId);
        int seq = (int) PageHelper.count(() -> commonFormDetailMapper.selectAll(params)) + 1;
        CommonFormDetail detail = new CommonFormDetail();
        detail.setFormId(formId);
        detail.setDetailSeq(seq);
        detail.setListSeq(seq);
        detail.setInputSize(6);
        detail.setEditable(false);
        detail.setMaxLength(0);
        detail.setRequired(false);
        detail.setDetailHidden(false);
        detail.setVariable(false);
        detail.setDeleted(false);
        detail.setGmtCreate(new Date());
        detail.setGmtModified(new Date());
        detail.setListHidden(true);
        detail.setDetailHidden(true);
        detail.setEditableTag("creator");
        detail.setMultiple(false);
        detail.setInputType("text");
        detail.setInputConfig("{}");
        detail.setId(0);
        return detail;
    }


    public CommonFormDetail getModelDetailById(int id) {
        return commonFormDetailMapper.selectByPrimaryKey(id);
    }

    public void updateDetail(CommonFormDetail commonFormDetail) {
        if (commonFormDetail.getId() == null || commonFormDetail.getId() == 0) {
            ModelUtil.setNotNullFields(commonFormDetail);
            commonFormDetailMapper.insert(commonFormDetail);
        } else {
            commonFormDetail.setGmtModified(new Date());
            commonFormDetailMapper.updateByPrimaryKey(commonFormDetail);
        }
    }

    public List<CommonFormDetail> listDetail(int formId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("formId", formId);
        List<CommonFormDetail> list = commonFormDetailMapper.selectAll(params);
        if (list.stream().noneMatch(p -> p.getInputCode().equalsIgnoreCase("creator"))) {
            basicInsert(formId, "备注", "remark", 50, "{}");
            basicInsert(formId, "创建人", "creator", 110, "{}");
            basicInsert(formId, "创建人", "creatorName", 111, "{\"headStyle\":{\"width\":\"80px\"}}");
            basicInsert(formId, "创建时间", "gmtCreate", 112, "{\"headStyle\":{\"width\":\"160px\"}}");
            basicInsert(formId, "修改时间", "gmtModified", 113, "{\"headStyle\":{\"width\":\"160px\"}}");
            list = commonFormDetailMapper.selectAll(params);
        }
        return list;
    }

    public void basicInsert(int formId, String inputText, String inputCode, int seq, String inputConfig) {

        CommonFormDetail detail = new CommonFormDetail();
        detail.setFormId(formId);
        detail.setGroupName("基础信息");
        detail.setInputType("text");
        detail.setInputCode(inputCode);
        detail.setInputText(inputText);
        detail.setInputSize(6);
        detail.setInputConfig(inputConfig);
        detail.setDetailSeq(seq);
        detail.setListSeq(seq);
        detail.setEditable(false);
        detail.setDisabled(false);
        detail.setMaxLength(100);
        detail.setRequired(false);
        detail.setDetailHidden(false);
        detail.setVariable(false);
        detail.setDeleted(false);
        detail.setGmtCreate(new Date());
        detail.setGmtModified(new Date());
        detail.setListHidden(false);
        detail.setDetailHidden(false);
        detail.setEditableTag("creator");
        detail.setMultiple(false);
        ModelUtil.setNotNullFields(detail);
        commonFormDetailMapper.insert(detail);
    }

    public CommonForm getModel(String tenetId, String formKey, int version) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("formKey", formKey);
        if(PageHelper.count(()->commonFormMapper.selectAll(params))==0) {
            Date now = new Date();
            CommonForm commonForm = new CommonForm();
            commonForm.setTenetId(tenetId);
            commonForm.setFormVersion(1);
            commonForm.setFormKey(formKey);
            commonForm.setFormIcon("icon-note");
            commonForm.setFormCategory("设计管理");
            commonForm.setFormDesc(formKey);
            commonForm.setOtherTplConfig("{\"showBusinessFile\":true}");
            commonForm.setPublished(true);
            commonForm.setDeleted(false);
            commonForm.setCreatorName("罗冬");
            commonForm.setCreator("luodong");
            commonForm.setGmtCreate(now);
            commonForm.setGmtModified(now);
            ModelUtil.setNotNullFields(commonForm);
            commonFormMapper.insert(commonForm);
        }

        if (version > 0) {
            params.put("version", version);
        }

        List<CommonForm> commonForms = commonFormMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonForm::getFormVersion).reversed()).collect(Collectors.toList());

        if(commonForms.stream().anyMatch(p->p.getTenetId().equalsIgnoreCase(tenetId))){
            commonForms=commonForms.stream().filter(p->p.getTenetId().equalsIgnoreCase(tenetId)).collect(Collectors.toList());
        }

        if (commonForms.size() == 0) return null;

        if (version > 0 && commonForms.stream().anyMatch(p -> p.getFormVersion().equals(version)))
            return commonFormMapper.selectAll(params).stream().filter(p -> p.getFormVersion().equals(version)).findFirst().get();

        if (commonForms.stream().anyMatch(CommonForm::getPublished))
            return commonFormMapper.selectAll(params).stream().filter(CommonForm::getPublished).findFirst().get();

        return commonForms.get(0);
    }
}
