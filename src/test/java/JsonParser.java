import com.google.gson.Gson;
import xin.heng.IHXJsonParser;


public class JsonParser implements IHXJsonParser {

    private Gson gson = new Gson();

    @Override
    public <T> T optFromJson(String jsonString, Class<T> clz) {
        return gson.fromJson(jsonString, clz);
    }

    @Override
    public <T> String optToJson(T obj) {
        return gson.toJson(obj);
    }
}
