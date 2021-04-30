package com.cmcu.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MyStringUtil {


    public static List<Integer> getIntList(String value){
        try {
            List<Integer> result= Lists.newArrayList();
            List<String> stringList=getStringList(value);
            stringList.forEach(p->result.add(Integer.parseInt(p)));
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Lists.newArrayList();
    }
    //获取新的id ',1,4,8' ->'1,8'
    public static String getNewStringId(String value,int id) {
        List<Integer> oldIds = MyStringUtil.getIntList(value);
        List<Integer> ids = new ArrayList<>();
        for (int id1 : oldIds) {
            if (id1 != id) {
                ids.add(id1);
            }
        }
        String newStringId = "";
        for (int id1 : ids) {
            newStringId = newStringId + "," + id1;
        }
        return newStringId;
    }


    public static List<String> getStringList(String value) {
        try {
            if(StringUtils.isNotEmpty(value)) {
                List<String> stringList = Arrays.asList(StringUtils.split(value.replace("，",","), ",")).stream().filter(StringUtils::isNotEmpty).map(StringUtils::trim).distinct().collect(Collectors.toList());
                return stringList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Lists.newArrayList();
    }
    //排除id为0的数据
    public static List<String> getStringListExcept0(String value) {
        try {
            if(StringUtils.isNotEmpty(value)) {
                List<String> stringList = Arrays.asList(StringUtils.split(value.replace("，",","), ",")).stream()
                        .filter(StringUtils::isNotEmpty).filter(p->!p.equalsIgnoreCase("0")).map(StringUtils::trim).distinct().collect(Collectors.toList());
                return stringList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Lists.newArrayList();
    }


    /**
     * 处理数据为格式 方便数据库检索
     * ,值1,值2,
     * @param value
     * @return
     */
    public static  String  getMultiDotString(String value){
        List<String> stringList=getStringList(value);
        if(stringList.size()==0) return "";
        return ","+ StringUtils.join(stringList,",")+",";
    }



    public static  String getDurationRead(long duration){
        if(duration< 60 * 1000){
            return duration/1000+"秒";
        }else if(duration< 60*60*1000) {
            return duration / (60 * 1000) + "分";
        }else if(duration<24*60*60*1000) {
            return new BigDecimal(duration / (60 * 60 * 1000 * 1.0)).setScale(2, RoundingMode.UP).toString() + "时";
        }else {
            return duration / (24 * 60 * 60 * 1000) + "天";
        }
    }



    public static String getFirstValue(String value){
        List<String> values=getStringList(value);
        if(values.size()==0) return "";
        return values.get(0);
    }

    public static String listToString(List<String> list){
        String result = "";
        for(String s:list){
            result = result+","+s;
        }
        return result;
    }


    public static  String getAddMultiDotString(String preValue,String addedValue) {
        List<String> stringList = getStringList(preValue);
        stringList.add(addedValue);
        return "," + StringUtils.join(stringList.stream().distinct().collect(Collectors.toList()), ",") + ",";
    }

    /**
     * 去除字符串首尾 特殊符号
     * @param srcStr 需求去除的字符串
     * @param splitter 需要去除的特殊字符
     * @return
     */
    public static String trimBothEndsChars(String srcStr, String splitter) {
        String regex = "^" + splitter + "*|" + splitter + "*$";
        return srcStr.replaceAll(regex, "");
    }


    //输入金额 标准化
    public static String moneyToString( String money) {
        try {
            if(StringUtils.isEmpty(money)){
                return "0.000000";
            }else{
                BigDecimal bdv = new BigDecimal(money);
                bdv = bdv.setScale(8,BigDecimal.ROUND_HALF_UP);
                return bdv.toPlainString();
            }
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //scale 保留位数
    public static String moneyToString( String money,int scale) {
        try {
            if(StringUtils.isEmpty(money)){
                return "0.000000";
            }else{
                BigDecimal bdv = new BigDecimal(money);
                bdv = bdv.setScale(scale,BigDecimal.ROUND_HALF_UP);
                return bdv.toPlainString();
            }
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //获取新的金额 加法
    public static String getNewAddMoney(String preMoney, String money) {
        try {

            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(money)){
                money = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).add(BigDecimal.valueOf(Double.valueOf(money)));
            bdv = bdv.setScale(8,BigDecimal.ROUND_HALF_UP);
            return bdv.toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //scale 保留位数
    public static String getNewAddMoney(String preMoney, String money,int scale) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(money)){
                money = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).add(BigDecimal.valueOf(Double.valueOf(money)));
            bdv = bdv.setScale(scale,BigDecimal.ROUND_HALF_UP);
            return bdv.toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //获取新的金额 减法
    public static String getNewSubMoney(String preMoney, String money) {
        try {
        if(StringUtils.isEmpty(preMoney)){
            preMoney = "0";
        }
        if(StringUtils.isEmpty(money)){
            money = "0";
        }
        BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).subtract(BigDecimal.valueOf(Double.valueOf(money)));
        bdv = bdv.setScale(8,BigDecimal.ROUND_HALF_UP);
        return bdv.toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //scale 保留位数
    public static String getNewSubMoney(String preMoney, String money,int scale) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(money)){
                money = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).subtract(BigDecimal.valueOf(Double.valueOf(money)));
            bdv = bdv.setScale(scale,BigDecimal.ROUND_HALF_UP);
            return bdv.toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }

    //获取新的金额 乘法
    public static String getNewMultiplyMoney(String preMoney, String proportion ) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(proportion)){
                proportion = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).multiply(BigDecimal.valueOf(Double.valueOf(proportion)));
            return bdv.setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //scale 保留位数
    public static String getNewMultiplyMoney(String preMoney, String proportion,int scale ) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(proportion)){
                proportion = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).multiply(BigDecimal.valueOf(Double.valueOf(proportion)));
            return bdv.setScale(scale,BigDecimal.ROUND_HALF_UP).toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }

    //获取新的金额 占比
    public static String getNewDivideMoney(String preMoney, String totalMoney) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(totalMoney)||Double.valueOf(totalMoney).equals(0.0)){
                totalMoney = "1";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).divide(BigDecimal.valueOf(Double.valueOf(totalMoney)),4, BigDecimal.ROUND_HALF_UP);
            return bdv.multiply(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString()+"%";
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //获取新的金额 乘法 %
    public static String getNewMultiplyMoney1(String preMoney, String proportion ) {
        try {
            if(StringUtils.isEmpty(preMoney)){
                preMoney = "0";
            }
            if(StringUtils.isEmpty(proportion)){
                proportion = "0";
            }
            BigDecimal bdv = BigDecimal.valueOf(Double.valueOf(preMoney)).multiply(BigDecimal.valueOf(Double.valueOf(proportion)*0.01));
            return bdv.setScale(6,BigDecimal.ROUND_HALF_UP).toPlainString();
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //获取新的金额 集合累加
    public static String getNewTotalListMoney(List<String> total) {
        String sum ="0";
        for(String s:total){
            sum = getNewAddMoney(sum,moneyToString(s));
        }
        return sum;
    }

    //元 转 万元
    public static String getMoneyW(String money) {
        try {
            if(StringUtils.isEmpty(money)){
                return "0.000000";
            }else{
                BigDecimal bdv = new BigDecimal(money);
                BigDecimal bdv1 =bdv.divide(BigDecimal.valueOf(10000));
                bdv1 = bdv1.setScale(8,BigDecimal.ROUND_HALF_UP);
                return bdv1.toPlainString();
            }
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }
    //万元 转 元
    public static String getMoneyY(String money) {
        try {
            if(StringUtils.isEmpty(money)){
                return "0.00";
            }else{
                BigDecimal bdv = new BigDecimal(money);
                BigDecimal bdv1 =bdv.multiply(BigDecimal.valueOf(10000));
                bdv1 = bdv1.setScale(2,BigDecimal.ROUND_HALF_UP);
                return bdv1.toPlainString();
            }
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return "";
        }
    }

    //金额比较 -1 a小于b   0 a等于b    1 a大于b
    public static int compareMoney(String a,String b) {
        try {
            if(StringUtils.isEmpty(a)){
                a = "0.000000";
            }
            if(StringUtils.isEmpty(b)){
                b = "0.000000";
            }
            BigDecimal bdv1 = new BigDecimal(a);
            BigDecimal bdv2 = new BigDecimal(b);

            return bdv1.compareTo(bdv2);
        }catch (Exception ex) {
            Assert.state(false, "请准确填写金额数据");
            return -2;
        }
    }



    //预算类型 数据库表转化
    public static String getTrueBudgetBusinessKey(String businessKey) {
        return "";
    }


}
