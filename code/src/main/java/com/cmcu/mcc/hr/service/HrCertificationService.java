package com.cmcu.mcc.hr.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrCertificationMapper;
import com.cmcu.mcc.hr.dto.HrCertificationDto;
import com.cmcu.mcc.hr.entity.HrCertification;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.service.ActService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class HrCertificationService {

    @Resource
    ActService actService;

    @Resource
    HrCertificationMapper hrCertificationMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;


    @Resource
    EdFileService edFileService;



    private HrCertificationDto getDto(HrCertification item) {
        HrCertificationDto dto = HrCertificationDto.adapt(item);
        ProcessInstanceDto processInstanceDto = actService.getProcessInstanceDto(dto.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());

        if (StringUtils.isNotEmpty(dto.getProcessInstanceId()) && StringUtils.isEmpty(dto.getProcessName())) {
            dto.setProcessName("流程已结束");
            if (!dto.getProcessEnd()) {
                dto.setProcessEnd(true);
                hrCertificationMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        Date now = new Date();

        HrCertification item = new HrCertification();
        HrEmployee hrEmployee = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployee.getUserName());
        item.setUserLogin(userLogin);
        item.setUserName(hrEmployee.getUserName());

        item.setGmtModified(now);
        item.setGmtCreate(now);

        ModelUtil.setNotNullFields(item);
        hrCertificationMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", "luodong");
        variables.put("hrChargeMan", "hnz");
        variables.put("passed", true);
        String processInstanceId = actService.startProcess(EdConst.HR_CERTIFICATION,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrCertificationMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public void update(HrCertificationDto item){

        HrCertification model=hrCertificationMapper.selectByPrimaryKey(item.getId());

        model.setUserLogin(item.getUserLogin());
        model.setCertificationName(item.getCertificationName());
        model.setCertificationNo(item.getCertificationNo());
        model.setMajorName(item.getMajorName());
        model.setRegisterNo(item.getRegisterNo());
        model.setSealNo(item.getSealNo());
        model.setQualifyNo(item.getQualifyNo());

        model.setIncompany(item.getIncompany());
        model.setIssuseDate(item.getIssuseDate());
        model.setValidDate(item.getValidDate());
        model.setInDate(item.getInDate());

        model.setIncompany(item.getIncompany());
        model.setRemark(item.getRemark());

        model.setGmtModified(new Date());
       // BeanValidator.check(model);
        ModelUtil.setNotNullFields(model);
        hrCertificationMapper.updateByPrimaryKey(model);
    }

    public void remove(int id) {
        HrCertification item = hrCertificationMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        hrCertificationMapper.updateByPrimaryKey(item);

    }

    public HrCertificationDto getModelById(int id) {
        return getDto(hrCertificationMapper.selectByPrimaryKey(id));
    }

    public List<HrCertification> listData(String userLogin) {
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        List<HrCertification> certifications =hrCertificationMapper.selectAll(map);
        List<HrCertification> list = Lists.newArrayList();
        certifications.forEach(p -> list.add(getDto(p)));
        return list;
    }

    /**
     * 获取个人所以执业资格附件 （已完成流程）
     * @param userLogin
     * @return
     */
    public List<EdFileDto> getAllEdFile(String userLogin){
        List<EdFileDto> list=new ArrayList<>();
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        map.put("processEnd",true);
        List<HrCertification> certifications =hrCertificationMapper.selectAll(map);
        for (HrCertification dto:certifications){
            String businessId="hrCertification_"+dto.getId();
            List<EdFileDto> edFileDtoList = edFileService.listData(businessId);
            if (edFileDtoList.size()>0){
                list.addAll(edFileDtoList);
            }
        }
        return list;
    }
}
