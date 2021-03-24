package com.cmcu.mcc.oa.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaKnowledgeMapper;
import com.cmcu.mcc.oa.dto.OaKnowledgeDto;
import com.cmcu.mcc.oa.entity.OaKnowledge;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaKnowledgeService {
    @Resource
    OaKnowledgeMapper oaKnowledgeMapper;
    @Autowired
    SelectEmployeeService selectEmployeeService;

    public void remove(int id, String userLogin) {
        OaKnowledge item = oaKnowledgeMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");

        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaKnowledgeMapper.updateByPrimaryKey(item);
    }

    public void update(OaKnowledgeDto oaKnowledgeDto) {
        OaKnowledge item = oaKnowledgeMapper.selectByPrimaryKey(oaKnowledgeDto.getId());
        boolean updateTitle=oaKnowledgeDto.getNoticeTitle().equalsIgnoreCase(item.getNoticeTitle());


        item.setNoticeType(oaKnowledgeDto.getNoticeType());
        item.setNoticeTitle(oaKnowledgeDto.getNoticeTitle());
        item.setDeptName(oaKnowledgeDto.getDeptName());
        item.setPublishUserName(oaKnowledgeDto.getPublishUserName());
        item.setTop(oaKnowledgeDto.getTop());
        item.setPublish(oaKnowledgeDto.getPublish());
        item.setRemark(oaKnowledgeDto.getRemark());
        item.setNoticeContent(oaKnowledgeDto.getNoticeContent());
        item.setGmtModified(new Date());
        item.setPublishDeptName(oaKnowledgeDto.getPublishDeptName());
        item.setDeptId(oaKnowledgeDto.getDeptId());
        BeanValidator.check(item);
        oaKnowledgeMapper.updateByPrimaryKey(item);

        if(updateTitle){
         //   myActService.setVariable(item.getProcessInstanceId(),"description",item.getNoticeTitle());
        }
    }
    public OaKnowledgeDto getModelById(int id) {
        return getDto(oaKnowledgeMapper.selectByPrimaryKey(id));
    }
    public OaKnowledgeDto getDto(OaKnowledge item) {
        OaKnowledgeDto oaKnowledgeDto = OaKnowledgeDto.adapt(item);
        oaKnowledgeDto.setProcessName("已发布");
        return oaKnowledgeDto;
    }

    public int getNewModel(String userLogin) { //knowledge
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaKnowledge item=new OaKnowledge();

        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setNoticeType("知识库");
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(true);
        item.setBusinessKey(EdConst.OA_KNOWLEDGE+"_"+item.getId());
        ModelUtil.setNotNullFields(item);
        oaKnowledgeMapper.insert(item);
        return item.getId();
    }
    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaKnowledgeMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaKnowledge item = (OaKnowledge) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<OaKnowledge> selectAll() {
        Map map = new HashMap();
        map.put("deleted",false);
        List<OaKnowledge> oaNotices = oaKnowledgeMapper.selectAll(map);
        return oaNotices;
    }

}
