package com.cmcu.mcc.hr.service;

import com.cmcu.act.dto.ActCandidateTask;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSimpleDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HrDeptService {

    @Resource
    HrDeptMapper hrDeptMapper;


    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Autowired
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    HrEmployeeSysService hrEmployeeSysService;
    @Resource
    SelectEmployeeService selectEmployeeService;


    public HrDeptDto getModelById(int id){
        return  getDto(hrDeptMapper.selectByPrimaryKey(id));
    }

    /**
     *   五洲获取默认部门
     *   一院  二院  环能 等有多个下级部门的单位默认部门 为一院 二院 环能院
     *   建研院下子部门 默认为 建研院 子部门名称
     * @param id
     * @return
     */
    public Map<String,Object> getDefaultDept(int id){
        Map<String,Object> map=Maps.newHashMap();
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(id);
        if (hrDept.getParentId()==1){
            map.put("deptId",hrDept.getId());
            map.put("deptName",hrDept.getName());
        }else {
            HrDept parentDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            if (parentDept.getId()==47){
                map.put("deptId",hrDept.getId());
                map.put("deptName",hrDept.getName());
            }else {
                map.put("deptId",parentDept.getId());
                map.put("deptName",parentDept.getName());
            }
        }

        return map;
    }
    /**
     *   五洲获取默认部门 上级部门
     *   一院  二院  环能 等有多个下级部门的单位默认部门 为一院 二院 环能院
     * @param id
     * @return
     */
    public Map<String,Object> getDefaultDept2(int id){
        Map<String,Object> map=Maps.newHashMap();
        HrDept hrDept = hrDeptMapper.selectByPrimaryKey(id);
        if (hrDept.getParentId()==1){
            map.put("deptId",hrDept.getId());
            map.put("deptName",hrDept.getName());
            map.put("deptType",hrDept.getDeptType());
        }else {
            HrDept parentDept = hrDeptMapper.selectByPrimaryKey(hrDept.getParentId());
            map.put("deptId",parentDept.getId());
            map.put("deptName",parentDept.getName());
            map.put("deptType",parentDept.getDeptType());
        }

        return map;
    }

    public String getDeptNameById(int id){
        HrDept hrDept=hrDeptMapper.selectByPrimaryKey(id);
        if(hrDept!=null) return hrDept.getName();
        return "";
    }


    public HrDeptDto getModelByName(String name){
        Map params=Maps.newHashMap();
        params.put("name",name);
        params.put("deleted",false);
        if(PageHelper.count(()->hrDeptMapper.selectAll(params))>0){
           return getDto(hrDeptMapper.selectAll(params).get(0));
        }
        return null;
    }

    public List<HrDeptDto> selectAll(){
        List<HrDeptDto> list= Lists.newArrayList();
        for(HrDept hrDept:hrDeptMapper.selectAll(Maps.newHashMap())){
            if(!hrDept.getDeleted()) {
                list.add(getDto(hrDept));
            }
        }
        return list;
    }

    public List<HrDeptDto> selectAllByUiSref(String userLogin,String uiSref){
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        List<HrDeptDto> list= Lists.newArrayList();
        for(HrDept hrDept:hrDeptMapper.selectAll(Maps.newHashMap())){
            if(!hrDept.getDeleted()&&deptIdList.contains(hrDept.getId())) {
                list.add(getDto(hrDept));
            }
        }
        return list;
    }


    public void remove(int id){
        HrDept hrDept=hrDeptMapper.selectByPrimaryKey(id);
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("deptId",hrDept.getId());
        if(PageHelper.count(()->hrEmployeeSysMapper.selectAll(params))>0){
            throw new IllegalArgumentException("请先删除该部门下员工!");
        }
        hrDept.setDeleted(true);
        hrDeptMapper.updateByPrimaryKey(hrDept);
    }

    public void update(HrDeptDto item) {
        if(!item.getDeleted()){
            HrDept before=hrDeptMapper.selectByPrimaryKey(item.getId());
            Assert.state(before!=null,"待更新的部门不存在");
            if(checkExist(item.getParentId(),item.getName(),item.getId())) {
                throw new IllegalArgumentException("同一层级下存在相同名称的部门");
            }
        }
        HrDept model=hrDeptMapper.selectByPrimaryKey(item.getId());
        model.setName(item.getName());
        model.setDeptCode(item.getDeptCode());
        model.setDeptType(item.getDeptType());
        model.setParentId(item.getParentId());
        model.setDeptChargeMan(item.getDeptChargeMan());//部门 分管领导 为满足五洲分管领导对应多个部门使用
        model.setDeptChargeManName(item.getDeptChargeManName());

        model.setDeptFirstLeader(item.getDeptFirstLeader());//部门正职
        model.setDeptFirstLeaderName(item.getDeptFirstLeaderName());
        model.setDeptSecondLeader(item.getDeptSecondLeader());//部门副职
        model.setDeptSecondLeaderName(item.getDeptSecondLeaderName());
        model.setDeptFinanceMan(item.getDeptFinanceMan());//财务人员
        model.setDeptFinanceManName(item.getDeptFinanceManName());

        model.setSeq(item.getSeq());
        model.setDeleted(item.getDeleted());
        model.setGmtModified(new Date());
        model.setRemark(item.getRemark());
        BeanValidator.validateObject(model);
        hrDeptMapper.updateByPrimaryKey(model);
    }

    public HrDeptDto getNewModel(int parentId) {
        Map<String, Object> deptParam = Maps.newHashMap();
        deptParam.put("deleted", false);
        deptParam.put("parentId", parentId);
        long total = PageHelper.count(() -> hrDeptMapper.selectAll(deptParam));
        HrDept parent = hrDeptMapper.selectByPrimaryKey(parentId);
        HrDept model = new HrDept();
        model.setDeptType(parent.getDeptType());
        model.setDeptCode("00");
        model.setParentId(parentId);
        model.setDeleted(false);
        model.setSeq((int) (total + 1));
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(model);
        hrDeptMapper.insert(model);
        return getDto(model);
    }

    public List<HrDeptDto> listParentCandicate(Integer id) {
        List<HrDeptDto> all = selectAll();
        List<HrDeptDto> childDeptList = listChild(id);

        return all.stream().filter(p -> !childDeptList.stream().anyMatch(o -> o.getId().equals(p.getId()))).collect(Collectors.toList());
    }

    public List<HrDeptDto> listChild(Integer id){
        List<HrDeptDto> all = selectAll();
        List<HrDeptDto> childDeptList=Lists.newArrayList();
        replenishChild(all,childDeptList,id);
        return childDeptList;
    }

    private HrDeptDto getDto(HrDept item){

        HrDeptDto dto=HrDeptDto.adapt(item);
        if(item.getParentId()!=0) {
            HrDept hrDept = hrDeptMapper.selectByPrimaryKey(item.getParentId());
            dto.setParentDeptName(hrDept.getName());
        }
        return dto;
    }

    private boolean checkExist(Integer parentId,String deptName,Integer deptId){
        Map<String,Object> params= Maps.newHashMap();
        params.put("parentId",parentId);
        params.put("name",deptName);
        params.put("deleted",false);
        List<HrDept> list=hrDeptMapper.selectAll(params);
        if(list.size()==0) {
            return false;
        }else{
            if(deptId==null||deptId==0){
                return true;
            }
            //不是自己
            if(!list.get(0).getId().equals(deptId)){
                return true;
            }
        }
        return false;
    }

    private void replenishChild(List<HrDeptDto> all,List<HrDeptDto> childDeptList,int id){
        for(HrDeptDto dept:all){
            if(dept.getParentId().equals(id)){
                childDeptList.add(dept);
                replenishChild(all,childDeptList,dept.getId());
            }
        }
    }

    public List<String> getDeptChargeMen(int deptId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", deptId);
        List<HrEmployeeSys> hrEmployeeSys = hrEmployeeSysMapper.selectAll(params);
        String[] keyPositions = new String[]{"院长", "分院院长", "部长", "所长"};
        for (String position : keyPositions) {
            if (hrEmployeeSys.stream().anyMatch(p -> p.getPositionNames().contains("," + position + ","))) {
                return hrEmployeeSys.stream().filter(p -> p.getPositionNames().contains("," + position + ",")).map(HrEmployeeSys::getUserLogin).collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }

    public List<HrEmployeeSys> selectEmployee(Integer deptId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("deptId", deptId);
        List<HrEmployeeSys> hrEmployeeSys = hrEmployeeSysMapper.selectAll(params);
        return hrEmployeeSys;
    }

    /**
     * 通过部门名称查找 部门
     * @param deptName
     * @return
     */
    public HrDept selectByName(String deptName){
        if ("".equals(deptName)){
            return null;
        }
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("name", deptName);
        if(PageHelper.count(()->hrDeptMapper.selectAll(params))==0)return null;
        return hrDeptMapper.selectAll(params).get(0);
    }

    /**
     * 表格导入数据是新增部门 默认
     * @param name
     * @return
     */
    public int insertByName(String name){
        HrDept hrDept=new HrDept();
        Map params = Maps.newHashMap();
        params.put("name", name);
        if(PageHelper.count(()->hrDeptMapper.selectAll(params))>0){
            hrDept = hrDeptMapper.selectAll(params).get(0);
            hrDept.setDeleted(false);
            hrDept.setGmtModified(new Date());
            hrDeptMapper.updateByPrimaryKey(hrDept);
        }else {
            HrDept parentDept = selectByName("中国五洲工程设计有限公司");
            Map<String, Object> deptParam = Maps.newHashMap();
            deptParam.put("deleted", false);
            deptParam.put("parentId", parentDept.getId());
            long total = PageHelper.count(() -> hrDeptMapper.selectAll(deptParam));
            hrDept.setDeptType(parentDept.getDeptType());
            hrDept.setDeptCode("00");
            hrDept.setParentId(parentDept.getId());
            hrDept.setDeleted(false);
            hrDept.setSeq((int) (total + 1));
            hrDept.setGmtModified(new Date());
            hrDept.setGmtCreate(new Date());
            hrDept.setRemark("导入数据时，新建部门");
            ModelUtil.setNotNullFields(hrDept);
            hrDeptMapper.insert(hrDept);
        }
        return hrDept.getId();
    }

    public List<HrDeptDto> selectByDeptIds(String deptIds) {
        Calendar date = Calendar.getInstance();
        int sortId =99999;

        List<String> depts = MyStringUtil.getStringList(deptIds);
        List<HrDeptDto> list= Lists.newArrayList();

        Map map1 =new HashMap();
        map1.put("deleted",false);
        List<HrDept> hrDepts = hrDeptMapper.selectAll(map1);
        for(HrDept hrDept:hrDepts){
                if(depts.contains(hrDept.getId()+"")||depts.contains(hrDept.getParentId()+"")||depts.contains(1+"")){
                    if(!depts.contains(hrDept.getId()+"")){
                        depts.add(hrDept.getId()+"");
                    }
                    HrDeptDto dto = getDto(hrDept);
                    dto.setUserLogin("");
                    dto.setUserDeptName("");
                    //dto.setIcon("icon-users");
                    list.add(dto);
                    //添加该部门下的人员
                    Map map= new HashMap();
                    map.put("deptId",dto.getId());
                    List<HrEmployeeSimpleDto> hrEmployeeSimpleDtos = hrEmployeeMapper.selectOaSimpleAll(map);
                    for(int i=0;i<hrEmployeeSimpleDtos.size();i++){
                        //判断人员状态
                        if(!hrEmployeeSimpleDtos.get(i).getUserStatus().equals("离职")){
                            HrDeptDto hrDeptDto = new HrDeptDto();
                            hrDeptDto.setParentId(dto.getId());
                            hrDeptDto.setName(hrEmployeeSimpleDtos.get(i).getUserName());
                            hrDeptDto.setUserLogin(hrEmployeeSimpleDtos.get(i).getUserLogin());
                            hrDeptDto.setIcon("icon-user");
                            hrDeptDto.setUserDeptName(hrEmployeeSimpleDtos.get(i).getDeptName());
                            sortId--;
                            hrDeptDto.setId(sortId);
                            hrDeptDto.setSeq(hrEmployeeSimpleDtos.get(i).getUserSeq());
                            list.add(hrDeptDto);
                        }
                    }
            }
        }
        //如果没有根节点 添加根节点
        if(!depts.contains(1+"")){
            HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(1));
            dto.setUserLogin("");
            dto.setUserDeptName("");
            //dto.setName("");
            //dto.setIcon("icon-users");
            list.add(dto);
            depts.add(1+"");
        }
        //添加已选集合的父节点
        for(String deptId:depts){
            HrDept hrDept = hrDeptMapper.selectByPrimaryKey(Integer.parseInt(deptId));
            if(!depts.contains(hrDept.getParentId()+"")&&!hrDept.getDeleted()&&hrDept.getParentId()!=0){
                HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(hrDept.getParentId()));
                dto.setUserLogin("");
                dto.setUserDeptName("");
                //dto.setIcon("icon-users");
                list.add(dto);
            }
        }
        //去重 set对象去重 比较的引用
        Set set = new HashSet();
        set.addAll(list);     // 将list所有元素添加到set中    set集合特性会自动去重复
        list.clear();
        list.addAll(set);    // 将list清空并将set中的所有元素添加到list中
        List<HrDeptDto> lists =list.stream().sorted(Comparator.comparing(HrDeptDto::getSeq)).collect(Collectors.toList());
        return lists;
    }

    //取公司领导级人员选择
    public List<HrDeptDto> selectByDeptLeader(String deptIds) {
        int sortId =99999;

        List<String> depts = MyStringUtil.getStringList(deptIds);
        List<HrDeptDto> list= Lists.newArrayList();

        Map map1 =new HashMap();
        map1.put("deleted",false);
        List<HrDept> hrDepts = hrDeptMapper.selectAll(map1);
        for(HrDept hrDept:hrDepts){
            if(depts.contains(hrDept.getId()+"")||depts.contains(hrDept.getParentId()+"")||depts.contains(1+"")){
                if(!depts.contains(hrDept.getId()+"")){
                    depts.add(hrDept.getId()+"");
                }
                HrDeptDto dto = getDto(hrDept);
                dto.setUserLogin("");
                dto.setUserDeptName("");
                list.add(dto);
                //添加该部门下的人员
                if(dto.getId()==16){
                    //如果是公司领导 部门取领导该部门所有人
                    Map map= new HashMap();
                    map.put("deptId",dto.getId());
                    List<HrEmployeeSimpleDto> hrEmployeeSimpleDtos = hrEmployeeMapper.selectOaSimpleAll(map);
                    for(int i=0;i<hrEmployeeSimpleDtos.size();i++){
                        //判断人员状态
                        if(!hrEmployeeSimpleDtos.get(i).getUserStatus().equals("离职")){
                            HrDeptDto hrDeptDto = new HrDeptDto();
                            hrDeptDto.setParentId(dto.getId());
                            hrDeptDto.setName(hrEmployeeSimpleDtos.get(i).getUserName());
                            hrDeptDto.setUserLogin(hrEmployeeSimpleDtos.get(i).getUserLogin());
                            hrDeptDto.setIcon("icon-user");
                            hrDeptDto.setUserDeptName(hrEmployeeSimpleDtos.get(i).getDeptName());
                            sortId--;
                            hrDeptDto.setId(sortId);
                            hrDeptDto.setSeq(hrEmployeeSimpleDtos.get(i).getUserSeq());
                            list.add(hrDeptDto);
                        }
                    }
                }else {
                    //正职
                    if (!StringUtils.isEmpty(hrDept.getDeptFirstLeader())){
                        String[] split = hrDept.getDeptFirstLeader().split(",");
                        for (String enLogin :split){
                            HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(enLogin);
                            HrDeptDto hrDeptDto = new HrDeptDto();
                            hrDeptDto.setParentId(dto.getId());
                            hrDeptDto.setName(hrEmployeeDto.getUserName()+"(正)");
                            hrDeptDto.setUserLogin(enLogin);
                            hrDeptDto.setIcon("icon-user");
                            hrDeptDto.setUserDeptName(hrEmployeeDto.getDeptName());
                            sortId--;
                            hrDeptDto.setId(sortId);
                            hrDeptDto.setSeq(hrEmployeeDto.getSeq());
                            list.add(hrDeptDto);
                        }

                    }
                    //副职
                    if (!StringUtils.isEmpty(hrDept.getDeptSecondLeader())){
                        String[] split = hrDept.getDeptSecondLeader().split(",");
                        for (String enLogin :split){
                            HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(enLogin);
                            HrDeptDto hrDeptDto = new HrDeptDto();
                            hrDeptDto.setParentId(dto.getId());
                            hrDeptDto.setName(hrEmployeeDto.getUserName()+"(副)");
                            hrDeptDto.setUserLogin(enLogin);
                            hrDeptDto.setIcon("icon-user");
                            hrDeptDto.setUserDeptName(hrEmployeeDto.getDeptName());
                            sortId--;
                            hrDeptDto.setId(sortId);
                            hrDeptDto.setSeq(hrEmployeeDto.getSeq());
                            list.add(hrDeptDto);
                        }

                    }
                }


            }
        }
        //如果没有根节点 添加根节点
        if(!depts.contains(1+"")){
            HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(1));
            dto.setUserLogin("");
            dto.setUserDeptName("");
            list.add(dto);
            depts.add(1+"");
        }
        //添加已选集合的父节点
        for(String deptId:depts){
            HrDept hrDept = hrDeptMapper.selectByPrimaryKey(Integer.parseInt(deptId));
            if(!depts.contains(hrDept.getParentId()+"")&&!hrDept.getDeleted()&&hrDept.getParentId()!=0){
                HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(hrDept.getParentId()));
                dto.setUserLogin("");
                dto.setUserDeptName("");
                list.add(dto);
            }
        }
        //去重 set对象去重 比较的引用
        Set set = new HashSet();
        set.addAll(list);     // 将list所有元素添加到set中    set集合特性会自动去重复
        list.clear();
        list.addAll(set);    // 将list清空并将set中的所有元素添加到list中
        List<HrDeptDto> lists =list.stream().sorted(Comparator.comparing(HrDeptDto::getSeq)).collect(Collectors.toList());
        return lists;
    }






    public List<HrDeptDto> listDataByDeptIdList(String deptIdList) {
        List<HrDeptDto> list=Lists.newArrayList();
        List<String> deptIds= MyStringUtil.getStringList(deptIdList);
        for(String deptId:deptIds){
            if(Integer.parseInt(deptId)!=0){
                HrDeptDto hrDeptDto = getModelById(Integer.parseInt(deptId));
                if(hrDeptDto!=null) {
                    list.add(hrDeptDto);
                }
            }

        }
        return list;
    }
    //根据查询部门id 返回树节点数据
    public List<HrDeptDto> selectDeptByDeptIds(String deptIds,boolean bigDept) {
        List<Integer> depts = MyStringUtil.getIntList(deptIds);
        List<HrDeptDto> list= Lists.newArrayList();
        for(HrDept hrDept:hrDeptMapper.selectAll(Maps.newHashMap())){
            if(!hrDept.getDeleted()) {
                if(depts.contains(hrDept.getId())||depts.contains(hrDept.getParentId())||depts.contains(1)){
                    if(!depts.contains(hrDept.getId())){
                        depts.add(hrDept.getId());
                    }
                    HrDeptDto dto = getDto(hrDept);
                    dto.setUserLogin("");
                    //dto.setIcon("icon-users");
                    if(bigDept){
                         //判断是否为二级部门
                        if(dto.getParentId()==1||dto.getId()==1){
                            list.add(dto);
                        }
                    }else {
                        list.add(dto);
                    }

                }
            }
        }
        //添加已选集合的父节点
        for(int deptId:depts){
            HrDept hrDept = hrDeptMapper.selectByPrimaryKey(deptId);
            if(!depts.contains(hrDept.getParentId())&&!hrDept.getDeleted()&&hrDept.getParentId()!=0){
                HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(hrDept.getParentId()));
                dto.setUserLogin("");
                //dto.setIcon("icon-users");
                list.add(dto);
            }
        }
        //如果没有根节点 添加根节点
        if(!depts.contains(1)){
            HrDeptDto dto = getDto(hrDeptMapper.selectByPrimaryKey(1));
            dto.setUserLogin("");
            //dto.setIcon("icon-users");
            list.add(dto);
        }
        //去重
        Set set = new HashSet();
        set.addAll(list);     // 将list所有元素添加到set中    set集合特性会自动去重复
        list.clear();
        list.addAll(set);    // 将list清空并将set中的所有元素添加到list中
        //list.stream().sorted(Comparator.comparing(HrDeptDto::getSeq).reversed()).collect(Collectors.toList());
        List<HrDeptDto> lists =list.stream().sorted(Comparator.comparing(HrDeptDto::getSeq)).collect(Collectors.toList());
        return lists;
    }

    /**
     *查询 部门list 中国五洲工程设计有限公司节点下第一子节点  选择分类使用
     * @return
     */
    public List<HrDeptDto> listMultipleDept(){
        Map map =new HashMap();
        map.put("deleted",false);
        map.put("parentId",1);
        List<HrDept> hrDeptList = hrDeptMapper.selectAll(map);
        List<HrDeptDto> list= Lists.newArrayList();
        hrDeptList.forEach(p->{
          list.add(getDto(p)) ;
        });
        return list.stream().sorted(Comparator.comparing(HrDeptDto::getSeq)).collect(Collectors.toList());
    }




}
