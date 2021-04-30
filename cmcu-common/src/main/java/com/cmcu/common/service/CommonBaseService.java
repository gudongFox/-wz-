package com.cmcu.common.service;

import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.dao.CommonDirMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.entity.CommonDir;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class CommonBaseService {

    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonFileMapper commonFileMapper;

    @Resource
    CommonAttachMapper commonAttachMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    TaskExecutor taskExecutor;

    public final String CACHE_COMMON_DIR = "_cache_dir_businessKey_";

    public final String CACHE_COMMON_FILE = "_cache_file_businessKey_";


    public void clearCache(String businessKey,int dirId) {
        clearFileCache(businessKey);
        clearDirCache(businessKey);
        if (dirId > 0) {
            recursiveDirSizeBottomToTop(businessKey, dirId);
        }
    }

    public void clearFileCache(String businessKey) {
        guavaCacheService.invalidate(CACHE_COMMON_FILE + businessKey);
    }

    public void clearDirCache(String businessKey) {
        guavaCacheService.invalidate(CACHE_COMMON_DIR + businessKey);
    }


    public void recursiveDirSizeBottomToTop(String businessKey, int dirId) {
        if (dirId > 0) {
            long size=commonFileMapper.selectSizeByBusinessKey(businessKey,dirId);
            long childDirSize=commonDirMapper.selectChildSizeByBusinessKey(businessKey,dirId);
            CommonDir dir = commonDirMapper.selectByPrimaryKey(dirId);
            if (!dir.getSize().equals(size+childDirSize)) {
                dir.setSize(size + childDirSize);
                dir.setSizeName(FileUtil.getFileSizeName(dir.getSize()));
                dir.setGmtModified(new Date());
                commonDirMapper.updateByPrimaryKey(dir);
                recursiveDirSizeBottomToTop(businessKey, dir.getParentId());
            }
        }
    }

    /**
     * 得到文件所属文件夹
     * @param businessKey
     * @param parentId
     * @param webkitRelativePath
     * @param enLogin
     * @return
     */
    public synchronized int getTargetDirId(String businessKey,int parentId,String webkitRelativePath,String enLogin){
        String cnName=commonUserService.getCnNames(enLogin);
        int targetId=parentId;
        try {
            String[] dirs = StringUtils.split(webkitRelativePath, "/");
            if (dirs.length > 1) {
                for (int i = 0; i <= dirs.length - 2; i++) {
                    Map params = Maps.newHashMap();
                    params.put("parentId", targetId);
                    params.put("cnName", dirs[i]);
                    params.put("businessKey",businessKey);
                    if (PageHelper.count(() -> commonDirMapper.selectAll(params)) > 0) {
                        CommonDir cpDir = commonDirMapper.selectAll(params).get(0);
                        if (cpDir.getDeleted()) {
                            cpDir.setDeleted(false);
                            cpDir.setGmtModified(new Date());
                            cpDir.setCreator(enLogin);
                            cpDir.setCreatorName(cnName);
                            commonDirMapper.updateByPrimaryKey(cpDir);
                        }
                        targetId = cpDir.getId();
                    } else {

                        CommonDir newDir = new CommonDir();
                        newDir.setBusinessKey(businessKey);
                        newDir.setParentId(targetId);
                        newDir.setCnName(dirs[i]);
                        newDir.setSize(0L);
                        newDir.setSeq(1);
                        newDir.setCreator(enLogin);
                        newDir.setCreatorName(cnName);
                        newDir.setDeleted(false);
                        newDir.setGmtModified(new Date());
                        newDir.setGmtCreate(new Date());
                        newDir.setEditableTag("creator");
                        if(targetId>0) {
                            CommonDir parentDir = commonDirMapper.selectByPrimaryKey(targetId);
                            newDir.setMajorName(parentDir.getMajorName());
                            newDir.setBuildId(parentDir.getBuildId());
                        }
                        ModelUtil.setNotNullFields(newDir);
                        commonDirMapper.insert(newDir);
                        targetId = newDir.getId();
                    }
                }
            }
        }catch (Exception ex){

        }
        guavaCacheService.invalidate(CACHE_COMMON_DIR + businessKey);
        return targetId;
    }



}
