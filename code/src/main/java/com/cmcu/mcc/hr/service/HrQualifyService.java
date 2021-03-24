package com.cmcu.mcc.hr.service;

import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.*;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrQualifyDto;
import com.cmcu.mcc.hr.entity.*;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class HrQualifyService {
    


    @Resource
    HrQualifyMapper hrQualifyMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    HrDeptMapper hrDeptMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    ActService actService;
    @Autowired
    GuavaCacheService guavaCacheService;
    @Autowired
    CommonCodeService commonCodeService;



    public HrQualify selectLatestByUserLogin(String userLogin){
        return hrQualifyMapper.selectLatestByUserLogin(userLogin);
    }

    private HrQualifyDto getDto(HrQualify item) {
        HrQualifyDto dto = HrQualifyDto.adapt(item);

        if(item.getProjectCharge()){
            dto.setIsProjectCharge("√");
        }else{
            dto.setIsProjectCharge("×");
        }
        if(item.getMajorCharge()){
            dto.setIsMajorCharge("√");
        }else{
            dto.setIsMajorCharge("×");
        }
        if(item.getProofread()){
            dto.setIsProofread("√");
        }else{
            dto.setIsProofread("×");
        }
        if(item.getAudit()){
            dto.setIsAudit("√");
        }else{
            dto.setIsAudit("×");
        }
        if(item.getDesign()){
            dto.setIsDesign("√");
        }else{
            dto.setIsDesign("×");
        }
        if(item.getApprove()){
            dto.setIsApprove("√");
        }else{
            dto.setIsApprove("×");
        }
        if(item.getChiefDesigner()){
            dto.setIsChiefDesigner("√");
        }else{
            dto.setIsChiefDesigner("×");
        }
        if(item.getProChief()){
            dto.setIsProChief("√");
        }else{
            dto.setIsProChief("×");
        }
        if(item.getCompanyApprove()){
            dto.setIsCompanyApprove("√");
        }else{
            dto.setIsCompanyApprove("×");
        }
        return dto;
    }

    public HrQualify getNewModel(String userLogin) {
        Date now = new Date();
        HrEmployeeDto hrEmployeeDto=hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        HrQualify item = new HrQualify();
        item.setUserLogin(userLogin);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setGmtModified(now);
        item.setGmtCreate(now);
        ModelUtil.setNotNullFields(item);
        hrQualifyMapper.insert(item);

        return item;
    }

    public void update(HrQualifyDto item){

        HrQualify model=hrQualifyMapper.selectByPrimaryKey(item.getId());

        model.setUserLogin(item.getUserLogin());
        model.setUserName(item.getUserName());
        model.setCheckYear(item.getCheckYear());
        model.setChiefDesigner(item.getChiefDesigner());

        model.setMajorName(item.getMajorName());
        model.setProChief(item.getProChief());
        //审定人
        model.setMajorCharge(item.getMajorCharge());
        model.setApprove(item.getApprove());
        //工程设计
        model.setProofread(item.getProofread());
        model.setDesign(item.getDesign());
        model.setAudit(item.getAudit());
        //工程咨询
        model.setConsultDesign(item.getConsultDesign());
        model.setConsultProofread(item.getConsultProofread());
        model.setConsultAudit(item.getConsultAudit());
        //工程承包
        model.setContractDesign(item.getContractDesign());
        model.setContractAudit(item.getContractAudit());
        model.setContractProofread(item.getContractProofread());
        //勘察
        model.setExploreEngineer(item.getExploreEngineer());
        model.setExploreAudit(item.getExploreAudit());
        model.setExplorePrincipal(item.getExplorePrincipal());
        //监理
        model.setSupervisorAudit(item.getSupervisorAudit());
        model.setSupervisorDesign(item.getSupervisorDesign());
        model.setSupervisorProofread(item.getSupervisorProofread());

        model.setProjectType(item.getProjectType());
        //如果不勾选专业 默认全部
        if ("".equals(item.getMajorName())){
            model.setMajorName("全部");
        }else {
            model.setMajorName(item.getMajorName());
        }

        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        BeanValidator.check(model);
        ModelUtil.setNotNullFields(model);
        hrQualifyMapper.updateByPrimaryKey(model);
    }

    public void remove(int id) {
        HrQualify item = hrQualifyMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        hrQualifyMapper.updateByPrimaryKey(item);
    }

    public void copy(int id) {
        HrQualify item = hrQualifyMapper.selectByPrimaryKey(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        hrQualifyMapper.insert(item);
    }


    public HrQualifyDto getModelById(int id) {
        return getDto(hrQualifyMapper.selectByPrimaryKey(id));
    }

    public List<HrQualifyDto> listData(String userLogin) {
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        List<HrQualify> qualifys =hrQualifyMapper.selectAll(map);
        List<HrQualifyDto> list = Lists.newArrayList();
        qualifys.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public PageInfo<Object> loadPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("checkYear", Integer.parseInt(DateFormatUtils.format(new Date(),"yyyy")));
        if(params.containsKey("parentDeptId")) {
            int parentDeptId=Integer.parseInt(params.get("parentDeptId").toString());
            List<Integer> deptIds = hrDeptService.listChild(parentDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(parentDeptId);
            params.put("deptIds", deptIds);
        }

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrQualifyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrQualify item = (HrQualify) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 五洲初始化数据 附上全部数据
     */

    public void initData(){
        int checkYear= Integer.parseInt(DateFormatUtils.format(new Date(),"yyyy"));
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("checkYear",checkYear);
       List<String> majorNames= commonCodeService.listDataByCatalog(MccConst.APP_CODE,"设计专业").stream().map(CommonCode::getName).collect(Collectors.toList());
        //系统用户
        List<HrEmployeeDto> employees=hrEmployeeMapper.selectAll(params);
        //设计资格
        List<HrQualify>  existList=hrQualifyMapper.selectAll(params);

        for(HrEmployeeDto hrEmployeeDto:employees){

            if(existList.stream().noneMatch(p->p.getUserLogin().equalsIgnoreCase(hrEmployeeDto.getUserLogin()))){
                HrQualify model=new HrQualify();
                model.setUserLogin(hrEmployeeDto.getUserLogin());
                model.setUserName(hrEmployeeDto.getUserName());
                model.setDeptId(hrEmployeeDto.getDeptId());
                HrDept hrDept = hrDeptMapper.selectByPrimaryKey(hrEmployeeDto.getDeptId());

                model.setDeptName(hrDept.getName());
                if(majorNames.contains(hrEmployeeDto.getSpecialty())){
                    model.setMajorName(hrEmployeeDto.getSpecialty());
                }else {
                    model.setMajorName("全部");
                }

                model.setCheckYear(checkYear);
                model.setProjectCharge(true);
                model.setProjectExeCharge(true);
                model.setMajorCharge(true);
                model.setDesign(true);
                model.setProofread(true);
                model.setAudit(true);
                model.setApprove(false);
                model.setCompanyApprove(false);
                model.setProjectType("全部");
                model.setDeleted(false);
                model.setGmtCreate(new Date());
                model.setGmtModified(new Date());
                model.setProChief(false);
                model.setChiefDesigner(false);

                model.setExploreAudit(false);
                model.setExploreEngineer(false);
                model.setExplorePrincipal(false);
                model.setProjectExeCharge(false);

                model.setConsultAudit(false);
                model.setConsultDesign(false);
                model.setConsultProofread(false);


                model.setContractAudit(false);
                model.setContractDesign(false);
                model.setContractProofread(false);

                model.setSupervisorAudit(false);
                model.setSupervisorDesign(false);
                model.setSupervisorProofread(false);
                //职能部门资格都为false
                if ("职能".equals(hrDept.getDeptType())){
                    model.setProjectCharge(false);
                    model.setProjectExeCharge(false);
                    model.setMajorCharge(false);
                    model.setDesign(false);
                    model.setProofread(false);
                    model.setAudit(false);
                }
                model.setRemark("");
                model.setBusinessKey("");
                ModelUtil.setNotNullFields(model);
                hrQualifyMapper.insert(model);
                model.setBusinessKey("hrQualify_"+model.getId());
                hrQualifyMapper.updateByPrimaryKey(model);
            }
            //如果资格表里用户专业是全部 且用户真实专业存在 则直接更新
            else {
                HrQualify model=existList.stream().filter(p->p.getUserLogin().equalsIgnoreCase(hrEmployeeDto.getUserLogin())).findFirst().get();
                model.setMajorName(hrEmployeeDto.getSpecialty());
                model.setGmtModified(new Date());

                model.setDeptId(hrEmployeeDto.getDeptId());//用户所在部门修改情况
                model.setDeptName(hrDeptMapper.selectByPrimaryKey(hrEmployeeDto.getDeptId()).getName());
                if(majorNames.contains(hrEmployeeDto.getSpecialty())){
                    model.setMajorName(hrEmployeeDto.getSpecialty());
                }else {
                    model.setMajorName("全部");
                }
                hrQualifyMapper.updateByPrimaryKey(model);
            }
        }

        guavaCacheService.invalidate("selectAllEmployee");
    }

    public void insertHrQualify(HrEmployeeDto hrEmployeeDto){
        int checkYear= Integer.parseInt(DateFormatUtils.format(new Date(),"yyyy"));
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("checkYear",checkYear);
        params.put("userLogin",hrEmployeeDto.getUserLogin());
        if(PageHelper.count(()->hrQualifyMapper.selectAll(params))==0) {

            HrQualify model = new HrQualify();
            model.setUserLogin(hrEmployeeDto.getUserLogin());
            model.setUserName(hrEmployeeMapper.getNameByUserLogin(model.getUserLogin()));
            model.setDeptId(hrEmployeeDto.getDeptId());
            model.setDeptName(hrDeptMapper.selectByPrimaryKey(hrEmployeeDto.getDeptId()).getName());
            model.setMajorName("全部");
            if (StringUtils.isNotEmpty(hrEmployeeDto.getSpecialty())) {
                model.setMajorName(hrEmployeeDto.getSpecialty());
            }
            model.setCheckYear(checkYear);
            model.setProjectCharge(true);
            model.setProjectExeCharge(true);
            model.setMajorCharge(true);
            model.setDesign(true);
            model.setProofread(true);
            model.setAudit(true);
            model.setApprove(false);
            model.setCompanyApprove(false);
            model.setProjectType("全部");
            model.setDeleted(false);
            model.setGmtCreate(new Date());
            model.setGmtModified(new Date());
            model.setProChief(false);
            model.setChiefDesigner(false);
            model.setRemark("");
            model.setBusinessKey("");
            ModelUtil.setNotNullFields(model);
            hrQualifyMapper.insert(model);
            model.setBusinessKey("hrQualify_" + model.getId());
            hrQualifyMapper.updateByPrimaryKey(model);
        }
    }

    /**
     * 开启/关闭
     * @param id
     * @param role
     */
    public void toggleEnable(int id,String role){
        HrQualify hrQualify=hrQualifyMapper.selectByPrimaryKey(id);
        if(hrQualify!=null){
            if("设计人".equals(role)){
                hrQualify.setDesign(!hrQualify.getDesign());
            }else if("校核人".equals(role)){
                hrQualify.setProofread(!hrQualify.getProofread());
            }else if("审核人".equals(role)){
                hrQualify.setAudit(!hrQualify.getAudit());
            }else if("审定人-院级".equals(role)){
                hrQualify.setApprove(!hrQualify.getApprove());
            }else if("审定人-公司级".equals(role)){
                hrQualify.setMajorCharge(!hrQualify.getMajorCharge());
            }else if("项目负责人".equals(role)){
                hrQualify.setProjectCharge(!hrQualify.getProjectCharge());
            }
            else if("项目执行负责人".equals(role)){
                hrQualify.setProjectExeCharge(!hrQualify.getProjectExeCharge());
            }else if("总设计师".equals(role)){
                hrQualify.setChiefDesigner(!hrQualify.getChiefDesigner());
            }else if("consultDesign".equals(role)){ //工程咨询
                hrQualify.setConsultDesign(!hrQualify.getConsultDesign());
            }else if("consultProofread".equals(role)){
                hrQualify.setConsultProofread(!hrQualify.getConsultProofread());
            }else if("consultAudit".equals(role)){
                hrQualify.setConsultAudit(!hrQualify.getConsultAudit());
            }else if("contractDesign".equals(role)){//工程承包
                hrQualify.setContractDesign(!hrQualify.getContractDesign());
            }else if("contractProofread".equals(role)){
                hrQualify.setContractProofread(!hrQualify.getContractProofread());
            }else if("contractAudit".equals(role)){
                hrQualify.setContractAudit(!hrQualify.getContractAudit());
            }else if("exploreEngineer".equals(role)){//勘察
                hrQualify.setExploreEngineer(!hrQualify.getExploreEngineer());
            }else if("exploreAudit".equals(role)){
                hrQualify.setExploreAudit(!hrQualify.getExploreAudit());
            }else if("explorePrincipal".equals(role)){
                hrQualify.setExplorePrincipal(!hrQualify.getExplorePrincipal());
            }else if("supervisorDesign".equals(role)){//监理
                hrQualify.setSupervisorDesign(!hrQualify.getSupervisorDesign());
            }else if("supervisorProofread".equals(role)){
                hrQualify.setSupervisorProofread(!hrQualify.getSupervisorProofread());
            }else if("supervisorAudit".equals(role)){
                hrQualify.setSupervisorAudit(!hrQualify.getSupervisorAudit());
            }
            hrQualify.setGmtModified(new Date());
            hrQualifyMapper.updateByPrimaryKey(hrQualify);
        }
    }

    public void updateMajor(int id,String majorName){
        HrQualify hrQualify=hrQualifyMapper.selectByPrimaryKey(id);
        if(hrQualify!=null){
            if(!hrQualify.getMajorName().equalsIgnoreCase(majorName)){
                //如果不勾选专业 默认全部
                if ("".equals(majorName)||majorName==null){
                    hrQualify.setMajorName("全部");
                }else {
                    hrQualify.setMajorName(majorName);
                }
                hrQualify.setGmtModified(new Date());
                hrQualifyMapper.updateByPrimaryKey(hrQualify);

                HrEmployee hrEmployee=hrEmployeeMapper.selectByUserLoginOrNo(hrQualify.getUserLogin());
                if(hrEmployee!=null){
                    if(StringUtils.isEmpty(hrEmployee.getSpecialty())){
                        hrEmployee.setSpecialty(majorName);
                        hrEmployee.setGmtModified(new Date());
                        hrEmployeeMapper.updateByPrimaryKey(hrEmployee);
                    }
                }
            }
        }
    }

    public void insertByDate(HrQualify hrQualify){
        hrQualifyMapper.insert(hrQualify);
    }
}
