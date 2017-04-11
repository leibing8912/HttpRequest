package cn.jianke.httprequest.httprequest;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @className: JkApiConvertFactory
 * @classDescription: this converter decode the response.
 * @author: leibing
 * @createTime: 2016/8/30
 */
public class JkApiConvertFactory extends Converter.Factory{

    public static JkApiConvertFactory create() {
        return create(new Gson());
    }

    public static JkApiConvertFactory create(Gson gson) {
        return new JkApiConvertFactory(gson);
    }

    private JkApiConvertFactory(Gson gson) {
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
                System.out.println("xxxxxxxxxxxxxxx ex = " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
