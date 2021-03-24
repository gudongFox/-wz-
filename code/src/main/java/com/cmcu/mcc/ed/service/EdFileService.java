package com.cmcu.mcc.ed.service;

import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.sys.dao.SysAttachMapper;
import com.cmcu.mcc.ed.dao.EdFileMapper;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.entity.EdFile;
import com.cmcu.mcc.sys.dto.SysAttachDto;
import com.cmcu.mcc.sys.entity.SysAttach;
import com.cmcu.mcc.sys.service.SysConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EdFileService {

    @Resource
    EdFileMapper edFileMapper;

    @Resource
    SysAttachMapper sysAttachMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    SysConfigService sysConfigService;


    @Autowired
    CommonFileMapper commonFileMapper;
    @Resource
    CommonCodeService commonCodeService;

    public void update(EdFileDto edFileDto){
        EdFile item = edFileMapper.selectByPrimaryKey(edFileDto.getId());
        item.setBusinessId(edFileDto.getBusinessId());
        item.setAttachId(edFileDto.getAttachId());
        item.setFileType(edFileDto.getFileType());
        item.setFileName(edFileDto.getFileName());
        item.setSeq(edFileDto.getSeq());
        item.setDeleted(edFileDto.getDeleted());
        item.setCreator(edFileDto.getCreator());
        item.setCreatorName(edFileDto.getCreatorName());
        item.setGmtCreate(edFileDto.getGmtCreate());
        item.setGmtModified(edFileDto.getGmtModified());
        item.setCpFileId(edFileDto.getCpFileId());
        item.setRemark(edFileDto.getRemark());
        item.setAttachIds(edFileDto.getAttachIds());
        edFileMapper.updateByPrimaryKey(item);
    }


    public void updateSign(int id,int signId,String userLogin){
        EdFile edFile=edFileMapper.selectByPrimaryKey(id);
        edFile.setSignId(signId);
        edFile.setSignUser(userLogin);
        edFile.setGmtSign(new Date());
        edFile.setAttachIds(edFile.getAttachIds()+","+signId);
        edFileMapper.updateByPrimaryKey(edFile);
    }

    /**
     * 得到附件列表
     * @param businessId
     * @return
     */
    public List<EdFileDto> listData(String businessId) {
        List<EdFile> oList = edFileMapper.listByBusinessId(businessId);
        List<EdFileDto> list = Lists.newArrayList();
        oList.forEach(p -> {
            EdFileDto edFileDto = EdFileDto.adapt(p);
            SysAttach sysAttach = sysAttachMapper.selectByPrimaryKey(p.getAttachId());
            if (sysAttach != null) {
                edFileDto.setSysAttach(sysAttach);
                edFileDto.setSizeName(FileUtil.getFileSizeName(sysAttach.getSize()));
                edFileDto.setExtensionName(FileUtil.getExtensionName(sysAttach.getName()));
                edFileDto.setMd5(sysAttach.getMd5());
            }
            list.add(edFileDto);
        });
        return list;
    }


    public EdFileDto getModelById(int id){
        return getDto(edFileMapper.selectByPrimaryKey(id));
    }


    public int getCount(String businessId){
        return (int)PageHelper.count(()->edFileMapper.listByBusinessId(businessId));
    }

    public EdFileDto insert(String name,String localPath,String userLogin,String businessId,String fileType) {
        String md5 = FileUtil.getFileMD5String(localPath);
        SysAttach sysAttach = sysAttachMapper.selectByMd5(md5);
        Date now = new Date();
        if (sysAttach == null) {
            sysAttach = new SysAttach();
            sysAttach.setSource(source);
            sysAttach.setName(name);
            sysAttach.setGmtCreate(now);
            sysAttach.setGmtModified(now);
            sysAttach.setLocalPath(localPath);
            sysAttach.setMd5(md5);
            sysAttach.setSize(new File(localPath).length());
            sysAttach.setCreator(userLogin);
            sysAttachMapper.insert(sysAttach);
        } else {
            if (new File(sysAttach.getLocalPath()).exists()) {
                new File(localPath).deleteOnExit();
            } else {
                sysAttach.setLocalPath(localPath);
                sysAttachMapper.updateByPrimaryKey(sysAttach);
            }
        }


        Map params = Maps.newHashMap();
        params.put("businessId", businessId);
        params.put("creator", userLogin);
        params.put("fileName", name);
        if (PageHelper.count(() -> edFileMapper.selectAll(params)) > 0) {
            EdFile exist = edFileMapper.selectAll(params).get(0);
            exist.setAttachId(sysAttach.getId());
            exist.setDeleted(false);
            exist.setGmtModified(new Date());
            exist.setFileName(name);
            exist.setFileType(fileType);
            exist.setSignUser("");
            exist.setAttachIds(exist.getAttachIds()+","+sysAttach.getId());
            edFileMapper.updateByPrimaryKey(exist);
            return getDto(exist);
        } else {
            EdFile edFile = new EdFile();
            edFile.setAttachId(sysAttach.getId());
            edFile.setBusinessId(businessId);
            edFile.setFileType(fileType);
            edFile.setFileName(name);
            edFile.setSeq(1);
            edFile.setDeleted(false);
            edFile.setCreator(userLogin);
            edFile.setCreatorName(hrEmployeeMapper.getNameByUserLogin(userLogin));
            edFile.setGmtCreate(now);
            edFile.setGmtModified(now);
            edFile.setAttachIds(sysAttach.getId().toString());
            ModelUtil.setNotNullFields(edFile);
            edFileMapper.insert(edFile);
            return getDto(edFile);
        }
    }


    public int insertByCpFile(String businessId,int cpFileId,String fileType,String userLogin) {
       return 0;
    }

    public int insertByFileDto(EdFileDto edFileDto,String businessId){
        EdFile item = new EdFile();
         item.setBusinessId(businessId);
         item.setAttachId(edFileDto.getAttachId());
         item.setFileType(edFileDto.getFileType());
         item.setFileName(edFileDto.getFileName());
         item.setSeq(edFileDto.getSeq());
         item.setDeleted(edFileDto.getDeleted());
         item.setCreator(edFileDto.getCreator());
         item.setCreatorName(edFileDto.getCreatorName());
         item.setGmtCreate(edFileDto.getGmtCreate());
         item.setGmtModified(edFileDto.getGmtModified());
         item.setCpFileId(edFileDto.getCpFileId());
         item.setRemark(edFileDto.getRemark());
         item.setAttachIds(edFileDto.getAttachIds());
         edFileMapper.insert(item);
         return item.getId();
    }

    public EdFileDto getDto(EdFile edFile){
        if(edFile==null) return null;
        EdFileDto edFileDto=EdFileDto.adapt(edFile);
        SysAttach sysAttach = sysAttachMapper.selectByPrimaryKey(edFile.getAttachId());
        if (sysAttach != null) {
            edFileDto.setSysAttach(sysAttach);
            edFileDto.setSizeName(FileUtil.getFileSizeName(sysAttach.getSize()));
            edFileDto.setMd5(sysAttach.getMd5());
        }
      /*  if(edFileDto.getCpFileId()>0){
            CpFile cpFile=cpFileMapper.selectByPrimaryKey(edFile.getCpFileId());
            if(cpFile!=null){
                edFileDto.setSource(cpDirService.getDirPath(cpFile.getDirId())+cpFile.getName());
            }
        }*/
        if(StringUtils.isNotEmpty(edFile.getSignUser())){
            SysAttach signAttach=sysAttachMapper.selectByPrimaryKey(edFile.getSignId());
            edFileDto.setSignMd5(signAttach.getMd5());
        }
        return edFileDto;
    }

    public void remove(int id,String userLogin){
        EdFile edFile=edFileMapper.selectByPrimaryKey(id);
        Assert.state(edFile.getCreator().equalsIgnoreCase(userLogin),"该文件属于其他用户!");
        edFile.setDeleted(true);
        edFile.setGmtModified(new Date());
        edFileMapper.updateByPrimaryKey(edFile);
    }


    public void updateFileType(int id,String fileType,String userLogin){
        EdFile edFile=edFileMapper.selectByPrimaryKey(id);
        if(!fileType.equals(edFile.getFileType())) {
            Assert.state(edFile.getCreator().equalsIgnoreCase(userLogin),"该文件属于其他用户!");
            edFile.setFileType(fileType);
            edFile.setGmtModified(new Date());
            edFileMapper.updateByPrimaryKey(edFile);
        }
    }

    /**
     * 智能判断文件类别
     * @param businessId
     * @param fileName
     * @return
     */
    public String getAIFileType(String businessId,String fileName){

        String fileType="其他相关资料";
        if(StringUtils.isEmpty(fileName)) return fileType;
        if(StringUtils.isEmpty(businessId)) return fileType;

        if(businessId.contains("edArrange_")){
            fileType="设计与开发策划书";
        }else if(businessId.contains("stampChangeAndProcess_")){
            if(fileName.contains("函件")){
                fileType="往来函件";
            }else if(fileName.contains("会议纪要")){
                fileType="会议纪要";
            }else if(fileName.contains("洽商单")){
                fileType="洽商单";
            }else if(fileName.contains("变更")){
                fileType="变更图纸";
            }else if(fileName.contains("送审版图纸")){
                fileType="送审版图纸";
            }else {
                fileType="其他相关资料";
            }
        }
        else if(businessId.toLowerCase().contains("task")){
            if(fileName.contains("合同")){
                fileType="设计合同";
            }else if(fileName.contains("委托书")){
                fileType="设计委托书";
            }else if(fileName.contains("红线")){
                fileType="国土部门批准的用地红线通知书";
            }else if(fileName.contains("规划")){
                fileType="规划部门签发的规划定点通知书";
            }else if(fileName.contains("地下")||fileName.contains("管网")){
                fileType="相关的城市地下管网资料";
            }else if(fileName.contains("勘察")){
                fileType="地质勘察报告书";
            }else if(fileName.contains("工艺")){
                fileType="有关工艺资料";
            }else if(fileName.contains("可行")){
                fileType="可行性研究报告";
            }else{
                fileType="其他相关资料";
            }
        }
        return fileType;
    }

    /**
     * 五洲附件分类 处理
     * @param businessId
     * @param fileName
     * @return
     */
    public String getAIFileTypeByBusinessKey(String businessId,String fileName){
        String fileType="其他资料";
        if(StringUtils.isEmpty(fileName)) return fileType;
        if(StringUtils.isEmpty(businessId)) return fileType;
        String businessKey = businessId.split("_")[0];
        String codeCatalog=businessKey+"_附件";
        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE, codeCatalog);
        if (commonCodes.size()>0){
            for (CommonCode commonCode:commonCodes){
                if (fileName.contains(commonCode.getCode())){
                    fileType=commonCode.getName();
                }
            }
        }

        return fileType;
    }

    public Map<String,Object> fileCheckByBusinessKey(String businessId){
        Map params = Maps.newHashMap();
        params.put("businessKey",businessId);
        params.put("deleted",false);
        List<CommonFile> commonFiles = commonFileMapper.selectAll(params);
        String businessKey = businessId.split("_")[0];
        String codeCatalog=businessKey+"_附件";
        String outType="其他资料";


        List<CommonCodeDto> commonCodes = commonCodeService.listDataByCatalog(MccConst.APP_CODE,codeCatalog);
        String allRestType="";
        for(CommonFile commonFile:commonFiles){
            allRestType=allRestType+","+commonFile.getFileType();
        }
        Map<String,Object> map=Maps.newHashMap();
        map.put("isExist",true);
        map.put("lackType","");
        String name="";
        for (CommonCode code:commonCodes){
            if (!outType.contains(code.getName())){
                if (!allRestType.contains(code.getName())){
                    map.put("isExist",false);
                    name=name+","+code.getName();
                }
            }
        }
        map.put("lackType",MyStringUtil.trimBothEndsChars(name,","));
        return map;
    }

    String source="ed";
    public String getRandomFilePath() {
        StringBuilder sbPath = new StringBuilder();
        sbPath.append(sysConfigService.getConfig().getDirPath());
        sbPath.append("\\");
        sbPath.append(source);
        sbPath.append("\\");
        sbPath.append(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        String directoryPath = sbPath.toString();
        if (!new File(directoryPath).exists()) {
            new File(directoryPath).mkdirs();
        }
        return directoryPath + "\\" + UUID.randomUUID().toString();
    }


    public List<EdFile> listDataByStepId(int stepId){
        List<EdFile> list=Lists.newArrayList();
        //删除
      /*  List<EdTaskDto> tasks=edTaskService.listDataByStepId(stepId);
        for(EdTaskDto edTask:tasks){
            list.addAll(listData(edTask.getBusinessKey()));
        }*/
        return list;
    }

    public List<SysAttachDto> listFileHistory(int id){
        List<SysAttachDto> list=Lists.newArrayList();
        EdFile edFile=edFileMapper.selectByPrimaryKey(id);
        if(edFile!=null) {
            List<Integer> attachIds = MyStringUtil.getIntList(edFile.getAttachIds());
            Map map = Maps.newHashMap();
            map.put("idList", attachIds);
            List<SysAttach> oList = sysAttachMapper.selectAll(map);
            for(SysAttach sysAttach:oList){
                SysAttachDto dto=SysAttachDto.adapt(sysAttach);
                dto.setSizeName(FileUtil.getFileSizeName(sysAttach.getSize()));
                dto.setCreatorName(hrEmployeeMapper.selectByUserLoginOrNo(sysAttach.getCreator()).getUserName());
                list.add(dto);
            }
        }
        return list;
    }

    public boolean checkIsInHistory(int id,String md5){
        EdFile edFile=edFileMapper.selectByPrimaryKey(id);
        if(edFile!=null) {
            List<Integer> attachIds = MyStringUtil.getIntList(edFile.getAttachIds());
            Map map = Maps.newHashMap();
            map.put("idList", attachIds);
            List<SysAttach> oList = sysAttachMapper.selectAll(map);
            for(SysAttach sysAttach:oList){
                if(sysAttach.getMd5().equalsIgnoreCase(md5)){
                    return true;
                }
            }
        }
        return false;
    }


    public PageInfo<Object> listDeletedFile(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",true);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> edFileMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            EdFile item=(EdFile) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void recoverFile(int id){
        EdFile edFile = edFileMapper.selectByPrimaryKey(id);
        edFile.setDeleted(false);
        edFile.setGmtModified(new Date());
        edFileMapper.updateByPrimaryKey(edFile);
    }
}
