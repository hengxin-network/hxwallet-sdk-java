package xin.heng.sample;

import com.alibaba.fastjson.JSON;
import xin.heng.IHXJsonParser;

public class FastJsonParser implements IHXJsonParser {
    @Override
    public <T> T optFromJson(String s, Class<T> aClass) {
        return JSON.parseObject(s, aClass);
    }

    @Override
    public <T> String optToJson(T t) {
        return JSON.toJSONString(t);
    }

    private static class INSTANCE{
        private static FastJsonParser instance = new FastJsonParser();
    }

    public static FastJsonParser getInstance(){
        return INSTANCE.instance;
    }
}
