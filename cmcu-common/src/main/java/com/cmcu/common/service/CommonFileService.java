package com.cmcu.common.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.*;
import com.cmcu.common.dto.*;
import com.cmcu.common.dto.CommonDirDto;
import com.cmcu.common.entity.*;
import com.cmcu.common.util.*;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonFileService {

    @Resource
    CommonDirMapper commonDirMapper;

    @Resource
    CommonFileMapper commonFileMapper;

    @Resource
    CommonAttachMapper commonAttachMapper;

    @Resource
    CommonAttachService commonAttachService;

    @Resource
    CommonEdLayerExtractionMapper commonEdLayerExtractionMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    CommonBaseService commonBaseService;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    CommonDirService commonDirService;

    @Resource
    CommonEdMarkMapper commonEdMarkMapper;

    @Resource
    CommonFormService commonFormService;

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    IHandleFormService handleFormService;

    @Resource
    CommonEdMarkService commonEdMarkService;

    @Resource
    CommonMessageService commonMessageService;

    @Resource
    IEdDataService edDataService;

    @Resource
    TaskExecutor taskExecutor;

    public List<CommonFileDto> listData(String businessKey, int dirId,String enLogin) {
        List<CommonFileDto> all = guavaCacheService.get(commonBaseService.CACHE_COMMON_FILE + businessKey, () -> {
            List<CommonFileDto> list = Lists.newArrayList();
            Map params = Maps.newHashMap();
            params.put("businessKey", businessKey);
            params.put("deleted", false);
            List<CommonFile> oList = commonFileMapper.selectAll(params);
            oList.forEach(p -> list.add(getDto(p)));
            return Optional.of(list.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getFileName)).collect(Collectors.toList()));
        });
        if (dirId >= 0) {
            all = all.stream().filter(p -> p.getDirId().equals(dirId)).collect(Collectors.toList());
            all.forEach(p -> p.setMarkCount(commonEdMarkService.getMarkCount(p.getId())));
        }
        if (StringUtils.isNotEmpty(enLogin)) {
            UserDto userDto = commonUserService.selectByEnLogin(enLogin);
            if (userDto != null && StringUtils.isNotEmpty(userDto.getOrderMethod())) {
                switch (userDto.getOrderMethod()) {
                    case "create asc":
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getGmtCreate)).collect(Collectors.toList());
                        break;
                    case "create desc":
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getGmtCreate).reversed()).collect(Collectors.toList());
                        break;
                    case "modify desc":
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getGmtModified).reversed()).collect(Collectors.toList());
                        break;
                    case "modify asc":
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getGmtModified)).collect(Collectors.toList());
                        break;
                    case "seq":
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getSeq)).collect(Collectors.toList());
                        break;
                    default:
                        all = all.stream().sorted(Comparator.comparing(CommonFile::getDirId).thenComparing(CommonFile::getFileName)).collect(Collectors.toList());
                        break;
                }
            }
        }
        return all;
    }


    public CommonFileDto getModelById(int id) {
        return getDto(commonFileMapper.selectByPrimaryKey(id));
    }

    private  CommonFileDto getDto(CommonFile item) {
        CommonFileDto dto = CommonFileDto.adapt(item);
        dto.setCommonAttach(commonAttachMapper.selectByPrimaryKey(item.getAttachId()));
        if (dto.getDirId() > 0) {
            CommonDirDto dir = commonDirService.getModelById(dto.getDirId());
            if (dir != null) {
                dto.setDirPath(dir.getDirPath());
                dto.setMajorName(dir.getMajorName());
                dto.setBuildName(dir.getBuildName());
            }
        }



        if (dto.getBusinessKey().startsWith("co_")) {
            //图层提取模板
            if (dto.getExtractId() > 0) {
                CommonEdLayerExtraction commonEdLayerExtraction = commonEdLayerExtractionMapper.selectByPrimaryKey(dto.getExtractId());
                if (commonEdLayerExtraction != null) dto.setExtractName(commonEdLayerExtraction.getExtractName());
            }
            //发布区电子传递的文件来源于哪个文件
            if(dto.getSourceId()>0) {
                CommonFile sourceFile = commonFileMapper.selectByPrimaryKey(dto.getSourceId());
                if (sourceFile != null) dto.setSourceFileName(sourceFile.getFileName());
            }
        }
        else if (StringUtils.isNotEmpty(item.getFileProperty())) {
            Map property = JsonMapper.string2Map(item.getFileProperty());
            dto.setSignId((int) property.getOrDefault("signId", 0));
            if (dto.getSignId() > 0) {
                try {
                    dto.setSignTime(DateUtils.parseDate(property.get("signTime").toString(), "yyyy-MM-dd HH:mm:ss"));
                    dto.setSigner(property.get("signer").toString());
                    CommonAttach signAttach=commonAttachService.getModelById(dto.getSignId());
                    dto.setSignMd5(signAttach.getMd5());
                }catch (Exception ex){

                }
            }
        }




        dto.setMarkCount(commonEdMarkService.getMarkCount(item.getId()));
        ModelUtil.setNotNullFields(dto);
        return dto;
    }


    /**
     * 同步数据到设计流程中
     * @param coFiles
     * @param destBusinessKey
     * @param enLogin
     */
    @Transactional
    public void transferCoToEd(List<CommonFileDto> coFiles,String destBusinessKey,String enLogin) {
        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance("", destBusinessKey, enLogin);
        Assert.state(customProcessInstance != null && customProcessInstance.getMyRunningTaskNameList().size() > 0, "该流程在其他用户处理中!");
        Date now=new Date();
        String cnName=commonUserService.getCnNames(enLogin);
        Map params = Maps.newHashMap();
        params.put("businessKey", destBusinessKey);
        params.put("parentId",0);
        List<CommonFile> list=commonFileMapper.selectAll(params);
        for (CommonFile pre : coFiles) {

            checkUploadAble(destBusinessKey,0,pre.getFileName(),"",enLogin);

            if(list.stream().anyMatch(p->pre.getId().equals(p.getSourceId()))) {
                Optional<CommonFile> optionalCommonFile = list.stream().filter(p -> pre.getId().equals(p.getSourceId())).filter(p -> !p.getDeleted()).findFirst();
                if (!optionalCommonFile.isPresent())
                    optionalCommonFile = list.stream().filter(p -> pre.getId().equals(p.getSourceId())).findFirst();
                CommonFile item = optionalCommonFile.get();
                if(item.getDeleted()||!item.getAttachId().equals(pre.getAttachId())) {
                    item.setDeleted(false);
                    item.setAttachId(pre.getAttachId());
                    List<Integer> attachIdList = MyStringUtil.getIntList(item.getAttachIdList());
                    attachIdList.add(pre.getAttachId());
                    item.setAttachIdList(StringUtils.join(attachIdList, ","));
                    item.setGmtModified(now);
                    item.setCreator(enLogin);
                    item.setLocked(true);
                    item.setLockedLogin(enLogin);
                    item.setCreatorName(cnName);
                    item.setLockedName(cnName);
                    commonFileMapper.updateByPrimaryKey(item);
                }
            }else{
                insertByExistAttach(destBusinessKey,0,pre.getFileName(),pre.getAttachId(),pre.getId(),0,enLogin);
            }
        }
    }




    /**
     * 上传文件
     *
     * @param file        文件
     * @param businessKey 目的标识
     * @param dirId       目的文件夹
     * @param fileName    文件名称
     * @param sourceId    协同源文件
     * @param remark      备注
     * @param enLogin     上传人
     * @return
     * @throws IOException
     */
    public int insert(MultipartFile file, String businessKey, int dirId, String fileName, int sourceId,int extractId,String remark, String enLogin) throws IOException {
        Assert.state(StringUtils.isNotEmpty(businessKey) || dirId > 0, "上传目的地不能为空!");
        if (StringUtils.isEmpty(businessKey)) {
            businessKey = commonDirMapper.selectByPrimaryKey(dirId).getBusinessKey();
        }
        String cnName = commonUserService.getCnNames(enLogin);
        Assert.state(StringUtils.isNotEmpty(cnName), "上传人不能识别!");
        int attachId = commonAttachService.insert(file, fileName, remark, enLogin);
        return insertByExistAttach(businessKey, dirId, fileName, attachId, sourceId,extractId, enLogin);
    }

    public void insertBySign(String businessKey,int dirId,String fileName,int attachId,String enLogin) {
        Date now = new Date();
        Map params = Maps.newHashMap();
        params.put("dirId", dirId);
        params.put("fileName", fileName);
        params.put("deleted", false);
        params.put("businessKey", businessKey);
        if (PageHelper.count(() -> commonFileMapper.selectAll(params)) > 0) {
            UserDto userDto = commonUserService.selectByEnLogin(enLogin);
            CommonFile item = commonFileMapper.selectAll(params).get(0);
            Map fileProperty = Maps.newHashMap();
            fileProperty.put("signTime", now);
            fileProperty.put("signId", attachId);
            fileProperty.put("signer", enLogin);
            item.setFileProperty(JsonMapper.obj2String(fileProperty));
            commonFileMapper.updateByPrimaryKey(item);
            commonBaseService.clearCache(item.getBusinessKey(),0);
            commonMessageService.insert(userDto.getTenetId(), userDto.getEnLogin(), Lists.newArrayList(item.getCreator()), "协同设计", "文件打码", item.getFileName() + "打码成功.", "/web/v1/cad/file.html?fileId=" + item.getId() + "&enLogin=" + item.getCreator());
        }
    }

    public int insertByExistAttach(String businessKey,int dirId,String fileName,int attachId,int sourceId,int extractId,String enLogin){

        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Date now = new Date();
        Map params = Maps.newHashMap();
        params.put("dirId", dirId);
        params.put("fileName", fileName);
        params.put("businessKey", businessKey);

        CommonFile item;
        if (PageHelper.count(() -> commonFileMapper.selectAll(params)) == 0) {
            item = new CommonFile();
            item.setBusinessKey(businessKey);
            item.setFileType(getAIFileType(businessKey, fileName));
            item.setDirId(dirId);
            item.setAttachId(attachId);
            item.setCreator(enLogin);
            item.setCreatorName(userDto.getCnName());
            item.setDeleted(false);
            item.setFileName(fileName);
            item.setLocked(true);
            item.setLockedLogin(enLogin);
            item.setLockedName(item.getCreatorName());
            item.setSourceId(sourceId);
            item.setGmtCreate(now);
            item.setGmtModified(now);
            item.setExtractId(extractId);
            params.remove("fileName");
            params.put("deleted",false);
            item.setSeq((int)PageHelper.count(() -> commonFileMapper.selectAll(params))+1);
            item.setAttachIdList(attachId+"");
            ModelUtil.setNotNullFields(item);
            commonFileMapper.insert(item);
        } else {
            item = commonFileMapper.selectAll(params).get(0);
            if (item.getDeleted()) {
                item.setDeleted(false);
                item.setLockedName(userDto.getCnName());
                item.setLocked(true);
                item.setLockedLogin(enLogin);
            } else {
                Assert.state(item.getLocked(), "请先加锁该文件!");
                Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin), fileName + "被用户(" + item.getLockedName() + "锁定中..");
                CommonAttach added = commonAttachService.commonAttachMapper.selectByPrimaryKey(attachId);
                CommonAttach current = commonAttachService.commonAttachMapper.selectByPrimaryKey(item.getAttachId());
                Assert.state(!added.getMd5().equalsIgnoreCase(current.getMd5()), fileName + "与服务器最新版(" + DateFormatUtils.format(current.getGmtCreate(), "yyyy-MM-dd HH:mm") + ")一致!");
            }
            item.setAttachId(attachId);
            item.setGmtModified(now);
            if(extractId>0) {
                item.setExtractId(extractId);
            }
            item.setAttachIdList(StringUtils.join(MyStringUtil.getStringList(item.getAttachIdList() + "," + attachId), ","));
            item.setFileProperty("");
            commonFileMapper.updateByPrimaryKey(item);
        }


        commonBaseService.clearCache(item.getBusinessKey(),dirId);

        if(item.getBusinessKey().startsWith("co_")){
            taskExecutor.execute(()->{
                CommonFileDto file=getModelById(item.getId());

                if(file.getDirPath().contains("\\00参照\\")) {
                    com.cmcu.common.entity.CommonDir dir = commonDirMapper.selectByPrimaryKey(file.getDirId());
                    List<CommonEdArrangeUserDto> users = edDataService.listUser(Integer.parseInt(StringUtils.split(item.getBusinessKey(), "_")[1]), "", dir.getBuildId());
                    String msgType="协同设计";
                    String msgCaption="参照更新";
                    String msgText=file.getDirPath()+file.getFileName();
                    List<String> receivers= MyStringUtil.getStringList(StringUtils.join(users.stream().map(p->p.getAllUser()).collect(Collectors.toList()),","));
                    if(receivers.size()>0) {
                        for(String receiver:receivers) {
                            commonMessageService.insert(userDto.getTenetId(), userDto.getEnLogin(), Lists.newArrayList(receiver), msgType, msgCaption, msgText, "/web/v1/cad/file.html?fileId=" + item.getId() + "&enLogin=" + receiver);
                        }
                    }

                }
            });
        }

        return item.getId();
    }

    public void remove(int id, String enLogin) {
        CommonFile item = commonFileMapper.selectByPrimaryKey(id);
        Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin), "只能由用户" + item.getLockedName() + "删除");
        item.setDeleted(true);
        item.setGmtModified(new Date());
        commonFileMapper.updateByPrimaryKey(item);
        commonBaseService.clearCache(item.getBusinessKey(),item.getDirId());
    }

    public void save(int id,String fileType,String fileName,int seq,String remark,String enLogin) {
        CommonFile item = commonFileMapper.selectByPrimaryKey(id);
        Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin), "只能由用户" + item.getLockedName() + "修改");
        item.setFileName(fileName);
        if(fileType!=null){
            item.setFileType(fileType);
        }
        if(remark!=null){
            item.setRemark(remark);
        }
        item.setSeq(seq);
        item.setGmtModified(new Date());
        commonFileMapper.updateByPrimaryKey(item);
        guavaCacheService.invalidate(commonBaseService.CACHE_COMMON_FILE+item.getBusinessKey());
    }

    public void toggleLocked(int id, String enLogin) {
        CommonFile item = commonFileMapper.selectByPrimaryKey(id);
        if (item.getLocked()) {
            Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin), "文件由用户" + item.getLockedName() + "锁定!");
            item.setLocked(false);
        } else {
            item.setLocked(true);
            item.setLockedLogin(enLogin);
            item.setLockedName(commonUserService.getCnNames(enLogin));
        }
        item.setGmtModified(new Date());
        commonFileMapper.updateByPrimaryKey(item);
        guavaCacheService.invalidate(commonBaseService.CACHE_COMMON_FILE + item.getBusinessKey());
    }

    public boolean checkUploadAble(String businessKey,int dirId,String fileName,String md5,String enLogin){
        Assert.state(StringUtils.isNotEmpty(enLogin), "上传人不能为空");
        if (StringUtils.isEmpty(businessKey)) {
            businessKey = commonDirMapper.selectByPrimaryKey(dirId).getBusinessKey();
        }


        Map params=Maps.newHashMap();
        params.put("dirId",dirId);
        params.put("fileName",fileName);
        params.put("businessKey",businessKey);

        if(PageHelper.count(()->commonFileMapper.selectAll(params))==0) return true;
        CommonFile item=commonFileMapper.selectAll(params).get(0);
        if(item.getDeleted()) return true;
        Assert.state(item.getLocked(),"未锁定该文件.");
        Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin),"其他用户"+item.getLockedName()+"锁定中.");
        CommonAttach commonAttach = commonAttachService.getModelById(item.getAttachId());
        Assert.state(!commonAttach.getMd5().equalsIgnoreCase(md5),fileName+"与云端完全一致!");
        return true;
    }

    public List<CommonAttach> listHistory(int id){
        CommonFile item=commonFileMapper.selectByPrimaryKey(id);
        List<Integer> attachIdList=MyStringUtil.getIntList(item.getAttachIdList());
        if(attachIdList.size()>0) {
            Map params = Maps.newHashMap();
            params.put("attachIdList", attachIdList);
            return commonAttachMapper.selectAll(params);
        }
        return Lists.newArrayList();
    }

    //知道url 上传到附件
    public void insertByUrl(String businessKey,String url,String userLogin){
        if(Strings.isNullOrEmpty(url)){
           return;
        }
        String[] split = url.split("/");
        int attachId =Integer.valueOf(split[split.length-1]);
        CommonAttach commonAttach = commonAttachMapper.selectByPrimaryKey(attachId);

        //判断附件是否添加
        boolean isExist =false;
        List<CommonFileDto> commonFileDtos = listData(businessKey, 0, userLogin);
        for(CommonFileDto dto:commonFileDtos){
            if(dto.getAttachId().equals(attachId)){
                isExist = true;
            }
        }
        if(!isExist){
            insertByExistAttach(businessKey,0,commonAttach.getName(),attachId,0,0,userLogin);
        }

    }

    /**
     * 将一个流程的附件添加到另一个流程中  dirId =0
     * @param businessKey
     * @param destBusinessKey
     * @param dirId dirId =0 只转了流程最外层文件 dirId=-1  所有文件
     */
    public void copyFileByBusinessKey(String businessKey,String destBusinessKey,int  dirId){
        List<CommonFileDto> commonFileDtos = listData(businessKey, -1, "");
        for(CommonFileDto dto:commonFileDtos){
            CommonFile commonFile = new CommonFile();
            commonFile.setBusinessKey(destBusinessKey);
            //获取新的文件夹位置
            if (dirId==0){
                commonFile.setDirId(getNewDirId(0,destBusinessKey));
            }

            commonFile.setFileType(dto.getFileType());
            commonFile.setFileName(dto.getFileName());
            commonFile.setFileProperty(dto.getFileProperty());
            commonFile.setAttachIdList(dto.getAttachIdList());
            commonFile.setAttachId(dto.getAttachId());
            commonFile.setSourceId(dto.getSourceId());
            commonFile.setExtractId(dto.getExtractId());
            commonFile.setSeq(dto.getSeq());
            commonFile.setLocked(dto.getLocked());
            commonFile.setLockedName(dto.getLockedName());
            commonFile.setLockedLogin(dto.getLockedLogin());
            commonFile.setCreator(dto.getCreator());
            commonFile.setCreatorName(dto.getCreatorName());
            commonFile.setDeleted(dto.getDeleted());
            commonFile.setGmtCreate(dto.getGmtCreate());
            commonFile.setGmtModified(dto.getGmtModified());
            commonFile.setRemark(dto.getRemark());
            commonFileMapper.insert(commonFile);

        }
    }

    private int getNewDirId(Integer dirId, String destBusinessKey) {
        //最外层依旧最外层
        if(dirId==0){
            return 0;
        }
        CommonDir commonDir = commonDirMapper.selectByPrimaryKey(dirId);
        //目标流程下的文件夹
        List<CommonDirDto> commonDirDtos = commonDirService.listData(destBusinessKey, -1, "");
        boolean isExist = false;//判断文件夹是否存在
        for(CommonDirDto dto:commonDirDtos){
            //同名则放到对应目录
            if(dto.getCnName().equals(commonDir.getCnName())){
                isExist = true;
                return dto.getId();
            }
        }
        if(!isExist){

        }
      return 0;
    }


    public boolean checkIsInHistory(int id,String md5){
        List<CommonAttach> list=listHistory(id);
        return list.stream().anyMatch(p->p.getMd5().equalsIgnoreCase(md5));
    }

    public void removeHistory(int id,int attachId,String enLogin) {
        CommonFile item = commonFileMapper.selectByPrimaryKey(id);
        Assert.state(item.getLockedLogin().equalsIgnoreCase(enLogin), "该文件属于用户" + item.getLockedName());
        List<Integer> attachIdList = MyStringUtil.getIntList(item.getAttachIdList());
        if (attachIdList.contains(attachId)) {
            CommonAttach commonAttach = commonAttachMapper.selectByPrimaryKey(attachId);
            Assert.state(commonAttach.getCreator().equalsIgnoreCase(enLogin), "该版本属于用户" + commonAttach.getCreator());
            attachIdList.remove(attachIdList.indexOf(attachId));
        }
        item.setGmtModified(new Date());
        item.setAttachIdList(StringUtils.join(attachIdList, ","));
        commonFileMapper.updateByPrimaryKey(item);
    }


    /**
     * 得到提取过的名称
     * @param fileId
     * @param extractId 图层提取模板Id
     * @param enLogin
     * @return
     */
    public List<CommonFileDto> listDataByExtractId(int fileId, int extractId,String enLogin) {
        List<CommonFileDto> list = Lists.newArrayList();
        CommonFile commonFile = commonFileMapper.selectByPrimaryKey(fileId);
        if (commonFile != null) {
            List<CommonFileDto> all = listData(commonFile.getBusinessKey(), -1, "");
            List<CommonFileDto> fileList = all.stream().filter(p -> p.getExtractId().equals(extractId)).collect(Collectors.toList());
            list.addAll(fileList.stream()
                    .filter(p -> p.getCreator().equalsIgnoreCase(enLogin) || p.getLockedLogin().equalsIgnoreCase(enLogin))
                    .sorted(Comparator.comparing(CommonFile::getGmtCreate).reversed())
                    .collect(Collectors.toList()));

            list.addAll(fileList.stream()
                    .filter(p -> !p.getCreator().equalsIgnoreCase(enLogin) && !p.getLockedLogin().equalsIgnoreCase(enLogin))
                    .sorted(Comparator.comparing(CommonFile::getGmtCreate).reversed())
                    .collect(Collectors.toList()));

        }
        return list;
    }




    /**
     * 判断文件类型
     * @param businessKey
     * @param fileName
     * @return
     */
    private String getAIFileType(String businessKey, String fileName) {
        TplConfigDto tplConfigDto=handleFormService.getTplConfigDto("",businessKey,"");
        String fileType="业务附件";
        if(tplConfigDto.isShowFileType()){
            CommonFormData commonFormData=commonFormDataService.getModelByBusinessKey(businessKey);
            if(commonFormData!=null) {
                String code = tplConfigDto.getFileTypeCode();
                List<CommonCodeDto> codes = listFileType(commonFormData.getTenetId(), code);
                if (codes.stream().anyMatch(p -> fileName.contains(p.getCode()))) {
                    return codes.stream().filter(p -> fileName.contains(p.getCode())).findFirst().get().getCode();
                }

                CommonFile latest = commonFileMapper.selectLatestByBusinessKey(businessKey);
                if (latest != null) {
                    return latest.getFileType();
                }

                if (codes.stream().anyMatch(CommonCode::getDefaulted)) {
                    return codes.stream().filter(CommonCode::getDefaulted).findFirst().get().getCode();
                }

                if (codes.size() > 0) {
                    return codes.get(0).getCode();
                }
            }
        }

        if (businessKey.contains("ed_step_mis")) {
            return "项目分期管理";
        }

        return fileType;
    }


    public List<CommonCodeDto> listFileType(String tenetId, String fileCodeType){
        if(StringUtils.isNotEmpty(fileCodeType)) {
            if (fileCodeType.startsWith("code_")) {
                String codeCatalog = fileCodeType.replace("code_", "");
                return commonCodeService.listDataByCatalog(tenetId, codeCatalog);
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 得到该文件夹压缩后的文件
     * @param dirId
     * @return
     * @throws IOException
     */
    public String getTempZipResult(int dirId) throws IOException {
        String tempPath = commonAttachService.BASE_PATH + "\\temp";
        com.cmcu.common.entity.CommonDir dir = commonDirMapper.selectByPrimaryKey(dirId);
        Assert.state(dir.getSize()<1024*1024*500,"下载文件夹内容需小于500MB("+FileUtil.getFileSizeName(dir.getSize())+")!");
        String rootPath = tempPath + "\\" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        recursiveCopyChild(dirId, rootPath);
        String zipPath = tempPath+"\\"+dir.getCnName() + ".zip";
        ZipUtils.toZip(rootPath + "\\" + dir.getCnName(), new FileOutputStream(new File(zipPath)), true);
        FileUtil.deleteFile(new File(rootPath));
        return zipPath;
    }

    /**
     * 得到该文件夹压缩后的文件
     * @param ids dir_1,dir_2,file_1,file_2
     * @return
     * @throws IOException
     */
    public String getTempZipResult(String ids) throws IOException {
        String tempPath = commonAttachService.BASE_PATH + "\\temp";
        List<CommonFileDto> files = Lists.newArrayList();
        List<com.cmcu.common.entity.CommonDir> dirs = Lists.newArrayList();
        for (String id : MyStringUtil.getStringList(ids)) {
            Integer id_ = Integer.parseInt(StringUtils.split(id, "_")[1]);
            if (id.contains("file_")) {
                files.add(getModelById(id_));
            } else {
                dirs.add(commonDirMapper.selectByPrimaryKey(id_));
            }
        }


        String outputName = "多文件";
        long total = (Long) files.stream().mapToLong(p -> p.getCommonAttach().getSize()).sum() + (Long) dirs.stream().mapToLong(com.cmcu.common.entity.CommonDir::getSize).sum();
        Assert.state(total < 1024 * 1024 * 500, "下载内容(" + FileUtil.getFileSizeName(total) + ")需小于500MB");
        String rootPath = tempPath + "\\" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        if(!new File(rootPath).exists()){
            new File(rootPath).mkdirs();
        }

        for (com.cmcu.common.entity.CommonDir dir : dirs) {
            recursiveCopyChild(dir.getId(), rootPath);
        }
        for (CommonFileDto file : files) {
            File originalFile = new File(file.getCommonAttach().getLocalPath());
            if (originalFile.exists()) {
                FileCopyUtils.copy(originalFile, new File(rootPath + "\\" + file.getFileName()));
            }
        }

        //单个文件直接下载
        if(files.size()==1&&dirs.size()==0){
            return rootPath + "\\" + files.get(0).getFileName();
        }

        String zipPath = tempPath + "\\" + outputName + ".zip";
        ZipUtils.toZip(rootPath + "\\" + outputName, new FileOutputStream(new File(zipPath)), true);
        FileUtil.deleteFile(new File(rootPath));
        return zipPath;
    }


    private void recursiveCopyChild(int dirId,String parentPath) throws IOException {
        com.cmcu.common.entity.CommonDir dir=commonDirMapper.selectByPrimaryKey(dirId);
        String currentDirPath=parentPath+"\\"+dir.getCnName();
        File directory=new File(currentDirPath);
        if(!directory.exists()) directory.mkdirs();
        List<CommonFileDto> files=listData(dir.getBusinessKey(),dir.getId(),"");
        for(CommonFileDto file:files){
            File originalFile=new File(file.getCommonAttach().getLocalPath());
            if(originalFile.exists()){
                FileCopyUtils.copy(originalFile,new File(currentDirPath+"\\"+file.getFileName()));
            }
        }
        Map params=Maps.newHashMap();
        params.put("parentId",dir.getId());
        params.put("businessKey", dir.getBusinessKey());
        params.put("deleted",false);
        List<com.cmcu.common.entity.CommonDir> childDirList=commonDirMapper.selectAll(params);
        for(com.cmcu.common.entity.CommonDir childDir:childDirList){
            recursiveCopyChild(childDir.getId(),currentDirPath);
        }
    }



}



