package com.cmcu.mcc.sys.service;

import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.service.CommonAttachService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.sys.dao.SysSoftwareMapper;
import com.cmcu.mcc.sys.dto.SysSoftwareDto;
import com.cmcu.mcc.sys.entity.SysSoftware;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hnz
 * @date 2019/11/5
 */
@Service
public class SysSoftwareService {
    @Resource
    SysSoftwareMapper sysSoftwareMapper;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    CommonAttachService commonAttachService;

    public void remove(int id,String userLogin){
        SysSoftware item = sysSoftwareMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        item.setDeleted(true);
        item.setGmtModified(new Date());
        sysSoftwareMapper.updateByPrimaryKey(item);
    }

   public SysSoftware getNewModel(String userLogin) {
       SysSoftware item = new SysSoftware();
       HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
       Map parms = Maps.newHashMap();
       parms.put("deleted", false);
       int seq = (int) PageHelper.count(() -> sysSoftwareMapper.selectAll(parms)) + 1;
       item.setCreator(userLogin);
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setGmtCreate(new Date());
       item.setGmtModified(new Date());
       item.setDeleted(false);
       item.setSeq(seq);
       ModelUtil.setNotNullFields(item);
       return item;
   }

   public SysSoftwareDto getModelById(int id){
        return getDto(sysSoftwareMapper.selectByPrimaryKey(id));
   }

   public SysSoftwareDto getDto(SysSoftware sysSoftware){
       SysSoftwareDto dto = SysSoftwareDto.adapt(sysSoftware);
       if (sysSoftware.getAttachId()!=0){
           CommonAttach sysAttachDto = commonAttachService.getModelById(sysSoftware.getAttachId());
           if(sysAttachDto!=null)
            dto.setSizeName(sysAttachDto.getSizeName());
       }

      return dto;
   }
   public void update(SysSoftwareDto model){
       ModelUtil.setNotNullFields(model);
       BeanValidator.check(model);
        if(model.getId()!=null&&model.getId()>0) {
            SysSoftware item = sysSoftwareMapper.selectByPrimaryKey(model.getId());
            item.setSoftwareName(model.getSoftwareName());
            item.setSoftwareDesc(model.getSoftwareDesc());
            item.setAttachId(model.getAttachId());
            item.setRemark(model.getRemark());
            item.setGmtModified(new Date());
            item.setSeq(model.getSeq());
            sysSoftwareMapper.updateByPrimaryKey(item);
        }else{
            sysSoftwareMapper.insert(model);
        }
   }
    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, int pageNum, int pageSize) {
        params.put("deleted", false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> sysSoftwareMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            SysSoftware item = (SysSoftware) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
