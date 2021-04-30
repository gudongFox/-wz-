package com.cmcu.common.service;

import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CommonAttachService {

    public static String BASE_PATH="y:\\files";


    @Resource
    public CommonAttachMapper commonAttachMapper;



    public PageInfo<CommonAttach> listPagedData(int pageNum, int pageSize, Map<String,Object> params){
        PageInfo<CommonAttach> pageInfo= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()-> commonAttachMapper.selectAll(params));
        return pageInfo;
    }

    public List<CommonAttach> listLatestData(int size,String creator){
        Map params=Maps.newHashMap();
        params.put("creator",creator);
        params.put("deleted",false);
        PageInfo<CommonAttach> pageInfo=listPagedData(1,size,params);
        return pageInfo.getList();
    }

    public int insert(MultipartFile file,String fileName,String remark,String enLogin) throws IOException {
        Date now = new Date();
        CommonAttach item = new CommonAttach();
        String localPath = getLocalFilePath();
        file.transferTo(new File(localPath));
        String md5= FileUtil.getFileMD5String(localPath);
        item.setMd5(md5);
        item.setCreator(enLogin);
        item.setGmtCreate(now);
        item.setGmtModified(now);
        item.setName(fileName);
        item.setSize(file.getSize());
        item.setLocalPath(localPath);
        item.setSizeName(FileUtil.getFileSizeName(file.getSize()));
        item.setExtensionName(FileUtil.getExtensionName(item.getName()));
        item.setRemark(remark);
        ModelUtil.setNotNullFields(item);
        commonAttachMapper.insert(item);
        return item.getId();
    }

    public CommonAttach getModelById(int id){

        return commonAttachMapper.selectByPrimaryKey(id);
    }

    public CommonAttach selectByMd5(String md5,String enLogin) {
        Map params = Maps.newHashMap();
        params.put("md5", md5);
        if (PageHelper.count(() -> commonAttachMapper.selectAll(params)) == 0) return null;
        for (CommonAttach commonAttach : commonAttachMapper.selectAll(params)) {
            File file = new File(commonAttach.getLocalPath());
            if(file.exists()){
                CommonAttach newItem=new CommonAttach();
                BeanUtils.copyProperties(commonAttach,newItem);
                newItem.setCreator(enLogin);
                if(StringUtils.isNotEmpty(enLogin)) newItem.setCreator(commonAttach.getCreator());
                newItem.setGmtCreate(new Date());
                newItem.setGmtModified(new Date());
                ModelUtil.setNotNullFields(newItem);
                commonAttachMapper.insert(newItem);
                return commonAttach;
            }
        }
        return null;
    }


    public String getMd5ById(int id){
        CommonAttach commonAttach=commonAttachMapper.selectByPrimaryKey(id);
        if(commonAttach!=null) return commonAttach.getMd5();
        return "";
    }

    public  String getLocalFilePath(){
        String dirPath=BASE_PATH+"\\"+DateFormatUtils.format(new Date(),"yyyyMMdd");
        if (!new File(dirPath).exists()) {
            new File(dirPath).mkdirs();
        }
        return dirPath+"\\"+ UUID.randomUUID().toString();
    }
}
