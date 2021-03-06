package com.cmcu.mcc.ed.service;

import com.cmcu.common.dao.CommonEdBuildMapper;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonConfigService;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.business.dao.BusinessContractMapper;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.business.service.BusinessContractService;
import com.cmcu.mcc.business.service.BusinessInputContractService;
import com.cmcu.mcc.business.service.BusinessPreContractService;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.dao.EdStepBuildMapper;
import com.cmcu.mcc.ed.dto.EdProjectStepDto;
import com.cmcu.mcc.ed.dto.EdProjectTreeDto;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.entity.EdProjectTree;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.HrPositionService;
import com.cmcu.mcc.service.impl.EdDataService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service
public class EdProjectStepService {




    @Resource
    EdProjectTreeService edProjectTreeService;



    @Resource
    BusinessContractMapper businessContractMapper;

    @Autowired
    BusinessContractService businessContractService;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    public EdProjectStepMapper edProjectStepMapper;

    @Resource
    EdStepBuildMapper edStepBuildMapper;


    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Autowired
    HrPositionService hrPositionService;
    @Autowired
    CommonConfigService commonConfigService;
    @Autowired
    BusinessInputContractService businessInputContractService;
    @Autowired
    BusinessPreContractService businessPreContractService;

    @Autowired
    CommonCodeService commonCodeService;

    @Resource
    CommonEdBuildMapper commonEdBuildMapper;
    @Resource
    EdDataService edDataService;
    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    /**
     * ???????????????????????????
     * @param id
     * @param userLogin
     * @return
     */
    public boolean checkIsChargeUser(int id,Integer stepId,String userLogin) {

        if (stepId > 0) {
            EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(stepId);
            if (edProjectStep.getExeChargeMan().equalsIgnoreCase(userLogin) || edProjectStep.getChargeMan().equalsIgnoreCase(userLogin)) {
                return true;
            }
        }
        BusinessContractDto businessContractDto = businessContractService.getModelById(id);
        String all = businessContractDto.getChargeMen() + "," + businessContractDto.getExeChargeMen() + "," + businessContractDto.getBusinessContractDetail().getTotalDesigner();
        return MyStringUtil.getStringList(all).contains(userLogin);
    }


    public List<EdProjectStep> listDataByStageNodeId(int stageNodeId) {
        EdProjectTreeDto edProjectTreeDto=edProjectTreeService.getModelById(stageNodeId);
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("projectId", edProjectTreeDto.getProjectId());
        if(!edProjectTreeDto.getNodeName().contains("????????????")) {
            if(edProjectTreeDto.getNodeName().equals("??????????????????")){
                params.put("stageName", edProjectTreeService.edProjectTreeMapper.selectByPrimaryKey(edProjectTreeDto.getParentId()).getNodeName());
            }else{
                params.put("stageName", edProjectTreeDto.getNodeName());
            }
        }
        List<EdProjectStep> list= edProjectStepMapper.selectAll(params);
        return list;
    }


    public PageInfo<Object> listCpPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("cp",true);
        params.put("deleted",false);
        params.put("attendUser", params.get("userLogin"));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> edProjectStepMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto((EdProjectStep)p)));
        pageInfo.setList(list);
        return pageInfo;
    }


    public List<EdProjectStepDto> listCpData(String userLogin) {

        List<EdProjectStepDto> list = Lists.newArrayList();


        List<EdProjectStep> oList = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("cp", true);
        params.put("deleted", false);
        params.put("cpClosed",false);

        //1.?????????????????????????????????
        params.put("attendUser", userLogin);
        oList.addAll(edProjectStepMapper.selectAll(params));

        //2.??????????????????????????????????????????????????????
        if(hrPositionService.IsDeptChargeMan(userLogin)) {
            List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, "me.cpStep");
            if (deptIdList.size() > 0) {
                params.put("deptIdList", deptIdList);
                params.remove("attendUser");
                oList.addAll(edProjectStepMapper.selectAll(params));
                params.remove("deptIdList");
            }
        }
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
        oList.forEach(p -> {
            //??????????????????????????????????????????
            if(list.stream().noneMatch(o->o.getId().equals(p.getId()))) {
                EdProjectStepDto edProjectStepDto = getDto(p);
                edProjectStepDto.setUserLogin(userLogin);
                edProjectStepDto.setCadHide(hideList.contains(edProjectStepDto.getId()));
                list.add(edProjectStepDto);
            }
        });
        return list;
    }


    public List<EdProjectStepDto> listStepByAttendEnLogin(String enLogin,boolean onlyCp) {

        List<EdProjectStepDto> list = Lists.newArrayList();

        HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(enLogin);
        List<EdProjectStep> oList = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("attendUser", enLogin);
        if(onlyCp){
            params.put("cp", true);
            params.put("cpClosed",false);
        }

        //1.???????????????????????????????????????
        oList.addAll(edProjectStepMapper.selectAll(params));

        //2.??????????????????????????????????????????????????????
        if(hrPositionService.IsDeptChargeMan(enLogin)) {
            List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(enLogin, "me.cpStep");
            if (deptIdList.size() > 0) {
                params.put("deptIdList", deptIdList);
                params.remove("attendUser");
                oList.addAll(edProjectStepMapper.selectAll(params));
                params.remove("deptIdList");
            }
        }



        List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
        oList.forEach(p -> {
            //??????????????????????????????????????????
            if(list.stream().noneMatch(o->o.getId().equals(p.getId()))) {
                if(!onlyCp||!hideList.contains(p.getId())) {
                    EdProjectStepDto edProjectStepDto = getDto(p);
                    edProjectStepDto.setUserLogin(enLogin);
                    edProjectStepDto.setCadHide(hideList.contains(p.getId()));
                    list.add(edProjectStepDto);
                }
            }
        });
        return list;
    }

    /**
     * ?????????CAD??????????????????
     * projectId???projectName???stageName???stepName???stepId
     * @param enLogin
     * @return
     */
    public List<Map> listCadProjectByEnLogin(String enLogin,String q){
        List<Map> mapList=Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("cp", true);//Cad????????????
        params.put("attendUser", enLogin);
        List<EdProjectStep> list=edProjectStepMapper.selectAll(params);

        if(StringUtils.isNotEmpty(q)) list=list.stream().filter(p->p.getProjectName().contains(q)||p.getStepName().contains(q)||p.getStageName().contains(q)).collect(Collectors.toList());

        for (EdProjectStep step:list){
            Map<String,Object> map=Maps.newHashMap();
            map.put("projectId",step.getProjectId());
            map.put("projectName",step.getProjectName());
            map.put("stageName",step.getStageName());
            map.put("stepName",step.getStepName());
            map.put("stepId",step.getId());
            mapList.add(map);
        }
        return mapList;
    }


    public List<EdProjectStep> listAttendStep(int contractId,String enLogin,boolean onlyCp) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("attendUser", enLogin);
        params.put("projectId",contractId);
        if(onlyCp){
            params.put("cp", true);
            params.put("cpClosed",false);
        }
        BusinessContract businessContract=businessContractMapper.selectByPrimaryKey(contractId);

        //??????????????????????????????,??????????????????????????????
        if(hrPositionService.IsDeptChargeMan(enLogin)) {
            List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(enLogin, "me.cpStep");
            if (deptIdList.contains(businessContract.getDeptId())) {
                params.remove("attendUser");
            }
        }
        List<EdProjectStep> list=edProjectStepMapper.selectAll(params);

        if(onlyCp) {
            HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(enLogin);
            List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
            list=list.stream().filter(p->!hideList.contains(p.getId())).collect(Collectors.toList());
        }
        return list;
    }


    //????????????CAD ???????????????
    public void toggleCadHide(int stepId,String  userLogin){
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
        List<Integer> newList=Lists.newArrayList();
        //???????????????????????? ??? -????????? ???- ??????
        if (hideList.stream().anyMatch(p->p.equals(stepId))){
            for (Integer hide:hideList){
                if (!hide.equals(stepId)){
                    newList.add(hide);
                }
            }
        }else {
            hideList.add(stepId);
            newList.addAll(hideList);
        }
        hrEmployeeSys.setCadHide(StringUtils.join(newList,","));
        hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
    }

    public PageInfo<EdProjectStep> listPagedAttendStep(Map<String,Object> params,String userLogin,Boolean cp,int pageNum, int pageSize) {
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<EdProjectStep> list1 = Lists.newArrayList();
        List<EdProjectStep> list2 = Lists.newArrayList();

        params.put("deleted", false);
        params.put("cpClosed",false);
        if(userLogin.equals("linhaiyan")){
            list1.addAll(edProjectStepMapper.selectAll(params));
        }else {
            //1.?????????????????????????????????
            //params.put("cp", true);
            params.put("attendUser", userLogin);
            list1.addAll(edProjectStepMapper.selectAll(params));
            //2.??????????????????????????????????????????????????????
            if (hrPositionService.IsDeptChargeMan(userLogin)) {
                List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, "me.cpStep");
                if (deptIdList.size() > 0) {
                    params.put("deptIdList", deptIdList);
                    params.remove("attendUser");
                    list2.addAll(edProjectStepMapper.selectAll(params));
                    params.remove("deptIdList");
                }
            }
        }
        //????????????list ??????????????????
        List<EdProjectStep> list =new ArrayList<>();
        list1.forEach(p->{
            AtomicBoolean flag = new AtomicBoolean(false);
            list.forEach(q->{
                if(q.getId().equals(p.getId())) {
                    flag.set(true);
                }
            });
            if(!flag.get()) {
                list.add(p);
            }
        });
        list2.forEach(p->{
            AtomicBoolean flag = new AtomicBoolean(false);
            list.forEach(q->{
                if(q.getId().equals(p.getId())) {
                    flag.set(true);
                }
            });
            if(!flag.get()) {
                list.add(p);
            }
        });
        //??????????????????
        List<EdProjectStep> pageList =new ArrayList<>();
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
        list.forEach(p -> {
            EdProjectStepDto edProjectStepDto = getDto((EdProjectStep)p);
            BusinessContractDto modelById = businessContractService.getModelById(edProjectStepDto.getProjectId());
            edProjectStepDto.setSignTime(modelById.getSignDate());
            edProjectStepDto.setUserLogin(userLogin);
            edProjectStepDto.setCadHide(hideList.contains(edProjectStepDto.getId()));
            pageList.add(edProjectStepDto);
        });
        //???????????????????????????
        pageList.stream().sorted(Comparator.comparing(EdProjectStep::getGmtModified)).collect(Collectors.toList());

       return PageInfoUtils.list2PageInfo(pageList,pageNum,pageSize);
    }

    public PageInfo<Object> listPagedAllStep(Map<String,Object> params,String userLogin,int pageNum, int pageSize) {
        params.put("cp", true);
        params.put("deleted", false);
        params.put("cpClosed",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> edProjectStepMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Integer> hideList = MyStringUtil.getIntList(hrEmployeeSys.getCadHide());
        pageInfo.getList().forEach(p -> {
            EdProjectStepDto edProjectStepDto = getDto((EdProjectStep)p);
            edProjectStepDto.setUserLogin(userLogin);
            edProjectStepDto.setCadHide(hideList.contains(edProjectStepDto.getId()));
            list.add(edProjectStepDto);
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void update(EdProjectStepDto item) {
        EdProjectStep model = edProjectStepMapper.selectByPrimaryKey(item.getId());
        model.setProjectName(item.getProjectName());
        model.setProjectNo(item.getProjectNo());
        model.setStageName(item.getStageName());
        model.setStepName(FileUtil.getGoodName(item.getStepName()));

        model.setCp(item.getCp());
        model.setCpClosed(item.getCpClosed());
        model.setStepStatus(item.getStepStatus());
        model.setSeq(item.getSeq());
        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());
        model.setDwgTime(item.getDwgTime());
        model.setDwgVersion(item.getDwgVersion());
        model.setContractNo(item.getContractNo());
        model.setContentSortMethod(item.getContentSortMethod());

        model.setChargeMan(MyStringUtil.getMultiDotString(item.getChargeMan()));
        model.setChargeManName(item.getChargeManName());
        model.setExeChargeMan(MyStringUtil.getMultiDotString(item.getExeChargeMan()));
        model.setExeChargeManName(item.getExeChargeManName());
        model.setOtherChargeMan(MyStringUtil.getMultiDotString(item.getOtherChargeMan()));
        model.setOtherChargeManName(item.getOtherChargeManName());
        model.setProjectChargeMan(MyStringUtil.getMultiDotString(item.getProjectChargeMan()));
        model.setProjectChargeManName(item.getProjectChargeManName());
        BeanValidator.check(model);
        edProjectStepMapper.updateByPrimaryKey(model);
        edProjectTreeService.genProjectTree(model.getProjectId());

        //?????? attendUser
        String businessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(model.getId());
        edDataService.checkAttendUser(businessKey);

    }


    public void remove(int id,String userLogin) {

        EdProjectStep item = edProjectStepMapper.selectByPrimaryKey(id);
        if(!checkIsChargeUser(item.getProjectId(),id,userLogin)) {

                throw  new IllegalArgumentException("??????????????????"+item.getStepName()+"?????????!");

        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        edProjectStepMapper.updateByPrimaryKey(item);
        edProjectTreeService.genProjectTree(item.getProjectId());
    }


    public EdProjectStepDto getModelById(int id) {
        return getDto(edProjectStepMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(int stageNodeId,String userLogin){
        Date now=new Date();
        EdProjectTree edProjectTree=edProjectTreeService.getModelById(stageNodeId);
        BusinessContractDto businessContract= businessContractService.getModelById(edProjectTree.getProjectId());
        if(!checkIsChargeUser(businessContract.getId(),0,userLogin)) {
            throw  new IllegalArgumentException("?????????????????????"+businessContract.getExeChargeMenName()+"????????????!");
        }

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("projectId", edProjectTree.getProjectId());
        String stageName=edProjectTree.getNodeName();

        //????????????????????????
        if(stageName.equals("??????????????????")){
            stageName =edProjectTreeService.edProjectTreeMapper.selectByPrimaryKey(edProjectTree.getParentId()).getNodeName();
        }
        params.put("stageName",stageName);

        //params.put("stageName", edProjectTree.getNodeName());
        int seq= (int)PageHelper.count(()->edProjectStepMapper.selectAll(params))+2;
        EdProjectStep model=new EdProjectStep();
        List<EdProjectStep> collect = edProjectStepMapper.selectAll(params).stream().sorted(Comparator.comparing(EdProjectStep::getGmtCreate).reversed()).collect(Collectors.toList());
        if (collect.size()>0){
         model.setProjectNo(collect.get(0).getProjectNo());
        }else {
            model.setProjectNo("");
        }
        model.setProjectId(edProjectTree.getProjectId());
        model.setProjectName(businessContract.getProjectName());
        model.setStageName(stageName);

        model.setContractNo(businessContract.getContractNo());
        model.setProjectNo(businessContract.getBusinessContractDetail().getProjectNo());
        model.setStepName(NumberChangeUtils.a2r(seq-1));//??? ??? ??? ??????

        model.setChargeMan(businessContract.getBusinessContractDetail().getTotalDesigner());//????????????
        model.setChargeManName(businessContract.getBusinessContractDetail().getTotalDesignerName());

        model.setExeChargeMan(businessContract.getBusinessManager());//????????????
        model.setExeChargeManName(businessContract.getBusinessManagerName());

        model.setOtherChargeMan(businessContract.getBusinessContractDetail().getOtherDesigner());//????????????
        model.setOtherChargeManName(businessContract.getBusinessContractDetail().getOtherDesignerName());

        model.setProjectChargeMan(businessContract.getChargeMen());//????????????
        model.setProjectChargeManName(businessContract.getChargeMenName());

        model.setCp(true);//????????????????????????
        model.setCpClosed(false);
        model.setSeq(seq);
        model.setDeleted(false);
        model.setCreator(userLogin);
        model.setCreatorName(hrEmployeeMapper.getNameByUserLogin(userLogin));
        model.setGmtCreate(now);
        model.setGmtModified(now);
        model.setContentSortMethod(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"?????????????????????").toString());
        ModelUtil.setNotNullFields(model);
        edProjectStepMapper.insert(model);
        edProjectTreeService.genProjectTree(model.getProjectId());
        //?????????????????????????????????????????? ????????????
        createPreTask(edProjectTree.getProjectId(),model.getId());

        //????????????????????????????????????????????????
        if(!model.getStageName().contains("??????")|| model.getStageName().contains("??????")||model.getStageName().contains("??????")){

            CommonEdBuild build=new CommonEdBuild();
            build.setBusinessKey(model.getId().toString());
            build.setBuildName(businessContract.getProjectName()+"_"+model.getStageName());
            build.setBuildNo(model.getStageName());
            build.setSeq(1);
            build.setDeleted(false);
            build.setGmtCreate(now);
            build.setGmtModified(now);
            ModelUtil.setNotNullFields(build);
            commonEdBuildMapper.insert(build);
        }


        return model.getId();
    }

    /**???????????????????????? ?????????????????????????????? ????????? ?????????????????????
     *
     * @param projectId
     * @param stepId
     */
    private void createPreTask(int projectId, int stepId) {
        //????????????????????????
        Map stepMap = new HashMap();
        stepMap.put("deleted",false);
        stepMap.put("projectId",projectId);
        List<EdProjectStep> edProjectSteps = edProjectStepMapper.selectAll(stepMap);
      /*  for(EdProjectStep step:edProjectSteps){
            //?????????????????????????????????????????????
            Map taskMap = new HashMap();
            taskMap.put("deleted",false);
            taskMap.put("stepId",step.getId());
            taskMap.put("processEnd",true);
            List<EdTask> edTasks = edTaskMapper.selectAll(taskMap);
            if(edTasks.size()!=0){
                EdTask edTask = edTasks.get(0);
                edTask.setStepId(stepId);
                edTask.setGmtModified(new Date());
                edTask.setGmtCreate(new Date());
                edTaskMapper.insert(edTask);
            }

        }*/

    }

    public EdProjectStepDto getDto(EdProjectStep item){
        EdProjectStepDto edProjectStepDto=EdProjectStepDto.adapt(item);
        edProjectStepDto.setBusinessKey("edProjectStep_"+item.getId());
        try {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("stepId", edProjectStepDto.getId());
            edProjectStepDto.setBuildCount((int) PageHelper.count(() -> edStepBuildMapper.selectAll(params)));
            BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(item.getProjectId());
            edProjectStepDto.setDeptName(businessContract.getDeptName());
            edProjectStepDto.setEdType(businessContract.getProjectType());
            edProjectStepDto.setProjectType(businessContract.getProjectType());
        }catch (Exception ex){

        }
        return edProjectStepDto;
    }


    /**
    *  ??????????????????
    * */
    public List<EdProjectStep> listAllStep(){
    Map params=Maps.newHashMap();
    params.put("deleted",false);
    List<BusinessContract> businessContracts = businessContractMapper.selectAll(params);
    List<EdProjectStep> list=Lists.newArrayList();
    //??????????????????????????????????????????
    for ( BusinessContract businessContract :businessContracts){
        params.put("projectId",businessContract.getId());
        list.addAll( edProjectStepMapper.listAll(params));
    }

    return list;
    }

    /**
     * ?????????????????????????????????
     * @param deptId
     * @return
     */
    public List<EdProjectStepDto> listAllStepByDeptId(int deptId){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("deptId",deptId);
        List<BusinessContract> businessContracts = businessContractMapper.selectAll(params);
        List<EdProjectStepDto> list=Lists.newArrayList();
        //??????????????????????????????????????????
        for ( BusinessContract businessContract :businessContracts){
            params.put("projectId",businessContract.getId());
            List<EdProjectStep> list1 = edProjectStepMapper.selectAll(params);
            for (EdProjectStep step:list1){
                EdProjectStepDto dto = getDto(step);
                dto.setContractMoney(businessContract.getContractMoney());
                //?????????????????????????????????????????????

                list.add(dto);
            }
        }
        return list;
    }

}
