package com.cmcu.common.service;

import com.cmcu.common.dao.CommonDirMapper;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.dto.UserDto;

import com.cmcu.common.entity.CommonDir;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.util.*;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonDirService {




    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonFileMapper commonFileMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    CommonBaseService commonBaseService;

    public List<CommonDirDto> listData(String businessKey, int parentId, String enLogin) {
        List<CommonDirDto> all = guavaCacheService.get(commonBaseService.CACHE_COMMON_DIR + businessKey, () -> {
            List<CommonDirDto> dirs=Lists.newArrayList();

            Map params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("businessKey", businessKey);


            List<CommonDir> list = commonDirMapper.selectAll(params);
            list.forEach(p-> dirs.add(CommonDirDto.adapt(p)));

            dirs.forEach(p -> {
                p.setHaveChild(dirs.stream().anyMatch(n->n.getParentId().equals(p.getId())));
                p.setRemoveAble(false);
                p.setUploadAble(false);

                List<Map> breadcrumbs = Lists.newArrayList();
                recursiveBreadcrumb(p.getId(), breadcrumbs, dirs);
                StringBuilder sb = new StringBuilder();
                for (Map dirNode : breadcrumbs) {
                    if ((int) dirNode.get("id") > 0) {
                        sb.append(dirNode.get("path").toString());
                        sb.append("\\");
                    }
                }
                p.setDirPath(sb.toString());

                if (businessKey.startsWith("co_")) {

                    if (p.getDirPath().contains("\\互提资料(完成)\\") || p.getDirPath().contains("\\设计校审(完成)\\")) {

                    } else if (p.getCnName().equalsIgnoreCase("项目资料") && p.getParentId().equals(0)) {
                        p.setUploadAble(true);
                    } else if (p.getEditableTag().equalsIgnoreCase("creator")) {
                        p.setRemoveAble(true);
                        p.setUploadAble(true);
                    } else if (p.getBuildId() > 0 && StringUtils.isNotEmpty(p.getMajorName()) && p.getDirPath().startsWith("作图区\\")) {
                        p.setUploadAble(true);
                    }
                } else if (p.getEditableTag().equalsIgnoreCase("creator")) {
                    p.setRemoveAble(true);
                    p.setUploadAble(true);
                }
            });
            return Optional.ofNullable(dirs.stream().sorted(Comparator.comparing(CommonDir::getCnName)).collect(Collectors.toList()));
        });
        if (StringUtils.isNotEmpty(enLogin)) {
            UserDto userDto = commonUserService.selectByEnLogin(enLogin);
            if (userDto != null && StringUtils.isNotEmpty(userDto.getOrderMethod())) {

                switch (userDto.getOrderMethod()) {
                    case "create asc":
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getGmtCreate)).collect(Collectors.toList());
                        break;
                    case "create desc":
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getGmtCreate).reversed()).collect(Collectors.toList());
                        break;
                    case "modify desc":
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getGmtModified).reversed()).collect(Collectors.toList());
                        break;
                    case "modify asc":
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getGmtModified)).collect(Collectors.toList());
                        break;
                    case "seq":
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getSeq)).collect(Collectors.toList());
                        break;
                    default:
                        all = all.stream().sorted(Comparator.comparing(CommonDir::getCnName)).collect(Collectors.toList());
                        break;
                }
            }
        }
        if (parentId < 0) return all;
        else {
            List<CommonDirDto> list = all.stream().filter(p -> p.getParentId().equals(parentId)).collect(Collectors.toList());
            if (businessKey.startsWith("co_") && list.size() > 0) {
                if (list.get(0).getEditableTag().equalsIgnoreCase("none")) {
                    list = list.stream().sorted(Comparator.comparing(CommonDir::getSeq)).collect(Collectors.toList());
                }
            }
            return list;
        }
    }

    public CommonDirDto getModelById(int id) {
        CommonDir commonDir = commonDirMapper.selectByPrimaryKey(id);
        return listData(commonDir.getBusinessKey(), commonDir.getParentId(), "").stream().filter(p -> p.getId().equals(id)).findFirst().get();
    }

    @Transactional
    public void remove(int id,String enLogin){
        removeMore("dir_"+id,enLogin);
    }

    @Transactional
    public void removeMore(String ids,String enLogin){
        List<CommonFile> files = Lists.newArrayList();
        List<CommonDir> dirs = Lists.newArrayList();
        for (String id : MyStringUtil.getStringList(ids)) {
            Integer id_ = Integer.parseInt(StringUtils.split(id, "_")[1]);
            if (id.contains("file_")) {
                files.add(commonFileMapper.selectByPrimaryKey(id_));
            } else {
                dirs.add(commonDirMapper.selectByPrimaryKey(id_));
            }
        }

        String businessKey="";
        int parentId=0;
        if(files.size()>0) businessKey=files.get(0).getBusinessKey();
        if(dirs.size()>0){
            businessKey=dirs.get(0).getBusinessKey();
            parentId=dirs.get(0).getParentId();
        }


        for(CommonFile file:files){
            Assert.state(file.getLockedLogin().equalsIgnoreCase(enLogin),file.getFileName()+"属于用户"+file.getLockedName());
            file.setDeleted(true);
            file.setGmtModified(new Date());
            commonFileMapper.updateByPrimaryKey(file);
        }
        for(CommonDir dir:dirs) {
            if(dir.getBusinessKey().startsWith("co_")) {
                Assert.state(dir.getEditableTag().equalsIgnoreCase("creator"), dir.getCnName() + "不可以删除!");
            }
            Assert.state(dir.getCreator().equalsIgnoreCase(enLogin), dir.getCnName()+"属于用户"+dir.getCreatorName());
            dir.setDeleted(true);
            dir.setGmtModified(new Date());
            recursiveRemove(dir, enLogin);
            commonDirMapper.updateByPrimaryKey(dir);
        }
        commonBaseService.clearCache(businessKey,parentId);
    }

    private void recursiveRemove(CommonDir parentDir, String enLogin){

        Map params=Maps.newHashMap();
        params.put("dirId",parentDir.getId());
        params.put("businessKey",parentDir.getBusinessKey());
        params.put("deleted",false);
        List<CommonFile> files=commonFileMapper.selectAll(params);
        files.forEach(p->{
            Assert.state(p.getLockedLogin().equalsIgnoreCase(enLogin)||!p.getLocked(),"文件"+p.getFileName()+"属于用户"+p.getLockedName());
            p.setDeleted(true);
            p.setGmtModified(new Date());
            commonFileMapper.updateByPrimaryKey(p);
        });


        params.remove("dirId");
        params.put("parentId",parentDir.getId());
        List<CommonDir> childDirs=commonDirMapper.selectAll(params);
        childDirs.forEach(p->{
            Assert.state(p.getCreator().equalsIgnoreCase(enLogin),"文件夹"+p.getCnName()+"属于用户"+p.getCreatorName());
            p.setDeleted(true);
            p.setGmtModified(new Date());
            recursiveRemove(p,enLogin);
            commonDirMapper.updateByPrimaryKey(p);
        });
    }

    public int newDir(String businessKey,String dirName,int parentId,String enLogin) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("parentId", parentId);
        params.put("cnName", dirName);
        CommonDir item;
        String cnName = commonUserService.getCnNames(enLogin);
        Assert.state(StringUtils.isNotEmpty(cnName), "未知用户" + enLogin);
        Date now = new Date();
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) > 0) {
            item = commonDirMapper.selectAll(params).get(0);
            Assert.state(item.getDeleted(), dirName + "文件夹已存在!");
            item.setDeleted(false);
            item.setGmtModified(now);
            item.setSize(0L);
            item.setSizeName(FileUtil.getFileSizeName(item.getSize()));
            item.setCreator(enLogin);
            item.setCreatorName(cnName);
            commonDirMapper.updateByPrimaryKey(item);
        } else {
            CommonDir parentDir = commonDirMapper.selectByPrimaryKey(parentId);
            params.remove("cnName");
            int seq = (int) (PageHelper.count(() -> commonDirMapper.selectAll(params)) + 1);

            item = new CommonDir();
            item.setCreatorName(cnName);
            item.setCreator(enLogin);
            item.setBusinessKey(businessKey);
            item.setParentId(parentId);
            item.setCnName(dirName);
            item.setEditableTag("creator");
            item.setSeq(seq);
            item.setDeleted(false);
            if (parentDir != null) {
                item.setMajorName(parentDir.getMajorName());
                item.setBuildId(parentDir.getBuildId());
            }
            item.setGmtModified(now);
            item.setGmtCreate(now);
            ModelUtil.setNotNullFields(item);
            commonDirMapper.insert(item);
        }
        guavaCacheService.invalidate(commonBaseService.CACHE_COMMON_DIR + businessKey);
        return item.getId();
    }

    public int getDir(String businessKey,String dirName,int parentId,String enLogin) {
        Map params = Maps.newHashMap();
        params.put("businessKey", businessKey);
        params.put("parentId", parentId);
        params.put("cnName", dirName);
        params.put("deleted", false);
        if (PageHelper.count(() -> commonDirMapper.selectAll(params)) == 0) {
            return newDir(businessKey, dirName, parentId, enLogin);
        } else {
            return commonDirMapper.selectAll(params).get(0).getId();
        }
    }

    public void save(int id,String dirName,int seq,String remark,String enLogin) {
        CommonDir item = commonDirMapper.selectByPrimaryKey(id);
        if ("creator".equalsIgnoreCase(item.getEditableTag())) {
            Assert.state(item.getCreator().equalsIgnoreCase(enLogin), "创建人可以修改" + item.getCreatorName());
        }
        item.setCnName(dirName);
        item.setSeq(seq);
        item.setGmtModified(new Date());
        item.setRemark(remark);
        commonDirMapper.updateByPrimaryKey(item);
        guavaCacheService.invalidate(commonBaseService.CACHE_COMMON_DIR + item.getBusinessKey());
    }

    public List<Map> listBreadcrumb(int dirId) {
        List<Map> breadcrumbs = Lists.newArrayList();
        if (dirId > 0) {
            CommonDir dir = getModelById(dirId);
            List<CommonDirDto> all = listData(dir.getBusinessKey(), -1,"");
            recursiveBreadcrumb(dirId, breadcrumbs, all);
        }
        return breadcrumbs;
    }

    private void recursiveBreadcrumb(int dirId,List<Map> breadcrumbs, List<CommonDirDto> all){
        if(dirId==0){
            Map result=Maps.newHashMap();
            result.put("text","根目录");
            result.put("path","根目录");
            result.put("id",0);
            breadcrumbs.add(0,result);
        }else {
            CommonDirDto dir =all.stream().filter(p->p.getId().equals(dirId)).findFirst().get();
            Map result = Maps.newHashMap();
            result.put("text", dir.getCnName());
            result.put("path",dir.getCnName());
            if(dir.getBusinessKey().startsWith("co_")) {
                //9月份之后 启用无序号
                if(dir.getGmtCreate().after(new Date(2020,9,1))) {
                    if (StringUtils.isNotEmpty(dir.getMajorName()) && dir.getCnName().contains(dir.getMajorName())) {
                        CommonDir parent = all.stream().filter(p -> p.getId().equals(dir.getParentId())).findFirst().get();
                        if (StringUtils.isEmpty(parent.getMajorName())) {
                            result.put("path", dir.getMajorName());
                        }
                    }
                }
            }
            result.put("id", dir.getId());
            breadcrumbs.add(0, result);
            recursiveBreadcrumb(dir.getParentId(),breadcrumbs,all);
        }
    }

    /**
     * 判断是否有文件夹或文件
     * @param businessKey
     * @param dirId
     * @return
     */
    public boolean haveContent(String businessKey,int dirId){
        Map params=Maps.newHashMap();
        params.put("businessKey",businessKey);
        params.put("deleted",false);
        if(dirId>0) params.put("parentId",dirId);

        if(PageHelper.count(()->commonDirMapper.selectAll(params))>0) return true;

        params.remove("parentId");
        if(dirId>0) params.put("dirId",dirId);
        if(PageHelper.count(()->commonFileMapper.selectAll(params))>0) return true;

        return false;
    }

}
