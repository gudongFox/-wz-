package com.cmcu.common.service;

import com.cmcu.common.dao.CommonDirMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonFileCoService {

    @Resource
    CommonFileMapper commonFileMapper;

    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    IHandleFormService handleFormService;

    @Resource
    CommonFileService commonFileService;

    @Resource
    CommonDirService commonDirService;

    @Resource
    CommonBaseService commonBaseService;

    public List<Map> listBreadcrumb(String businessKey,int coParentId,String enLogin) {
        List<Map> breadcrumbs = Lists.newArrayList();
        //需要依次检查文件夹是否存在
        List<CommonDirDto> rootDirList = listSelectDir(businessKey, 0, enLogin);

        int  parentId=coParentId;
        while (parentId>0) {
            com.cmcu.common.entity.CommonDir parentDir = commonDirMapper.selectByPrimaryKey(parentId);
            Map bread=Maps.newHashMap();
            bread.put("text",parentDir.getCnName());
            bread.put("id",parentDir.getId());
            breadcrumbs.add(0,bread);
            if (rootDirList.stream().anyMatch(p -> p.getId().equals(parentDir.getId()))) {
                parentId = 0;
            } else {
                parentId = parentDir.getParentId();
            }
        }
        Map rootBread=Maps.newHashMap();
        rootBread.put("text","根目录");
        rootBread.put("id",0);
        breadcrumbs.add(0,rootBread);
        return breadcrumbs;
    }



    /**
     * 选择协同文件
     * @param businessKey 表单
     * @param coParentId 父节点 为0的时候为虚拟的
     * @param enLogin 用户名
     * @return
     */
    public List<CommonFileDto> listSelectFile(String businessKey, int coParentId, String enLogin) {

        TplConfigDto tplConfigDto = handleFormService.getTplConfigDto("", businessKey, enLogin);
        String selectCoFileType = tplConfigDto.getSelectCoFileType();

        List<CommonFileDto> list = Lists.newArrayList();
        if (coParentId == 0) {
            List<String> majorNameList = MyStringUtil.getStringList(tplConfigDto.getMajorNames());
            List<Integer> buildIdList = MyStringUtil.getIntList(tplConfigDto.getBuildIds());
            int stepId = tplConfigDto.getStepId();
            if (stepId > 0 && majorNameList.size() == 1 && buildIdList.size() == 1) {

                com.cmcu.common.entity.CommonDir rootDir = selectCoFileType.contains("发布") ? getPublishDir(stepId) : getReferDir(stepId);
                if (rootDir != null) {
                    Map majorParams = Maps.newHashMap();
                    majorParams.put("parentId", rootDir.getId());
                    majorParams.put("deleted", false);
                    List<com.cmcu.common.entity.CommonDir> majorDirs = commonDirMapper.selectAll(majorParams);
                    com.cmcu.common.entity.CommonDir majorDir = majorDirs.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorNameList.get(0))).findAny().get();
                    Map buildParams = Maps.newHashMap();
                    buildParams.put("parentId", majorDir.getId());
                    buildParams.put("deleted", false);
                    com.cmcu.common.entity.CommonDir buildIr = commonDirMapper.selectAll(buildParams).stream().filter(p -> p.getBuildId().equals(buildIdList.get(0))).findAny().get();
                    coParentId = buildIr.getId();
                }

            }
        }

        if (coParentId > 0) {
            com.cmcu.common.entity.CommonDir dir = commonDirMapper.selectByPrimaryKey(coParentId);
            Map params = Maps.newHashMap();
            params.put("dirId", coParentId);
            params.put("deleted", false);
            list.addAll(commonFileService.listData(dir.getBusinessKey(), dir.getId(), enLogin));


            Map destParams = Maps.newHashMap();
            destParams.put("deleted", false);
            destParams.put("businessKey", businessKey);
            destParams.put("cnName", dir.getCnName());
            destParams.put("majorName", dir.getMajorName());
            destParams.put("buildId", dir.getBuildId());
            if (PageHelper.count(() -> commonDirMapper.selectAll(destParams)) > 0) {
                com.cmcu.common.entity.CommonDir destDir = commonDirMapper.selectAll(destParams).get(0);
                List<CommonFileDto> existFiles = commonFileService.listData(businessKey, destDir.getId(), "");
                list.forEach(p -> p.setChecked(existFiles.stream().anyMatch(e -> e.getFileName().equalsIgnoreCase(p.getFileName()))));
            }
        }
        return list;
    }


    /**
     * 选择协同文件
     * @param businessKey 表单
     * @param coParentId 父节点 为0的时候为虚拟的
     * @param enLogin 用户名
     * @return
     */
    public List<CommonDirDto> listSelectDir(String businessKey, int coParentId, String enLogin) {
        TplConfigDto tplConfigDto = handleFormService.getTplConfigDto("", businessKey, enLogin);
        String selectCoFileType=tplConfigDto.getSelectCoFileType();
        List<CommonDirDto> list = Lists.newArrayList();
        List<String> majorNameList = MyStringUtil.getStringList(tplConfigDto.getMajorNames());
        List<Integer> buildIdList = MyStringUtil.getIntList(tplConfigDto.getBuildIds());
        int stepId = tplConfigDto.getStepId();
        if (stepId == 0) {
            return list;
        }

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        if (coParentId == 0) {
            com.cmcu.common.entity.CommonDir rootDir = selectCoFileType.contains("发布") ? getPublishDir(stepId) : getReferDir(stepId);
            if (rootDir != null) {
                Map majorParams = Maps.newHashMap();
                majorParams.put("parentId", rootDir.getId());
                majorParams.put("deleted", false);
                List<CommonDirDto> majorDirs =Lists.newArrayList();
                commonDirMapper.selectAll(majorParams).forEach(p-> majorDirs.add(CommonDirDto.adapt(p)));
                if (majorNameList.size() == 1) {
                    com.cmcu.common.entity.CommonDir majorDir = majorDirs.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorNameList.get(0))).findAny().get();
                    List<CommonDirDto> buildList =commonDirService.listData(majorDir.getBusinessKey(),majorDir.getId(),enLogin);
                    if (buildIdList.size() == 0) {
                        list = buildList;
                    } else {
                        list = buildList.stream().filter(p -> buildIdList.contains(p.getBuildId())).collect(Collectors.toList());
                    }
                } else {
                    list = majorDirs.stream().filter(p -> majorNameList.contains(p.getMajorName())).collect(Collectors.toList());
                }
            }

        } else {
            params.put("parentId", coParentId);
            List<CommonDirDto> childList =commonDirService.listData("",coParentId,enLogin);
            if (buildIdList.size() == 0) {
                list=childList;
            }
           else {
                list = childList.stream().filter(p -> buildIdList.contains(p.getBuildId())).collect(Collectors.toList());
            }
        }

        list.forEach(p-> p.setChecked(recursiveGetDirChecked(businessKey,p.getId())));

        return list;
    }


    /**
     * 确定选择文件
     * @param businessKey 表单businessKey
     * @param coParentId 表单的文件夹Id 根节点为0(虚拟)，其他则根据用户所选
     * @param coDirIds
     * @param coFileIds
     * @param enLogin
     */
    public void insertSelect(String businessKey, int coParentId, String coDirIds, String coFileIds, String enLogin) {
        String cnName = commonUserService.getCnNames(enLogin);

        TplConfigDto tplConfigDto = handleFormService.getTplConfigDto("", businessKey, enLogin);
        List<Integer> buildIdList = MyStringUtil.getIntList(tplConfigDto.getBuildIds());

        List<Integer> fileIdList = MyStringUtil.getIntList(coFileIds);
        List<Integer> dirIdList = MyStringUtil.getIntList(coDirIds);

        if(dirIdList.size()>0||fileIdList.size()>0) {

            int parentId=0;
            Date now=new Date();
            if(coParentId>0) {
                //需要依次检查文件夹是否存在
                List<CommonDirDto> rootDirList = listSelectDir(businessKey, 0, enLogin);

                List<String> dirNames=Lists.newArrayList();
                List<Integer> dirIds=Lists.newArrayList();
                parentId=coParentId;
                while (parentId>0) {
                    com.cmcu.common.entity.CommonDir parentDir = commonDirMapper.selectByPrimaryKey(parentId);
                    dirNames.add(0, parentDir.getCnName());
                    dirIds.add(0, parentDir.getId());

                    if (rootDirList.stream().anyMatch(p -> p.getId().equals(parentDir.getId()))) {
                        parentId = 0;
                    } else {
                        parentId = parentDir.getParentId();
                    }
                }

                parentId=0;
                for(int i=0;i<dirNames.size();i++){
                    String dirName=dirNames.get(i);
                    int dirId=dirIds.get(i);
                    com.cmcu.common.entity.CommonDir existDir = commonDirMapper.selectByCnName(businessKey, parentId, dirName);
                    if(existDir!=null){
                        if (existDir.getDeleted()) {
                            existDir.setDeleted(false);
                            existDir.setGmtModified(now);
                            commonDirMapper.updateByPrimaryKey(existDir);
                        }
                        parentId = existDir.getId();
                    }else{
                        com.cmcu.common.entity.CommonDir dir=commonDirMapper.selectByPrimaryKey(dirId);
                        com.cmcu.common.entity.CommonDir newDir = new com.cmcu.common.entity.CommonDir();
                        BeanUtils.copyProperties(dir, newDir);
                        newDir.setParentId(parentId);
                        newDir.setBusinessKey(businessKey);
                        newDir.setCreator(enLogin);
                        newDir.setCreatorName(cnName);
                        newDir.setGmtCreate(now);
                        newDir.setGmtModified(now);
                        ModelUtil.setNotNullFields(newDir);
                        commonDirMapper.insert(newDir);
                        parentId=newDir.getId();
                    }
                }
            }



            for (Integer selectFileId : fileIdList) {
                saveSelectFile(businessKey, parentId, selectFileId, enLogin, cnName);
            }
            if (dirIdList.size() > 0) {
                for (Integer selectDirId : dirIdList) {
                    saveSelectDir(businessKey, buildIdList, parentId, selectDirId, enLogin, cnName);
                }
            }

            commonBaseService.clearCache(businessKey,parentId);
        }
    }



    private void saveSelectDir(String businessKey, List<Integer> buildIdList, int parentId, int selectDirId, String enLogin, String cnName) {
        Date now = new Date();
        com.cmcu.common.entity.CommonDir dir = commonDirMapper.selectByPrimaryKey(selectDirId);
        com.cmcu.common.entity.CommonDir preDir = commonDirMapper.selectByCnName(businessKey, parentId, dir.getCnName());
        int newParentDirId = 0;
        if (preDir != null) {
            if (preDir.getDeleted()) {
                preDir.setDeleted(false);
                preDir.setGmtModified(now);
                commonDirMapper.updateByPrimaryKey(preDir);
            }
            newParentDirId = preDir.getId();
        } else {
            com.cmcu.common.entity.CommonDir newDir = new com.cmcu.common.entity.CommonDir();
            BeanUtils.copyProperties(dir, newDir);
            newDir.setParentId(parentId);
            newDir.setBusinessKey(businessKey);
            newDir.setCreator(enLogin);
            newDir.setCreatorName(cnName);
            newDir.setGmtCreate(now);
            newDir.setGmtModified(now);
            ModelUtil.setNotNullFields(newDir);
            commonDirMapper.insert(newDir);
            newParentDirId = newDir.getId();
        }
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("dirId", selectDirId);
        params.put("businessKey", dir.getBusinessKey());

        List<CommonFile> coFiles = commonFileMapper.selectAll(params);
        for (CommonFile file : coFiles) {
            saveSelectFile(businessKey, newParentDirId, file.getId(), enLogin, cnName);
        }
        params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("parentId", dir.getId());
        params.put("businessKey", dir.getBusinessKey());
        List<com.cmcu.common.entity.CommonDir> childDirs = commonDirMapper.selectAll(params);
        if (buildIdList.size() > 0) {
            childDirs = childDirs.stream().filter(p -> buildIdList.contains(p.getBuildId())).collect(Collectors.toList());
        }
        for (com.cmcu.common.entity.CommonDir childDir : childDirs) {
            saveSelectDir(businessKey, buildIdList, newParentDirId, childDir.getId(), enLogin, cnName);
        }
        commonBaseService.clearCache(businessKey,newParentDirId);
    }

    private void saveSelectFile(String businessKey, int parentId, int selectFileId, String enLogin, String cnName) {
        Date now = new Date();
        CommonFile file = commonFileMapper.selectByPrimaryKey(selectFileId);
        CommonFile preFile = commonFileMapper.selectByFileName(businessKey, parentId, file.getFileName());
        if (preFile != null) {
            if (preFile.getDeleted() || !preFile.getAttachId().equals(file.getAttachId())) {
                preFile.setDeleted(false);
                preFile.setAttachId(file.getAttachId());
                preFile.setSourceId(file.getId());
                List<Integer> attachIdList = MyStringUtil.getIntList(preFile.getAttachIdList());
                attachIdList.add(file.getAttachId());
                preFile.setAttachIdList(StringUtils.join(attachIdList, ","));
                preFile.setGmtModified(now);
                commonFileMapper.updateByPrimaryKey(preFile);
            }
        } else {
            CommonFile newFile = new CommonFile();
            BeanUtils.copyProperties(file, newFile);
            newFile.setBusinessKey(businessKey);
            newFile.setDirId(parentId);
            newFile.setLocked(true);
            newFile.setDeleted(false);
            newFile.setGmtCreate(now);
            newFile.setGmtModified(now);
            newFile.setLockedLogin(enLogin);
            newFile.setLockedName(cnName);
            newFile.setCreator(enLogin);
            newFile.setCreatorName(cnName);
            newFile.setSourceId(file.getId());
            newFile.setExtractId(0);
            ModelUtil.setNotNullFields(newFile);
            commonFileMapper.insert(newFile);
        }
    }

    /**
     * 判断文件夹是否选中状态
     * @param businessKey 表单BusinessKey
     * @param coDirId  协同文件夹Id
     * @return
     */
    private  boolean recursiveGetDirChecked(String businessKey,int coDirId) {
        com.cmcu.common.entity.CommonDir coDir = commonDirMapper.selectByPrimaryKey(coDirId);

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", businessKey);
        params.put("cnName", coDir.getCnName());
        params.put("majorName", coDir.getMajorName());
        params.put("buildId", coDir.getBuildId());
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) > 0) {
            List<CommonFileDto> coFiles = commonFileService.listData(coDir.getBusinessKey(), coDir.getId(), "");
            if (coFiles.size() > 0) {
                com.cmcu.common.entity.CommonDir destDir = commonDirMapper.selectAll(params).get(0);
                List<CommonFileDto> existFiles = commonFileService.listData(destDir.getBusinessKey(), destDir.getId(), "");
                List<String> existFileNames = existFiles.stream().map(CommonFile::getFileName).distinct().collect(Collectors.toList());
                //存在不满足的则直接返回false
                if (coFiles.stream().anyMatch(p -> !existFileNames.contains(p.getFileName()))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        List<CommonDirDto> childDirs = commonDirService.listData(businessKey, coDirId, "");
        for (CommonDirDto childDir : childDirs) {
            if (!recursiveGetDirChecked(businessKey, childDir.getId())) {
                return false;
            }
        }
        return true;
    }



    private com.cmcu.common.entity.CommonDir getReferDir(int stepId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", "co_" + stepId);
        params.put("parentId", 0);
        params.put("cnName", "作图区");
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) > 0) {
            com.cmcu.common.entity.CommonDir drawDir = commonDirMapper.selectAll(params).get(0);
            Map  referParams = Maps.newHashMap();
            referParams.put("deleted", false);
            referParams.put("businessKey", "co_" + stepId);
            referParams.put("parentId", drawDir.getId());
            referParams.put("cnName", "00参照");
            if (PageHelper.count(() -> commonDirMapper.selectAll(referParams)) > 0) {
                return commonDirMapper.selectAll(referParams).get(0);
            }
        }
        return null;
    }


    private com.cmcu.common.entity.CommonDir getPublishDir(int stepId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", "co_" + stepId);
        params.put("parentId", 0);
        params.put("cnName", "发布区");
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) > 0) {
            return commonDirMapper.selectAll(params).get(0);
        }
        return null;
    }

}
