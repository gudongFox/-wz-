package com.cmcu.common.data;


import java.util.List;

public interface IDataService<T> {

    void remove(int id,String userLogin);

    void update(T t);

    T getModelById(int id);

    int getNewModel(int foreignKey,String userLogin);

    T getDto(Object item);

    List<T> listDataByForeignKey(int foreignKey);

}
