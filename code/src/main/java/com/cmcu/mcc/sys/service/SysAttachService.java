package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.FileUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.sys.dao.SysAttachMapper;
import com.cmcu.mcc.sys.dto.SysAttachDto;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.entity.SysConfig;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/3/5 10:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysAttachService {

    @Resource
    SysAttachMapper sysAttachMapper;

    @Autowired
    SysConfigService sysConfigService;


    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        PageInfo<Object> info = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> sysAttachMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        info.getList().forEach(
                p -> {
                    SysAttach attach = (SysAttach) p;
                    SysAttachDto item = SysAttachDto.adapt(attach);
                    item.setSizeName(FileUtil.getFileSizeName(attach.getSize()));
                    item.setExtensionName(FileUtil.getExtensionName(attach.getName()));
                    list.add(item);
                });
        info.setList(list);
        return info;
    }

    public SysAttachDto getModelById(int id) {
        SysAttach attach = sysAttachMapper.selectByPrimaryKey(id);
        if(attach==null) return null;
        SysAttachDto item=SysAttachDto.adapt(attach);
        item.setSizeName(FileUtil.getFileSizeName(attach.getSize()));
        item.setExtensionName(FileUtil.getExtensionName(attach.getName()));
        return item;
    }

    public SysAttachDto getModelByName(String name) {
        Map map =new HashMap();
        map.put("name",name);
        if(PageHelper.count(()->sysAttachMapper.selectAll(map))>0){
            SysAttach attach = sysAttachMapper.selectAll(map).get(0);
            SysAttachDto item=SysAttachDto.adapt(attach);
            item.setSizeName(FileUtil.getFileSizeName(attach.getSize()));
            item.setExtensionName(FileUtil.getExtensionName(attach.getName()));
            return item;
        }else {
            return null;
        }

    }


    public SysAttach insert(String source,String name,String localPath,String creator){
        //如果存在
        String md5=FileUtil.getFileMD5String(localPath);
        SysAttach pre=sysAttachMapper.selectByMd5(md5);
        if(pre!=null&&pre.getCreator().equalsIgnoreCase(creator)&&pre.getSource().equalsIgnoreCase(source)) {
            if (new File(pre.getLocalPath()).exists()) {
                //删除本次上传的
                new File(localPath).deleteOnExit();
            } else {
                pre.setLocalPath(localPath);
                sysAttachMapper.updateByPrimaryKey(pre);
            }
            return pre;
        }
        Date now=new Date();
        SysAttach sysAttach=new SysAttach();
        sysAttach.setSource(source);
        sysAttach.setName(name);
        sysAttach.setGmtCreate(now);
        sysAttach.setGmtModified(now);
        sysAttach.setLocalPath(localPath);
        sysAttach.setMd5(md5);
        sysAttach.setSize(new File(localPath).length());
        sysAttach.setCreator(creator);
        sysAttachMapper.insert(sysAttach);
        return sysAttach;
    }



    public int getExistFile(String md5) {
        SysAttach sysAttach=sysAttachMapper.selectByMd5(md5);
        if(sysAttach!=null) return sysAttach.getId();
        return 0;
    }

    /**
     * 取得上传文件默认路径
     * @param source
     * @return
     */
    public String getRandomFilePath(String source){
        String directoryPath=getDirectoryPath(source);
        String dirPath= directoryPath+"\\"+ UUID.randomUUID().toString();
        return dirPath;
    }


    /**
     * 取得上传文件路径
     * @param source 来源
     * @return
     */
    private String getDirectoryPath(String source) {
        SysConfig sysConfig=sysConfigService.getConfig();
        StringBuilder sbPath = new StringBuilder();
        sbPath.append(sysConfig.getDirPath());
        sbPath.append("\\");
        sbPath.append(source);
        sbPath.append("\\");
        sbPath.append(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        String directoryPath = sbPath.toString();
        if (!new File(directoryPath).exists()) {
            new File(directoryPath).mkdirs();
        }
        return directoryPath;
    }




}
