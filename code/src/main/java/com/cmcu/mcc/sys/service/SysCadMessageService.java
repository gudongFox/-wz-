package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.sys.dao.SysCadMessageMapper;
import com.cmcu.mcc.sys.entity.SysCadMessage;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class SysCadMessageService {


    @Resource
    SysCadMessageMapper sysCadMessageMapper;


    @Resource
    HrEmployeeMapper hrEmployeeMapper;


    public List<SysCadMessage> listReceiveMessage(String receiver){
        Map params= Maps.newHashMap();
        params.put("receiver",receiver);
        params.put("received",false);
        return sysCadMessageMapper.selectAll(params);
    }



    public void sendMessage(String caption,String content,String sender,String receiver,String type,int delay){
        SysCadMessage model=new SysCadMessage();
        model.setDelaySecond(delay);
        model.setGmtCreate(new Date());
        model.setMessageType(type);
        model.setReceived(false);
        model.setReceiver(receiver);
        model.setReceiverName(hrEmployeeMapper.getNameByUserLogin(receiver));
        model.setSender(sender);
        model.setSenderName(hrEmployeeMapper.getNameByUserLogin(sender));
        model.setMessageCaption(caption);
        model.setMessageContent(content);
        ModelUtil.setNotNullFields(model);
        sysCadMessageMapper.insert(model);
    }

    public void sendMessage(String caption,String content,String sender,String receiver){
        sendMessage(caption,content,sender,receiver,"notice",30);
    }


    public void markReceived(int id) {
        SysCadMessage sysCadMessage=sysCadMessageMapper.selectByPrimaryKey(id);
        sysCadMessage.setReceived(true);
        sysCadMessage.setGmtReceived(new Date());
        sysCadMessageMapper.updateByPrimaryKey(sysCadMessage);
    }


}
