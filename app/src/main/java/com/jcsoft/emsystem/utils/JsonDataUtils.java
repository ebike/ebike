package com.jcsoft.emsystem.utils;

import com.google.gson.Gson;

/**
 * JSON的处理
 */
public class JsonDataUtils {

    /**
     * 把json字符串，转为指定的对象，转换失败则抛出com.google.gson.JsonSyntaxException异常
     *
     * @param response json字符串
     * @param clazz    转换的类型
     * @return
     */
    public final static <T> T jsonToObject(String response, Class<T> clazz) {
        Gson gson = new Gson();
        return (T) gson.fromJson(response, clazz);
    }

}
