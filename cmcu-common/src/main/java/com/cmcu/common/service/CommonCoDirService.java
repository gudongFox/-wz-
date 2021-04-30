package com.cmcu.common.service;

import com.cmcu.common.dao.CommonDirMapper;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.entity.CommonDir;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommonCoDirService {


    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    CommonBaseService commonBaseService;

    @Resource
    IHandleFormService handleFormService;


    /**
     * 生成、更新协同文件夹
     *
     * @param stepId        183
     * @param buildList     id,name,seq  {364  设计变更  99} {365  1  1} {366  2  2}
     * @param majorNameList 建筑,结构,给排水,电气,暖通
     * @param enLogin       项目负责人
     */
    @Transactional
    public void buildCoDir(int stepId, List<Map> buildList, List<String> majorNameList,List<String> rootNames,String enLogin) {
        String businessKey = "co_" + stepId;
        commonBaseService.clearCache(businessKey, -1);


        String cnName = commonUserService.getCnNames(enLogin);
        Map dirParams = Maps.newHashMap();
        dirParams.put("businessKey", businessKey);
        List<com.cmcu.common.entity.CommonDir> dirList = commonDirMapper.selectAll(dirParams);
        Date now = new Date();

        for (int i = 0; i < rootNames.size(); i++) {
            String rootName = rootNames.get(i);
            Optional<com.cmcu.common.entity.CommonDir> optionalRoot = dirList.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase(rootName)).findAny();
            if (!optionalRoot.isPresent()) {
                CommonDirDto rootDir = new CommonDirDto();
                rootDir.setBusinessKey(businessKey);
                rootDir.setParentId(0);
                rootDir.setCnName(rootName);
                rootDir.setMajorName("");
                rootDir.setBuildId(0);
                rootDir.setSize(0L);
                rootDir.setSizeName("0B");
                rootDir.setSeq(i + 1);
                rootDir.setDeleted(false);
                rootDir.setEditableTag("none");
                rootDir.setCreator(enLogin);
                rootDir.setCreatorName(cnName);
                rootDir.setGmtModified(now);
                rootDir.setGmtCreate(now);
                ModelUtil.setNotNullFields(rootDir);
                commonDirMapper.insert(rootDir);
                dirList.add(rootDir);
            } else {
                CommonDir rootDir = dirList.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase(rootName)).findFirst().get();
                if (rootDir.getDeleted()) {
                    rootDir.setDeleted(false);
                    rootDir.setSeq(i + 1);
                    rootDir.setGmtModified(now);
                    commonDirMapper.updateByPrimaryKey(rootDir);
                }
            }
        }

        int docId = dirList.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase("项目资料")).findFirst().get().getId();
        int workDirId = dirList.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase("作图区")).findFirst().get().getId();
        int publishDirId = dirList.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase("发布区")).findFirst().get().getId();


        int referId = addChildDir(workDirId, "00参照");
        //int referSuccessId = addChildDir(docId, "互提资料(完成)");
        //int validateSuccessId = addChildDir(docId, "设计校审(完成)");

        List<Integer> addAllRootIdList = Lists.newArrayList(referId, workDirId, publishDirId);
        dirList = commonDirMapper.selectAll(dirParams);
        for (Integer parentId : addAllRootIdList) {
            buildMajorDir(parentId, majorNameList, buildList, dirList);
        }
    }

    private int addChildDir(int parentId, String cnName) {
        com.cmcu.common.entity.CommonDir parentDir = commonDirMapper.selectByPrimaryKey(parentId);
        Map params = Maps.newHashMap();
        params.put("parentId", parentId);
        params.put("deleted", false);
        int seq = (int) PageHelper.count(() -> commonDirMapper.selectAll(params)) + 1;

        params.remove("deleted");
        params.put("cnName", cnName);
        Date now = new Date();
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) == 0) {
            com.cmcu.common.entity.CommonDir childDir = new CommonDirDto();
            childDir.setSeq(seq);
            childDir.setDeleted(false);
            childDir.setCnName(cnName);
            childDir.setBusinessKey(parentDir.getBusinessKey());
            childDir.setBuildId(parentDir.getBuildId());
            childDir.setBuildName(parentDir.getBuildName());
            childDir.setParentId(parentId);
            childDir.setSize(0L);
            childDir.setSizeName("0B");
            childDir.setCreator(parentDir.getCreator());
            childDir.setCreatorName(parentDir.getCreatorName());
            childDir.setGmtCreate(now);
            childDir.setGmtModified(now);
            ModelUtil.setNotNullFields(childDir);
            commonDirMapper.insert(childDir);
            return childDir.getId();
        } else {
            com.cmcu.common.entity.CommonDir childDir = commonDirMapper.selectAll(params).get(0);
            if (childDir.getDeleted()) {
                childDir.setDeleted(false);
                childDir.setGmtModified(now);
                commonDirMapper.updateByPrimaryKey(childDir);
            }
            return childDir.getId();
        }
    }

    private void buildMajorDir(int parentId, List<String> majorNameList, List<Map> builds, List<com.cmcu.common.entity.CommonDir> dirList) {
        com.cmcu.common.entity.CommonDir parent = dirList.stream().filter(p -> p.getId().equals(parentId)).findFirst().get();

        dirList.stream()
                .filter(p -> p.getParentId().equals(parentId))
                .filter(p -> !majorNameList.contains(p.getMajorName()) && !p.getCnName().equalsIgnoreCase("00参照"))
                .forEach(p -> deleteRecursiveChildDir(p.getId()));

        int begin = 1;
        for (String majorName : majorNameList) {
            String dirName = String.format("%2d", begin).replace(" ", "0") + majorName;
            Optional<com.cmcu.common.entity.CommonDir> optionalMajorDir = dirList.stream().filter(p -> p.getParentId().equals(parentId))
                    .filter(p -> p.getMajorName().equalsIgnoreCase(majorName)).findFirst();

            com.cmcu.common.entity.CommonDir majorDir;
            if (optionalMajorDir.isPresent()) {
                majorDir = optionalMajorDir.get();
                if (majorDir.getDeleted() || !majorDir.getSeq().equals(begin) || !majorDir.getCnName().equalsIgnoreCase(dirName)) {
                    majorDir.setDeleted(false);
                    majorDir.setSeq(begin);
                    majorDir.setCnName(dirName);
                    commonDirMapper.updateByPrimaryKey(majorDir);
                }
            } else {
                majorDir = new CommonDirDto();
                majorDir.setDeleted(false);
                majorDir.setCnName(dirName);
                majorDir.setBusinessKey(parent.getBusinessKey());
                majorDir.setEditableTag("none");
                majorDir.setBuildId(0);
                majorDir.setMajorName(majorName);
                majorDir.setParentId(parentId);
                majorDir.setSize(0L);
                majorDir.setSizeName("0B");
                majorDir.setCreator(parent.getCreator());
                majorDir.setCreatorName(parent.getCreatorName());
                majorDir.setGmtCreate(new Date());
                majorDir.setGmtModified(new Date());
                majorDir.setSeq(begin);
                ModelUtil.setNotNullFields(majorDir);
                commonDirMapper.insert(majorDir);
                dirList.add(majorDir);
            }
            buildBuildDir(majorDir.getId(), builds, dirList);
            begin++;
        }
    }

    private void buildBuildDir(int parentId, List<Map> builds, List<com.cmcu.common.entity.CommonDir> dirList) {
        com.cmcu.common.entity.CommonDir parent = dirList.stream().filter(p -> p.getId().equals(parentId)).findFirst().get();
        Map params = Maps.newHashMap();
        params.put("parentId", parentId);
        params.put("deleted", false);
        List<Integer> buildIdList = builds.stream().map(p -> Integer.parseInt(p.get("id").toString())).collect(Collectors.toList());
        dirList.stream().filter(p -> p.getParentId().equals(parentId)).filter(p -> !buildIdList.contains(p.getBuildId()))
                .forEach(p -> deleteRecursiveChildDir(p.getId()));

        for (Map build : builds) {
            int id = Integer.parseInt(build.get("id").toString());
            int seq = Integer.parseInt(build.get("seq").toString());
            String name = build.get("name").toString();
            params = Maps.newHashMap();
            params.put("parentId", parentId);
            params.put("buildId", id);
            Optional<com.cmcu.common.entity.CommonDir> optionalBuildDir = dirList.stream().filter(p -> p.getParentId().equals(parentId))
                    .filter(p -> p.getBuildId().equals(id)).findFirst();

            if (optionalBuildDir.isPresent()) {
                com.cmcu.common.entity.CommonDir buildDir = optionalBuildDir.get();
                if (buildDir.getDeleted() || buildDir.getSeq().equals(seq) || buildDir.getCnName().equalsIgnoreCase(name)) {
                    buildDir.setDeleted(false);
                    buildDir.setSeq(seq);
                    buildDir.setCnName(name);
                    buildDir.setBuildName(name);
                    commonDirMapper.updateByPrimaryKey(buildDir);
                }
            } else {
                com.cmcu.common.entity.CommonDir item = new com.cmcu.common.entity.CommonDir();
                item.setSeq(seq);
                item.setDeleted(false);
                item.setCnName(name);
                item.setBuildId(id);
                item.setBuildName(name);
                item.setMajorName(parent.getMajorName());
                item.setParentId(parentId);
                item.setBusinessKey(parent.getBusinessKey());
                item.setEditableTag("none");
                item.setSize(0L);
                item.setSizeName("0B");
                item.setCreator(parent.getCreator());
                item.setCreatorName(parent.getCreatorName());
                item.setGmtCreate(new Date());
                item.setGmtModified(new Date());
                ModelUtil.setNotNullFields(item);
                commonDirMapper.insert(item);
            }

        }
    }

    private void deleteRecursiveChildDir(int id) {
        com.cmcu.common.entity.CommonDir parent = commonDirMapper.selectByPrimaryKey(id);
        if (parent.getSize().equals(0L) && !parent.getDeleted()) {
            parent.setDeleted(true);
            commonDirMapper.updateByPrimaryKey(parent);
            Map params = Maps.newHashMap();
            params.put("parentId", id);
            params.put("deleted", false);
            List<com.cmcu.common.entity.CommonDir> childList = commonDirMapper.selectAll(params);
            for (com.cmcu.common.entity.CommonDir child : childList) {
                deleteRecursiveChildDir(child.getId());
            }
        }
    }

    public int getCoMirrorDir(int dirId, boolean referDir) {
        com.cmcu.common.entity.CommonDir commonDir = commonDirMapper.selectByPrimaryKey(dirId);
        if (commonDir.getBusinessKey().startsWith("co_") && commonDir.getBuildId() > 0 && StringUtils.isNotEmpty(commonDir.getMajorName())) {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("businessKey", commonDir.getBusinessKey());
            List<com.cmcu.common.entity.CommonDir> dirs = commonDirMapper.selectAll(params);

            List<String> dirNames = Lists.newArrayList();
            int parentId = commonDir.getId();
            while (parentId > 0) {
                int tempParentId = parentId;
                com.cmcu.common.entity.CommonDir parentDir = dirs.stream().filter(p -> p.getId().equals(tempParentId)).findFirst().get();
                System.out.println(parentDir.getCnName());
                com.cmcu.common.entity.CommonDir parentParentDir = dirs.stream().filter(p -> p.getId().equals(parentDir.getParentId())).findFirst().get();
                System.out.println(parentParentDir.getCnName());
                if (parentParentDir.getCnName().contains(parentParentDir.getMajorName())) {
                    parentId = 0;
                } else {
                    parentId = parentDir.getParentId();

                    dirNames.add(0, parentDir.getCnName());
                }
            }

            parentId = 0;
            if (referDir) {
                int workSpaceId = dirs.stream().filter(p -> p.getParentId().equals(0)).filter(p -> p.getCnName().equalsIgnoreCase("作图区")).findFirst().get().getId();
                int referId = dirs.stream().filter(p -> p.getParentId().equals(workSpaceId)).filter(p -> p.getCnName().equalsIgnoreCase("00参照")).findFirst().get().getId();
                int majorId = dirs.stream().filter(p -> p.getParentId().equals(referId)).filter(p -> p.getMajorName().equalsIgnoreCase(commonDir.getMajorName())).findFirst().get().getId();
                parentId = dirs.stream().filter(p -> p.getParentId().equals(majorId)).filter(p -> p.getBuildId().equals(commonDir.getBuildId())).findFirst().get().getId();

            } else {
                int workSpaceId = dirs.stream().filter(p -> p.getCnName().equalsIgnoreCase("发布区")).findFirst().get().getId();
                int majorId = dirs.stream().filter(p -> p.getParentId().equals(workSpaceId)).filter(p -> p.getMajorName().equalsIgnoreCase(commonDir.getMajorName())).findFirst().get().getId();
                parentId = dirs.stream().filter(p -> p.getParentId().equals(majorId)).filter(p -> p.getBuildId().equals(commonDir.getBuildId())).findFirst().get().getId();
            }

            boolean needUpdate = false;
            for (String dirName : dirNames) {
                int tempParentId = parentId;
                if (dirs.stream().filter(p -> p.getParentId().equals(tempParentId)).anyMatch(p -> p.getCnName().equalsIgnoreCase(dirName))) {
                    parentId = dirs.stream().filter(p -> p.getParentId().equals(tempParentId)).filter(p -> p.getCnName().equalsIgnoreCase(dirName)).findFirst().get().getId();
                } else {
                    needUpdate = true;
                    com.cmcu.common.entity.CommonDir item = new com.cmcu.common.entity.CommonDir();
                    item.setBuildId(commonDir.getBuildId());
                    item.setBuildName(commonDir.getBuildName());
                    item.setMajorName(commonDir.getMajorName());
                    item.setCnName(dirName);
                    item.setDeleted(false);
                    item.setGmtModified(new Date());
                    item.setGmtCreate(new Date());
                    item.setSeq(1);
                    item.setCreator(commonDir.getCreator());
                    item.setCreatorName(commonDir.getCreatorName());
                    item.setSize(0L);
                    item.setBusinessKey(commonDir.getBusinessKey());
                    item.setEditableTag("creator");
                    item.setParentId(tempParentId);
                    item.setSizeName("0B");
                    ModelUtil.setNotNullFields(item);
                    commonDirMapper.insert(item);
                    parentId = item.getId();
                }
            }

            if (needUpdate) {
                commonBaseService.clearCache(commonDir.getBusinessKey(), -1);
            }

            return parentId;
        }
        return dirId;
    }

}
