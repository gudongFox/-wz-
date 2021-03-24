package com.cmcu.mcc.hr.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.hr.dao.HrDeptMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dao.HrPositionMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrPositionDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import com.cmcu.mcc.hr.entity.HrPosition;
import com.cmcu.mcc.sys.entity.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HrPositionService {


    @Resource
    HrPositionMapper hrPositionMapper;

    @Resource
    HrDeptMapper hrDeptMapper;

    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    private HrPositionDto getDto(HrPosition item) {
        if (item != null) {
            HrPositionDto dto = HrPositionDto.adapt(item);
            Map params = Maps.newHashMap();
            List<Integer> deptIdList = MyStringUtil.getIntList(item.getChildDeptIds());
            if(deptIdList.size()>0) {
                params.put("deptIds", deptIdList);
                params.put("deleted", false);
                List<HrDept> hrDeptList = hrDeptMapper.selectAll(params);
                if (hrDeptList.size() > 3) {
                    dto.setChildDeptIdNames(hrDeptList.get(0).getName() + "," + hrDeptList.get(1).getName() + "," + hrDeptList.get(2).getName() + "等" + hrDeptList.size() + "个部门");
                } else {
                    dto.setChildDeptIdNames(StringUtils.join(hrDeptList.stream().map(HrDept::getName).collect(Collectors.toList()), ","));
                }
            }
            return dto;
        }
        return null;
    }

    public int getNewModel() {
        Date now = new Date();
        HrPosition item = new HrPosition();
        item.setGmtModified(now);
        item.setGmtCreate(now);
        item.setSeq((int) PageHelper.count(() -> hrPositionMapper.selectAll(Maps.newHashMap())) + 1);
        item.setPositionLv(1);
        ModelUtil.setNotNullFields(item);
        hrPositionMapper.insert(item);
        return item.getId();
    }

    public HrPosition update(HrPositionDto item) {
        HrPosition model = hrPositionMapper.selectByPrimaryKey(item.getId());
        model.setPositionName(item.getPositionName());
        model.setDeptCharge(item.getDeptCharge());
        model.setChildDeptIds(item.getChildDeptIds());
        model.setRemark(item.getRemark());
        model.setSeq(item.getSeq());
        model.setGmtModified(new Date());
        model.setPositionLv(item.getPositionLv());
        model.setDeleted(item.getDeleted());
        BeanValidator.check(model);
        ModelUtil.setNotNullFields(model);
        hrPositionMapper.updateByPrimaryKey(model);
        return model;
    }

    public void remove(int id) {
        HrPosition item = hrPositionMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        hrPositionMapper.updateByPrimaryKey(item);
    }

    public HrPositionDto getModelById(int id) {
        return getDto(hrPositionMapper.selectByPrimaryKey(id));
    }

    public PageInfo<Object> loadPagedData(Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", 0);
        params.put("q", params.get("q"));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrPositionMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> list.add(getDto((HrPosition) p)));
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<HrPosition> selectAll() {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        return hrPositionMapper.selectAll(params);
    }

    public String selectByName(String names) {
        names.replaceAll("，", ",");
        List<String> ids = Lists.newArrayList();
        String[] split = names.split(",");
        for (String name : split) {
            if (!name.equals("")) {
                Map params = Maps.newHashMap();
                params.put("deleted", false);
                params.put("positionName", name);
                if (PageHelper.count(() -> hrPositionMapper.selectAll(params)) > 0) {

                    ids.add(hrPositionMapper.selectAll(params).get(0).getId().toString());
                }
            }
        }
        return "," + StringUtils.join(ids, ",") + ",";
    }


    /**
     * 得到用户作为部门负责人
     * 管辖的部门列表
     * @param userLogin
     * @return
     */
    public List<Integer> getChargeDeptIdList(String userLogin){
        List<Integer> chargeDeptIdList = Lists.newArrayList();
        HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(userLogin);
        if(hrEmployeeSys!=null) {
            if (StringUtils.isNotEmpty(hrEmployeeSys.getPositionIds())) {
                List<Integer> myPositionIds = MyStringUtil.getIntList(hrEmployeeSys.getPositionIds());
                for (Integer positionId : myPositionIds) {
                    HrPosition hrPosition = hrPositionMapper.selectByPrimaryKey(positionId);
                    if (hrPosition.getDeptCharge()) {
                        chargeDeptIdList.addAll(MyStringUtil.getIntList(hrPosition.getChildDeptIds()));
                        chargeDeptIdList.add(hrEmployeeSys.getDeptId());
                    }
                }
            }
        }
        return chargeDeptIdList;
    }


    /**
     * 判断用户是否部门负责人
     * @param userLogin
     * @return
     */
    public boolean IsDeptChargeMan(String userLogin) {
        HrEmployeeSys hrEmployeeSys = hrEmployeeSysMapper.selectByUserLogin(userLogin);
        if (hrEmployeeSys != null) {
            if (StringUtils.isNotEmpty(hrEmployeeSys.getPositionIds())) {
                List<Integer> myPositionIds = MyStringUtil.getIntList(hrEmployeeSys.getPositionIds());
                for (Integer positionId : myPositionIds) {
                    HrPosition hrPosition = hrPositionMapper.selectByPrimaryKey(positionId);
                    if (hrPosition.getDeptCharge()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public List<HrPosition> listSortedAll() {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<HrPosition> positions=hrPositionMapper.selectAll(params);
        return positions.stream().sorted(Comparator.comparing(HrPosition::getPositionLv)).collect(Collectors.toList());
    }

    public List<Map> getUserTree(int positionId) {
        List<Map> mapList = Lists.newArrayList();
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        List<HrDept> deptList = hrDeptMapper.selectAll(params);
        List<HrEmployeeDto> userList = hrEmployeeMapper.selectAll(params).stream().sorted(Comparator.comparing(HrEmployee::getUserName)).collect(Collectors.toList());
        for (HrDept dept : deptList) {
            Map map = Maps.newHashMap();
            Map stateMap=Maps.newHashMap();
            map.put("id", "dept_" + dept.getId());
            map.put("text", dept.getName());
            if (dept.getParentId() == 0) {
                map.put("parent", "#");
                stateMap.put("opened",true);
            } else {
                map.put("parent", "dept_" + dept.getParentId());
                stateMap.put("opened",false);
            }

            map.put("state",stateMap);
            mapList.add(map);
            List<HrEmployeeDto> employeeDtos=userList.stream().filter(p ->dept.getId().equals(p.getDeptId())).collect(Collectors.toList());
            for (HrEmployeeDto employee : employeeDtos) {
                Map aclMap = Maps.newHashMap();
                aclMap.put("id", employee.getUserLogin());
                aclMap.put("text", employee.getUserName());
                aclMap.put("parent", "dept_" + employee.getDeptId());
                Map aclStateMap = Maps.newHashMap();
                aclStateMap.put("opened",false);
                aclStateMap.put("disabled",false);
                aclStateMap.put("selected", employee.getPositionIds().contains(","+positionId+","));
                aclMap.put("icon","glyphicon glyphicon-user");
                aclMap.put("state", aclStateMap);
                mapList.add(aclMap);
            }
        }
        return mapList;
    }

    public void savePositionUserList(int positionId, String userList) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("positionId", positionId);

        //处理以前的,删除被取消的
        List<HrEmployeeDto> employees=hrEmployeeMapper.selectAll(params);
        List<String> userLoginList= MyStringUtil.getStringList(userList).stream().filter(p->!p.contains("dept_")).collect(Collectors.toList());
        for(HrEmployeeDto hrEmployeeDto:employees){
            if(!userLoginList.contains(hrEmployeeDto.getUserLogin())){
                HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(hrEmployeeDto.getUserLogin());
                List<Integer> positionIds=MyStringUtil.getIntList(hrEmployeeSys.getPositionIds());
                positionIds.remove(positionIds.indexOf(positionId));
                hrEmployeeSys.setPositionIds(","+StringUtils.join(positionIds,",")+",");
                hrEmployeeSys.setPositionNames(","+getPositionNames(positionIds)+",");
                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            }
        }

        //增加
        for(String userLogin:userLoginList) {
            HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(userLogin);
            List<Integer> positionIds=MyStringUtil.getIntList(hrEmployeeSys.getPositionIds());
            if(!positionIds.contains(positionId)){
                positionIds.add(positionId);
                hrEmployeeSys.setPositionIds(","+StringUtils.join(positionIds,",")+",");
                hrEmployeeSys.setPositionNames(","+getPositionNames(positionIds)+",");
                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
            }
        }
    }

    public String getPositionNames(List<Integer> positionIds) {
        List<HrPosition> hrPositions = selectAll();
        return StringUtils.join(hrPositions.stream().filter(p -> positionIds.contains(p.getId())).map(HrPosition::getPositionName).collect(Collectors.toList()), ",");
    }


}
