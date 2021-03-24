package com.cmcu.mcc.hr.service;

import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonFormDataService;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrTrainService {

    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    HrDetailService hrDetailService;

    private  static String referType="hrTrainMember";

    public void save(int id,Map data,String enLogin) {
        Map preData=commonFormDataService.getFormDataById(id);
        String preTrainKey=preData.getOrDefault("trainKey","").toString();
        String currentTrainKey=data.getOrDefault("trainKey","").toString();
        commonFormDataService.save(id, data, enLogin);
        if(StringUtils.isNotEmpty(preTrainKey)||StringUtils.isNotEmpty(currentTrainKey)){
            if(!preTrainKey.equalsIgnoreCase(currentTrainKey)){
               List<Map> list=listHrTrainMember(preTrainKey);
               for(Map pre:list){
                   pre.put("trainKey",currentTrainKey);
                   int memberId=(int)pre.get("id");
                   CommonFormData memberFormData=commonFormDataService.commonFormDataMapper.selectByPrimaryKey(memberId);
                   pre.remove("id");
                   memberFormData.setFormData(JsonMapper.obj2String(pre));
                   commonFormDataService.commonFormDataMapper.updateByPrimaryKey(memberFormData);
               }
            }
        }
    }



    public List<Map> listHrTrainMember(String trainKey) {
        List<Map> list= Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("referType", "hrTrainMember");
        params.put("deleted", false);
        List<CommonFormData> oList = commonFormDataService.commonFormDataMapper.selectAll(params);
        for(CommonFormData commonFormData:oList){
            if(commonFormData.getFormData().contains(trainKey)){
                Map data= JsonMapper.string2Map(commonFormData.getFormData());
                if(trainKey.equalsIgnoreCase(data.getOrDefault("trainKey","").toString())){
                    data.put("id",commonFormData.getId());
                    list.add(data);
                }
            }
        }
        return list;
    }

    public void removeHrMember(int id){
        CommonFormData commonFormData=commonFormDataService.commonFormDataMapper.selectByPrimaryKey(id);
        commonFormData.setDeleted(true);
        commonFormData.setGmtModified(new Date());
        commonFormDataService.commonFormDataMapper.updateByPrimaryKey(commonFormData);
    }

    public void saveHrMember(Map data) {
        int id = Integer.parseInt(data.get("id").toString());
        CommonFormData commonFormData = commonFormDataService.commonFormDataMapper.selectByPrimaryKey(id);
        String userLogin = data.getOrDefault("userLogin", "").toString();
        if (StringUtils.isNotEmpty(userLogin)) {
            UserDto userDto = commonUserService.selectByEnLogin(userLogin);
            commonFormData.setReferId(userDto.getId());
        }
        data.remove("formDataId");
        commonFormData.setFormData(JsonMapper.obj2String(data));
        commonFormData.setGmtModified(new Date());
        commonFormDataService.commonFormDataMapper.updateByPrimaryKey(commonFormData);
    }

    /**
     * 新增培训人员
     * @param trainKey 培训主键
     * @param owners  选择的人员列表,
     * @param enLogin 操作人
     */
    public void newData(String trainKey,String owners,String enLogin) {
        List<String> users = MyStringUtil.getStringList(owners);
        if (users.size() == 0) users.add("");
        for (String owner : users) {
            //判断是否存在
            boolean isExist =false;
            List<Map> list = listHrTrainMember(trainKey);
            for(Map map:list){
                if(map.get("userLogin").equals(owner)){
                    isExist =true;
                }
            }
            if(!isExist) {
                CommonFormData commonFormData = hrDetailService.getNewModel(referType, owner, enLogin, false);
                Map data = JsonMapper.string2Map(commonFormData.getFormData());
                data.put("trainKey", trainKey);
                commonFormData.setFormData(JsonMapper.obj2String(data));
                commonFormDataService.commonFormDataMapper.updateByPrimaryKey(commonFormData);
            }

        }
    }
}
