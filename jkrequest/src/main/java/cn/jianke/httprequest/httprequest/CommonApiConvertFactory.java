package cn.jianke.httprequest.httprequest;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @className: CommonApiConvertFactory
 * @classDescription: this converter decode the response.
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class CommonApiConvertFactory extends Converter.Factory{

    public static CommonApiConvertFactory create() {
        return create(new Gson());
    }

    public static CommonApiConvertFactory create(Gson gson) {
        return new CommonApiConvertFactory(gson);
    }

    private CommonApiConvertFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonResponseBodyConverter<>();
    }

    final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        @Override public T convert(ResponseBody value) throws IOException {
            String reString;
            try {
                reString = value.string();
                return (T) reString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
