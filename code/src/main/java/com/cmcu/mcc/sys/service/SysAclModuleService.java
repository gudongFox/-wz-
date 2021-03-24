package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.sys.dao.SysAclModuleMapper;
import com.cmcu.mcc.sys.dto.SysAclModuleDto;
import com.cmcu.mcc.sys.entity.SysAclModule;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/26 16:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysAclModuleService {

    @Resource
    SysAclModuleMapper sysAclModuleMapper;


    public void insert(SysAclModule item){
        SysAclModule model=new SysAclModule();
        model.setCode(item.getCode());
        model.setName(item.getName());
        model.setParentId(item.getParentId());
        model.setSeq(item.getSeq());
        model.setDeleted(item.getDeleted());
        model.setRemark(item.getRemark());
        model.setTop(item.getTop());
        model.setLeft(item.getLeft());
        model.setIcon(item.getIcon());
        ModelUtil.setNotNullFields(model);
        BeanValidator.check(model);
        sysAclModuleMapper.insert(model);
    }

    public void update(SysAclModule item){
     //   BeanValidator.check(item);
        sysAclModuleMapper.updateByPrimaryKey(item);
        if(item.getDeleted()){
            deleteChild(item.getId());
        }
    }

    private void deleteChild(int parentId){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("parentId",parentId);
        List<SysAclModule> childList=sysAclModuleMapper.selectAll(params);
        childList.forEach(p->{
            p.setDeleted(true);
            sysAclModuleMapper.updateByPrimaryKey(p);
            deleteChild(p.getId());
        });

    }

    public SysAclModuleDto getModelById(int id) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        SysAclModuleDto dto = SysAclModuleDto.adapt(sysAclModule);
        if (sysAclModule.getParentId() > 0) {
            SysAclModule parent = sysAclModuleMapper.selectByPrimaryKey(sysAclModule.getParentId());
            dto.setParentName(parent.getName());
        }
        return dto;
    }

    public SysAclModuleDto getNewModel(int parentId){
        Map<String,Object> deptParam=Maps.newHashMap();
        deptParam.put("isDeleted",false);
        deptParam.put("parentId",parentId);
        long total= PageHelper.count(()->sysAclModuleMapper.selectAll(deptParam));
        SysAclModuleDto dto=new SysAclModuleDto();
        dto.setParentId(parentId);
        dto.setDeleted(false);
        dto.setSeq((int) (total+1));
        dto.setCode("");
        dto.setName("");
        dto.setIcon("fa fa-desktop");
        dto.setTop(false);
        dto.setLeft(false);
        if (parentId > 0) {
            SysAclModule parent = sysAclModuleMapper.selectByPrimaryKey(parentId);
            dto.setParentName(parent.getName());
        }
        return dto;
    }


    /**
     * 得到排序的权限模块列表
     * @return
     */
    public List<SysAclModule> listSortedAll(Integer excludeId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleted", false);
        List<SysAclModule> list = sysAclModuleMapper.selectAll(params);
        List<SysAclModule> sortedList = list.stream().sorted(Comparator.comparing(SysAclModule::getSeq)).collect(Collectors.toList());
        if(excludeId==null) return sortedList;
        return excludeWithChild(sortedList,excludeId);
    }



    private List<SysAclModule> excludeWithChild(List<SysAclModule> sortedList,int excludeId){
        for(int i=0;i<20;i++) {
            int count = 0;
            for (int j = sortedList.size() - 1; j >= 0; j--) {
                SysAclModule item = sortedList.get(j);
                if (item.getId().equals(excludeId) || item.getParentId().equals(excludeId)) {
                    sortedList.remove(item);
                    count++;
                } else if(item.getParentId()>0){
                    if(sortedList.stream().filter(p->p.getId().equals(item.getParentId())).count()==0){
                        sortedList.remove(item);
                        count++;
                    }
                }
            }
            if(count==0){
                break;
            }
        }
        return sortedList;
    }

    public List<Integer> getTreeIds(int rootId) {
        List<Integer> ids = Lists.newArrayList();
        List<SysAclModule> all = listSortedAll(null);
        replenishChildIds(rootId,all,ids);
        return ids;
    }

    private void replenishChildIds(int rootId,List<SysAclModule> all,List<Integer> ids) {
        ids.add(rootId);
        List<SysAclModule> childList = all.stream().filter(p -> p.getParentId().equals(rootId)).collect(Collectors.toList());
        for (SysAclModule child : childList) {
            replenishChildIds(child.getId(), all, ids);
        }
    }


}
