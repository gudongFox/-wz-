package com.cmcu.mcc.five.service;

import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.five.dao.FiveOaUseWordSizeMapper;
import com.cmcu.mcc.five.dao.FiveOaWordSizeMapper;

import com.cmcu.mcc.five.entity.FiveOaUseWordSize;
import com.cmcu.mcc.five.entity.FiveOaWordSize;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class
FiveOaWordSizeService {
    @Resource
    FiveOaWordSizeMapper fiveOaWordSizeMapper;
    @Resource
    FiveOaUseWordSizeMapper fiveOaUseWordSizeMapper;
    @Autowired
    SelectEmployeeService selectEmployeeService;


    /**
     * 获取当前可以使用的 文号
     * @param keyNames 查询关键字 38_人力资源部,0_中国五洲工程设计集团董事会文件 逗号分隔
     * @param year 年份
     * @param type 类型 公司发文 部门发文 收文
     * @return
     */
    public List<FiveOaWordSize> getCanUseWord(String keyNames,String year,String type){
      Map map= Maps.newHashMap();
      map.put("type",type);
      if (!Strings.isNullOrEmpty(year)){
          map.put("year",Integer.parseInt(year)-1);
      }

      List<String> deptNameList = MyStringUtil.getStringList(keyNames);
      List<FiveOaWordSize> list=Lists.newArrayList();
      for (String de:deptNameList){
          String[] s = de.split("_");
          map.put("deptId",s[0]);
          //判断如果部门ID为0
          if (Integer.parseInt(s[0])==0){
              map.put("deptName",s[1]);
          }

          List<FiveOaWordSize> list1 = fiveOaWordSizeMapper.selectAll(map);
          list.addAll(list1) ;
      }
        FiveOaWordSize nullWord = new FiveOaWordSize();
        nullWord.setWord(" ");
        nullWord.setMark(0);
        list.add(nullWord);
       return list;
    }

    /**
     * 序号表自增 对应编号+1 大流水号统一 +1
     * @param id
     */
    public void addMark(int id){
        if (id==0){
            return;
        }
        FiveOaWordSize fiveOaWordSize = fiveOaWordSizeMapper.selectByPrimaryKey(id);
        if ("大流水号".equals(fiveOaWordSize.getRemark())){
            List<FiveOaWordSize> allUnify = getAllUnify(fiveOaWordSize.getYear());
            for (FiveOaWordSize item:allUnify){
                item.setMark(item.getMark()+1);
                item.setGmtModified(new Date());
                fiveOaWordSizeMapper.updateByPrimaryKey(item);
            }
        }else {
         fiveOaWordSize.setMark(fiveOaWordSize.getMark()+1);
         fiveOaWordSize.setGmtModified(new Date());
         fiveOaWordSizeMapper.updateByPrimaryKey(fiveOaWordSize);
        }

    }


    public void updateWordSize(String selectWord ,String selectYear,int selectMark,String businessKey,String userLogin){
        if (selectMark==0||"".equals(selectWord)||"".equals(selectYear)){
            return;
        }
        //获取序号表记录
        Map<String,Object> map=Maps.newHashMap();
        map.put("word",selectWord);
        map.put("year",selectYear);
        FiveOaWordSize fiveOaWordSize = fiveOaWordSizeMapper.selectAll(map).get(0);

        //判断是否有文号记录
        Map<String,Object> params=Maps.newHashMap();
        params.put("businessKey",businessKey);
        params.put("deleted",false);
        //params.put("year",selectYear);
       if (fiveOaUseWordSizeMapper.selectAll(params).size()>0){//有记录
           FiveOaUseWordSize oldUsed = fiveOaUseWordSizeMapper.selectAll(params).get(0);
           //新增一条使用记录
           if (!IsUsedWordSize(selectWord,selectYear,selectMark)){//判断选择得文号是否使用
               //没有使用 使用改文号
               getNewUsedRecord(fiveOaWordSize.getId(),selectWord,selectYear,selectMark,businessKey,userLogin);
               //判读选择文号 是否与序列号相等
               if (fiveOaWordSize.getMark()==selectMark){
                   //序号表+1
                   addMark(fiveOaWordSize.getId());
               }
               //废弃前一条使用记录
               discardUsedRecord(oldUsed.getId());
           }else{
               if (fiveOaWordSize.getMark()==selectMark){//判断序号表
                   //序号表+1
                   addMark(fiveOaWordSize.getId());
               }
               Assert.state(false,selectWord+"〔"+selectYear+"〕"+selectMark+"文号已使用");
           }

       }else {//没有记录
           //新增一条使用记录
           if (!IsUsedWordSize(selectWord,selectYear,selectMark)){ //判断改文号 是否使用
               getNewUsedRecord(fiveOaWordSize.getId(),selectWord,selectYear,selectMark,businessKey,userLogin);
               //序号表+1
               addMark(fiveOaWordSize.getId());
           }else{
               Assert.state(false,selectWord+"〔"+selectYear+"〕"+selectMark+"文号已使用");
               if (fiveOaWordSize.getMark()==selectMark){//判断序号表
                   //序号表+1
                   addMark(fiveOaWordSize.getId());
               }
           }
       }

    }

    /**
     * 判断选择的字号 是否已经使用 true  已使用 flase 未使用
     * @param selectWord
     * @param selectYear
     * @param selectMark
     * @return
     */
    public boolean IsUsedWordSize(String selectWord ,String selectYear,int selectMark){
        Map<String,Object> params=Maps.newHashMap();
        params.put("word",selectWord);
        params.put("year",selectYear);
        params.put("mark",selectMark);
        if (fiveOaUseWordSizeMapper.selectAll(params).size()>0){
            FiveOaUseWordSize fiveOaUseWordSize = fiveOaUseWordSizeMapper.selectAll(params).get(0);
            if (fiveOaUseWordSize.getDeleted()){//判断是否存在删除记录的文号
                fiveOaUseWordSizeMapper.deleteByPrimaryKey(fiveOaUseWordSize.getId());
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    /**
     * 获取所有大流水号
     * @return
     */
    public List<FiveOaWordSize> getAllUnify(String year){
     Map map= Maps.newHashMap();
     map.put("type","公司发文");
     map.put("year",year);
     map.put("remark","大流水号");
     return fiveOaWordSizeMapper.selectAll(map);
 }

    /**
     * 新增一条使用记录
     * @param wordId
     * @param businessKey
     * @param userLogin
     */
    public void getNewUsedRecord(int wordId,String selectWord ,String selectYear,int selectMark,String businessKey,String userLogin){
        FiveOaWordSize fiveOaWordSize = fiveOaWordSizeMapper.selectByPrimaryKey(wordId);
        FiveOaUseWordSize item=new FiveOaUseWordSize();
        item.setWordId(fiveOaWordSize.getId());
        item.setBusinessKey(businessKey);
        item.setWord(selectWord);
        item.setYear(selectYear);
        item.setMark(selectMark);
        item.setRemark(item.getWord()+"〔"+item.getYear()+"〕"+item.getMark()+"号");
        item.setGmtModified(new Date());
        item.setCreator(userLogin);
        item.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaUseWordSizeMapper.insert(item);
    }

    /**
     * 废弃一条使用记录 记录废弃的号
     * @param id
     */
     public void discardUsedRecord(int id ){
         //废弃使用记录
         FiveOaUseWordSize item = fiveOaUseWordSizeMapper.selectByPrimaryKey(id);
         item.setDeleted(true);
         item.setGmtModified(new Date());
         fiveOaUseWordSizeMapper.updateByPrimaryKey(item);
        /* //记录废弃号
         FiveOaWordSize fiveOaWordSize = fiveOaWordSizeMapper.selectByPrimaryKey(item.getWordId());
         fiveOaWordSize.setAbandonMark(fiveOaWordSize.getAbandonMark()+","+item.getMark());
         fiveOaWordSize.setGmtModified(new Date());
         fiveOaWordSizeMapper.updateByPrimaryKey(fiveOaWordSize);*/

     }

    /**
     * 根据字号和年份查询 号 弱数据库中没有这个年份的字号 就新增默认编号为1号
     * @param word
     * @param year
     * @return
     */
     public FiveOaWordSize getMarkByChange(String word,String year){
     if(" ".equals(word)||"".equals(year)){//传过来得值为空 就返回一个空得数据
         FiveOaWordSize nullWord = new FiveOaWordSize();
         nullWord.setWord(word);
         nullWord.setMark(0);
         nullWord.setYear(year);
         return nullWord;
     }
      Map<String,Object> params=Maps.newHashMap();
      params.put("word",word);
      params.put("year",year);
      if (fiveOaWordSizeMapper.selectAll(params).size()>0){//查询到当前序号表 直接返回
          return  fiveOaWordSizeMapper.selectAll(params).get(0);
      }else {//为查询到数据 新增当前年份数据
          params.remove("year");
          FiveOaWordSize item=new FiveOaWordSize();
          if (fiveOaWordSizeMapper.selectAll(params).size()>0){
              FiveOaWordSize oldWord = fiveOaWordSizeMapper.selectAll(params).get(0);
              item.setWord(word);
              item.setYear(year);
              item.setMark(1);
              item.setRemark(oldWord.getRemark());
              item.setType(oldWord.getType());
              item.setDeptId(oldWord.getDeptId());
              item.setDeptName(oldWord.getDeptName());
              item.setGmtModified(new Date());
              fiveOaWordSizeMapper.insert(item);
          }
          return getModelById(item.getId());
      }
    }

     public FiveOaUseWordSize getByBusinessKey(String businessKey){
     Map map= Maps.newHashMap();
     map.put("businessKey",businessKey);
     map.put("deleted",false);
     if (fiveOaUseWordSizeMapper.selectAll(map).size()>0){
         return fiveOaUseWordSizeMapper.selectAll(map).get(0);
     }else {
         return new FiveOaUseWordSize();
     }
 }

     public FiveOaWordSize getModelById(int id){
   return fiveOaWordSizeMapper.selectByPrimaryKey(id);
   }

     public List<FiveOaUseWordSize> listUserWord(String word,String year,String key){
       Map map= Maps.newHashMap();
       map.put("word",word);
       map.put("year",year);
       if ("1".equals(key)){//正常使用中的文号
           map.put("deleted",false);
       }else if ("2".equals(key)){//废弃后的文号
           map.put("deleted",true);
       }else { //本次发文使用过的文号
           map.remove("word");
           map.remove("year");
           map.put("businessKey",key);
       }
       List<FiveOaUseWordSize> list = fiveOaUseWordSizeMapper.selectAll(map);
       return list;
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaWordSizeMapper.selectAll(params));
        return pageInfo;
    }

    public void removeWordByBusinessKey(String businessKey){
        Map map= Maps.newHashMap();
        map.put("businessKey",businessKey);
        map.put("deleted",false);

        List<FiveOaUseWordSize> list = fiveOaUseWordSizeMapper.selectAll(map);
        for (FiveOaUseWordSize wordSize:list){
            wordSize.setDeleted(true);
            wordSize.setGmtModified(new Date());
            fiveOaUseWordSizeMapper.updateByPrimaryKey(wordSize);
        }
    }

}
