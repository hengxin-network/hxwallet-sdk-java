package xin.heng;

public interface IHXJsonParser {
    <T> T optFromJson(String jsonString, Class<T> clz);

    <T> String optToJson(T obj);
}
