package com.cmcu.common.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2018/9/18 8:37
 */


public class GuavaCacheService {


    //    /**
    //     * 调用示例
    //     * @return
    //     */
    //    public List<CadBlock> listCadBlock() {
    //        List<CadBlock> cadBlocks = cacheService.get("listCadBlock", () -> Optional.ofNullable(cadBlockMapper.selectAll()));
    //        return cadBlocks;
    //    }


   public final static Cache<Object,Optional<Object>> simpleCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(200)
            .expireAfterWrite(120, TimeUnit.MINUTES)
            .build();


    final static Cache<Object,Optional<Object>> shortCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(200)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();


    /**
     * 获取数据
     * @param key
     * @param callable
     * @param <T>
     * @return
     */
    public <T> T get(Object key, Callable<? extends Optional<Object>> callable) {
        Optional<T> optional = null;
        try {
            optional= (Optional<T>) simpleCache.get(key, callable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * 手动增加更新缓存
     * @param key
     * @param value
     */
    public void put(Object key,Optional<Object> value) {
        if (value.isPresent()) {
            simpleCache.put(key, value);
        }
    }

    /**
     * 删除缓存
     * @param key
     */
    public void invalidate(Object key){
        simpleCache.invalidate(key);
    }

    public void invalidateAll(){
        simpleCache.invalidateAll();
    }
}
