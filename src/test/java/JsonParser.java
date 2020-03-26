import com.alibaba.fastjson.JSON;
import xin.heng.IHXJsonParser;


public class JsonParser implements IHXJsonParser {
    @Override
    public <T> T optFromJson(String jsonString, Class<T> clz) {
        return JSON.parseObject(jsonString, clz);
    }

    @Override
    public <T> String optToJson(T obj) {
        return JSON.toJSONString(obj);
    }

}
