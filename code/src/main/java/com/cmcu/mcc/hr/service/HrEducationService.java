package com.cmcu.mcc.hr.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.hr.dao.HrEducationMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEducationDto;
import com.cmcu.mcc.hr.entity.HrEducation;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.service.ActService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class HrEducationService {

    @Resource
    ActService actService;


    @Resource
    HrEducationMapper hrEducationMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    CommonCodeService commonCodeService;
    @Resource
    EdFileService edFileService;




    private HrEducationDto getDto(HrEducation item) {

        if(item==null) return null;

        HrEducationDto dto = HrEducationDto.adapt(item);

        HrEmployee hrEmployee = hrEmployeeMapper.selectByUserLoginOrNo(item.getUserLogin());
        dto.setUserName(hrEmployee.getUserName());

        ProcessInstanceDto processInstanceDto = actService.getProcessInstanceDto(dto.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());

        if (StringUtils.isNotEmpty(dto.getProcessInstanceId()) && StringUtils.isEmpty(dto.getProcessName())) {
            dto.setProcessName("流程已结束");
            if (!dto.getProcessEnd()) {
                dto.setProcessEnd(true);
                hrEducationMapper.updateByPrimaryKey(dto);
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        Date now = new Date();

        HrEducation item = new HrEducation();
        item.setUserLogin(userLogin);
        item.setEducationType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"教育类型").toString());
        item.setEducationName(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"学历").toString());
        item.setEducationYear(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"学制").toString());
        item.setEducationDegree(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"学位").toString());

        item.setGmtModified(now);
        item.setGmtCreate(now);

        ModelUtil.setNotNullFields(item);
        hrEducationMapper.insert(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", "luodong");
        variables.put("hrChargeMan", "hnz");
        variables.put("passed", true);
        String processInstanceId = actService.startProcess(EdConst.HR_EDUCATION,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrEducationMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public void update(HrEducationDto item){


        HrEducation model=hrEducationMapper.selectByPrimaryKey(item.getId());
        model.setSchoolName(item.getSchoolName());
        model.setBeginTime(item.getBeginTime());
        model.setEndTime(item.getEndTime());
        model.setEducationType(item.getEducationType());
        model.setEducationName(item.getEducationName());
        model.setEducationYear(item.getEducationYear());
        model.setEducationDegree(item.getEducationDegree());
        model.setPrimarySpecialty(item.getPrimarySpecialty());
        model.setOtherSpecialty(item.getOtherSpecialty());


        model.setGmtModified(new Date());
        //BeanValidator.check(model);
        ModelUtil.setNotNullFields(model);
        hrEducationMapper.updateByPrimaryKey(model);
    }

    public void remove(int id) {
        HrEducation item = hrEducationMapper.selectByPrimaryKey(id);

        item.setGmtModified(new Date());
        hrEducationMapper.deleteByPrimaryKey(item.getId());
    }

    public HrEducationDto getModelById(int educationId) {
        return getDto(hrEducationMapper.selectByPrimaryKey(educationId));
    }


    public List<HrEducation> listDtoData(String userLogin) {
        Map map=new HashMap();
        map.put("userLogin",userLogin);
        List<HrEducation> educations =hrEducationMapper.selectAll(map);
        return educations;
    }
    public List<HrEducationDto> listData(String userLogin) {
        Map map=new HashMap();
        map.put("userLogin",userLogin);
        List<HrEducation> educations =hrEducationMapper.selectAll(map);
        List<HrEducationDto> list = Lists.newArrayList();
        educations.forEach(p -> list.add(getDto(p)));
        return list;

    }

    /**
     * 获取个人所有学历学位证书附件 （已完成流程）
     * @param userLogin
     * @return
     */
    public List<EdFileDto> getAllEdFile(String userLogin){
        List<EdFileDto> list=new ArrayList<>();
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        map.put("processEnd",true);
        List<HrEducation> hrEducationList =hrEducationMapper.selectAll(map);
        for (HrEducation dto:hrEducationList){
            String businessId="hrEducation_"+dto.getId();
            List<EdFileDto> edFileDtoList = edFileService.listData(businessId);
            if (edFileDtoList.size()>0){
                list.addAll(edFileDtoList);
            }
        }
        return list;
    }
}
