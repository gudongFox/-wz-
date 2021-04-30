package com.cmcu.common.util;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class WebUtil {


    /**
     * 自动获取的参数进行转义
     * @return
     */
    public static Map getRequestParameters() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getRequestParameters(httpServletRequest);
    }


    public static Map getRequestParameters(HttpServletRequest httpServletRequest){

        Map<String, String[]> requestParameters = httpServletRequest.getParameterMap();
        Map map = new HashMap();
        for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (StringUtils.isNotEmpty(values[0])) {
                if(StringUtils.isNotEmpty(values[0])) {
                    map.put(key, values[0]);
                }
            }
        }
        return map;
    }

    public static Map getPageInfo(int pageNum,int pageSize,int total){
        Map result = Maps.newHashMap();
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", total);
        try {
            result.put("startRow",(pageNum-1)*pageSize+1);
            result.put("endRow",pageNum*pageSize>total?total:pageNum*pageSize);
            int pages=total/pageSize+(total%pageSize==0?0:1);
            result.put("pages",pages);
            List<Integer> navigatepageNums= Lists.newArrayList();
            //小于5页直接全部
            if(pages==0){
                navigatepageNums.add(1);
            }
            else if(pages<=5){
                for(int i=1;i<=pages;i++){
                    navigatepageNums.add(i);
                }
            }
            //当前为前2页
            else if(pageNum-2<=0){
                for(int i=1;i<=pages;i++){
                    if(navigatepageNums.size()<5){
                        navigatepageNums.add(i);
                    }
                }
            }
            //当前为后2页
            else if(pageNum+2>pages){
                for(int i=pages;i>=1;i--){
                    if(navigatepageNums.size()<5) {
                        navigatepageNums.add(0, i);
                    }
                }
            }else{
                navigatepageNums.add(pageNum-2);
                navigatepageNums.add(pageNum-1);
                navigatepageNums.add(pageNum);
                navigatepageNums.add(pageNum+1);
                navigatepageNums.add(pageNum+2);
            }
            result.put("navigatepageNums",navigatepageNums);
            result.put("navigateFirstPage",navigatepageNums.get(0));
            result.put("navigateLastPage",navigatepageNums.get(navigatepageNums.size()-1));

        }catch (Exception ex){
            log.warn(ex.getMessage(),ex);
        }

        return result;

    }



    public static PageInfo<Object> buildPageInfo(int pageNum,int pageSize,int total,Object list){
        PageInfo<Object> pageInfo=new PageInfo<>();
        pageInfo.setList((List<Object>)list);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setNavigatePages(8);
        pageInfo.setStartRow((pageNum - 1) * pageSize + 1);
        pageInfo.setEndRow(pageNum * pageSize > total ? total : pageNum * pageSize);
        pageInfo.setPages( total / pageSize + (total % pageSize == 0 ? 0 : 1));
        ArrayList<Integer> navigatePageNums = Lists.newArrayList();

        for(int i=4;i>=0;i--){
            if(pageNum-i>0) {
                navigatePageNums.add(pageNum - i);
            }
        }

        for(int i=1;i<=4;i++) {
            if (navigatePageNums.size() <= 7&&(pageNum+i)<=pageInfo.getPages()) {
                navigatePageNums.add(pageNum+i);
            }
        }

//        int half=pageInfo.getNavigatePages()/2;
//        //小于5页直接全部
//        if (pageInfo.getPages() == 0) {
//            navigatePageNums.add(1);
//        } else if (pageInfo.getPages() <= pageInfo.getNavigatePages()) {
//            for (int i = 1; i <= pageInfo.getPages(); i++) {
//                navigatePageNums.add(i);
//            }
//        }
//        else if (pageNum - half <= 0) {
//            for (int i = 1; i <= pageInfo.getPages(); i++) {
//                if (navigatePageNums.size() < pageInfo.getNavigatePages()) {
//                    navigatePageNums.add(i);
//                }
//            }
//        }
//        else if (pageNum + half > pageInfo.getNavigatePages()) {
//            for (int i = pageInfo.getNavigatePages(); i >= 1; i--) {
//                if (navigatePageNums.size() <pageInfo.getNavigatePages()) {
//                    navigatePageNums.add(0, i);
//                }
//            }
//        } else {
//            for(int i=1;i<=pageInfo.getNavigatePages();i++){
//                navigatePageNums.add(pageNum-4+i);
//            }
//        }
        pageInfo.setNavigatepageNums(navigatePageNums.stream().mapToInt(Integer::intValue).toArray());
        pageInfo.setNavigateFirstPage(navigatePageNums.get(0));
        pageInfo.setNavigateLastPage(navigatePageNums.get(navigatePageNums.size()-1));
        pageInfo.setIsFirstPage(pageNum==0);
        pageInfo.setIsLastPage(pageNum==pageInfo.getPages());
        return  pageInfo;
    }


}
