package com.cmcu.common.service;

import com.cmcu.common.dao.CommonWxMessageMapper;
import com.cmcu.common.dto.CommonWxMessageDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonWxMessage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CommonWxMessageService {
    @Resource
    CommonWxMessageMapper commonWxMessageMapper;
    @Resource
    CommonUserService commonUserService;

    public PageInfo<Object> listPagedData(Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect", true);
        if (params.get("userName")!=null){
            params.put("toUser", commonUserService.selectByName("",params.get("userName").toString()).getWxId());//将查询的名字转换为微信Id
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonWxMessageMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
       pageInfo.getList().forEach(p->{
           list.add(getDto((CommonWxMessage) p));
       });
        pageInfo.setList(list);
        return pageInfo;
    }

    public CommonWxMessage getModelById(int id) {
      return getDto(commonWxMessageMapper.selectByPrimaryKey(id));
    }


    public CommonWxMessageDto getDto(CommonWxMessage item){
        CommonWxMessageDto dto = CommonWxMessageDto.adapt(item);
        String toUserName="";
        if (dto.getToUser()!=null&&dto.getToUser()!=""){
            for (String wxId:dto.getToUser().split("\\|")){
                UserDto userDto = commonUserService.getUserByWxId("wuzhou", wxId);
                if (userDto!=null){
                    toUserName+=userDto.getCnName()+"|";
                }
            }
        }

        dto.setToUserName(toUserName);

        return dto;
    }

}
