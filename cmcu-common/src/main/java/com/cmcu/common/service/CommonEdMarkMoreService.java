package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdMarkMoreMapper;
import com.cmcu.common.dto.CommonEdMarkMoreDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonEdMarkMore;
import com.cmcu.common.util.ModelUtil;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonEdMarkMoreService {


    @Resource
    CommonEdMarkMoreMapper commonEdMarkMoreMapper;


    @Resource
    CommonUserService commonUserService;




    public List<CommonEdMarkMoreDto> listData(int markId, String enLogin){
        List<CommonEdMarkMoreDto> list= Lists.newArrayList();
        Map params= Maps.newHashMap();
        params.put("markId",markId);
        params.put("deleted",false);
        List<CommonEdMarkMore> oList=commonEdMarkMoreMapper.selectAll(params);
        CommonEdMarkMore latest=commonEdMarkMoreMapper.getLatest(markId);
        oList.forEach(p->{
            CommonEdMarkMoreDto dto=CommonEdMarkMoreDto.adapt(p);
            if(latest==null||latest.getId().equals(p.getId())){
                dto.setEditAble(dto.getCreator().equals(enLogin));
            }
        });
        return list;
    }


    public int save(int id,int markId,String commentContent,String enLogin) {
        Date now = new Date();
        if (id == 0) {
            UserDto commonUser = commonUserService.selectByEnLogin(enLogin);
            CommonEdMarkMore item = new CommonEdMarkMore();
            item.setMarkId(markId);
            item.setCommentContent(commentContent);
            item.setGmtModified(now);
            item.setGmtCreate(now);
            item.setDeleted(false);
            item.setCreator(commonUser.getEnLogin());
            item.setCreatorName(commonUser.getCnName());
            ModelUtil.setNotNullFields(item);
            commonEdMarkMoreMapper.insert(item);
            return item.getId();
        }

        CommonEdMarkMore item = commonEdMarkMoreMapper.selectByPrimaryKey(id);
        if (!item.getCommentContent().equalsIgnoreCase(commentContent)) {

            CommonEdMarkMore latest=commonEdMarkMoreMapper.getLatest(item.getMarkId());
            Assert.state(latest==null||latest.getId().equals(item.getId()),"当前不是最后记录,无法修改!");
            Assert.state(item.getCreator().equalsIgnoreCase(enLogin), "您不是创建人" + item.getCreatorName() + ",无法修改!");
            item.setCommentContent(commentContent);
            item.setGmtModified(now);
            ModelUtil.setNotNullFields(item);
            commonEdMarkMoreMapper.updateByPrimaryKey(item);
        }
        return id;
    }


    public void remove(int id,String enLogin) {
        CommonEdMarkMore item = commonEdMarkMoreMapper.selectByPrimaryKey(id);
        CommonEdMarkMore latest = commonEdMarkMoreMapper.getLatest(item.getMarkId());
        Assert.state(latest == null || latest.getId().equals(item.getId()), "当前不是最后记录,无法修改!");
        Assert.state(item.getCreator().equalsIgnoreCase(enLogin), "您不是创建人" + item.getCreatorName() + ",无法修改!");
        item.setDeleted(true);
        item.setGmtModified(new Date());
        commonEdMarkMoreMapper.updateByPrimaryKey(item);
    }


    public CommonEdMarkMoreDto getModelById(int id,String enLogin){
        CommonEdMarkMore item = commonEdMarkMoreMapper.selectByPrimaryKey(id);
        CommonEdMarkMoreDto dto=CommonEdMarkMoreDto.adapt(item);
        if(dto.getCreator().equalsIgnoreCase(enLogin)) {
            CommonEdMarkMore latest = commonEdMarkMoreMapper.getLatest(item.getMarkId());
            dto.setEditAble(latest==null||item.getId().equals(latest.getId()));
        }
        return dto;
    }


}
