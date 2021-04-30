package com.cmcu.common.service;

import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dao.CommonMessageMapper;
import com.cmcu.common.entity.CommonMessage;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonMessageService {


    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonMessageMapper commonMessageMapper;

    @Resource
    CommonFileMapper commonFileMapper;


    public PageInfo<CommonMessage> listPagedData(int pageNum, int pageSize, Map<String,Object> params) {
        PageInfo<CommonMessage> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonMessageMapper.selectAll(params));
        return pageInfo;
    }

    public void insert(String tenetId,String sender, List<String> receivers,String msgType, String msgCaption, String msgText, String msgUrl){
        String senderName=commonUserService.getCnNames(sender);
        for(String receiver:receivers) {
            CommonMessage item = new CommonMessage();
            item.setTenetId(tenetId);
            item.setSenderName(senderName);
            item.setSender(sender);
            item.setReceiver(receiver);
            item.setReceiverName(commonUserService.getCnNames(receiver));
            item.setMsgType(msgType);
            item.setMsgCaption(msgCaption);
            item.setMsgText(msgText);
            item.setMsgUrl(msgUrl);
            item.setMsgBtnTxt("详情");
            item.setReceived(false);
            item.setHandled(false);
            item.setDeleted(false);
            item.setGmtCreate(new Date());
            insert(item);
        }
    }

    public int insert(CommonMessage item){
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        commonMessageMapper.insert(item);
        return item.getId();
    }

    public List<CommonMessage> listLastFiveNoReceived(String receiver) {
        List<CommonMessage> list = commonMessageMapper.listLastFiveNoReceived(receiver);
        for (CommonMessage item : list) {
            markReceived(item.getId());
        }
        Integer hour=Integer.parseInt(DateFormatUtils.format(new Date(),"HH"));
        if(hour<8||hour>22) {
            commonMessageMapper.autoCoMarkReceived();
        }
        return list;
    }

    public void markReceived(int id){
        commonMessageMapper.markReceived(id);
    }

    public void markAll(String receiver){
        Date now=new Date();
        Map params= Maps.newHashMap();
        params.put("receiver",receiver);
        params.put("deleted",false);
        params.put("received",false);
        List<CommonMessage> list=commonMessageMapper.selectAll(params);
        for(CommonMessage item:list){
            item.setReceived(true);
            item.setGmtReceived(now);
            commonMessageMapper.updateByPrimaryKey(item);
        }


    }

    public void clearAll(String receiver){
        Map params= Maps.newHashMap();
        params.put("receiver",receiver);
        params.put("deleted",false);
        List<CommonMessage> list=commonMessageMapper.selectAll(params);
        for(CommonMessage item:list){
            item.setDeleted(true);
            commonMessageMapper.updateByPrimaryKey(item);
        }


    }


}
