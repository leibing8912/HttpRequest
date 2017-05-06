package cn.jianke.sample.httprequest.okhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @className: JkOkHttpUpDownFileUtils
 * @classDescription: okhttp上传下载
 * @author: leibing
 * @createTime: 2017/5/6
 */
public class JkOkHttpUpDownFileUtils {
    // 连接超时时间
    public final static int CONNECT_TIMEOUT =60;
    // 读取超时时间
    public final static int READ_TIMEOUT=100;
    // 写的超时时间
    public final static int WRITE_TIMEOUT=60;
    // 已存在该文件
    public final static String HAS_THE_FILE_EXISTS = "the file is existed~";
    // sington
    private static JkOkHttpUpDownFileUtils instance;
    // ok http client
    private OkHttpClient mOkHttpClient;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    private JkOkHttpUpDownFileUtils(){
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * get sington
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param
     * @return
     */
    public static JkOkHttpUpDownFileUtils getInstance(){
        if (instance == null){
            synchronized (OkHttpRequestUtils.class){
                if (instance == null)
                    instance = new JkOkHttpUpDownFileUtils();
            }
        }
        return  instance;
    }

    /**
     * 上传单文件
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param requestUrl 上传文件地址
     * @param filePath 本地地址
     * @param mCallBack 上传回调
     * @return
     */
    public void upLoadFile(String requestUrl, String filePath, final UpLoadFileCallBack mCallBack) {
        // create file
        File file = new File(filePath);
        // create requestBody
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        // create request
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        // create call
        final Call call = mOkHttpClient.newCall(request);
        // start request
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mCallBack != null)
                    mCallBack.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    if (mCallBack != null)
                        mCallBack.onSuccess(string);
                } else {
                    if (mCallBack != null)
                        mCallBack.onFail();
                }
            }
        });
    }

    /**
     * 上传多文件（通过hashmap存储多文件）
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param requestUrl 上传文件地址
     * @param paramsMap 本地多文件表
     * @param mCallBack 上传回调
     * @return
     */
    public void upLoadFile(String requestUrl,
                           HashMap<String, Object> paramsMap,
                           final UpLoadFileCallBack mCallBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            // set type
            builder.setType(MultipartBody.FORM);
            // add multi file params
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(),
                            RequestBody.create(null, file));
                }
            }
            // create RequestBody
            RequestBody body = builder.build();
            // create Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            // create call
            final Call call = mOkHttpClient.newCall(request);
            // start request
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (mCallBack != null)
                        mCallBack.onFail();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        if (mCallBack != null)
                            mCallBack.onSuccess(string);
                    } else {
                        if (mCallBack != null)
                            mCallBack.onFail();
                    }
                }
            });
        } catch (Exception e) {
            if (mCallBack != null)
                mCallBack.onException(e);
        }
    }

    /**
     * 创建带进度的RequestBody
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param contentType MediaType
     * @param file  准备上传的文件
     * @param mCallBack 上传回调
     * @return
     */
    private RequestBody createProgressRequestBody(final MediaType contentType,
                                                 final File file,
                                                 final UpLoadFileCallBack mCallBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        if (mCallBack != null)
                            mCallBack.onProgress(remaining, current);
                    }
                } catch (Exception e) {
                    if (mCallBack != null)
                        mCallBack.onException(e);
                }
            }
        };
    }

    /**
     * 上传多文件带进度回调（通过hashmap存储多文件）
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param requestUrl 上传文件地址
     * @param paramsMap 本地多文件表
     * @param mCallBack 上传回调
     * @return
     */
    public void upLoadFileOnProgress(String requestUrl,
                           HashMap<String, Object> paramsMap,
                           final UpLoadFileCallBack mCallBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            // set type
            builder.setType(MultipartBody.FORM);
            // add multi file params
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(),
                            createProgressRequestBody(MediaType.parse("application/octet-stream"),
                                    file, mCallBack));
                }
            }
            // create requestBody
            RequestBody body = builder.build();
            // create request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            // create call
            final Call call = mOkHttpClient.newCall(request);
            // start request
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (mCallBack != null)
                        mCallBack.onFail();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        if (mCallBack != null)
                            mCallBack.onSuccess(string);
                    } else {
                        if (mCallBack != null)
                            mCallBack.onFail();
                    }
                }
            });
        } catch (Exception e) {
            if (mCallBack != null)
                mCallBack.onException(e);
        }
    }

    /**
     * 下载文件
     * @author leibing
     * @createTime 2017/5/6
     * @lastModify 2017/5/6
     * @param fileUrl 下载文件地址
     * @param destFileDir 本地存储目标目录
     * @param mCallBack 下载回调
     * @return
     */
    public void downLoadFile(String fileUrl,
                                 final String destFileDir,
                                 final UpLoadFileCallBack mCallBack) {
        // 对下载文件地址md5得到下载文件名
        final String fileName = MD5Util.md5Encode(fileUrl, "UTF-8");
        // 判断下载文件是否存在
        final File file = new File(destFileDir, fileName);
        if (file.exists()
                && mCallBack != null) {
            mCallBack.onSuccess(HAS_THE_FILE_EXISTS);
            return;
        }
        // create request
        final Request request = new Request.Builder().url(fileUrl).build();
        // create call
        final Call call = mOkHttpClient.newCall(request);
        // start request
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mCallBack != null){
                    mCallBack.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                StringBuilder mStringBuilder = new StringBuilder();
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        mStringBuilder.append(buf);
                        fos.write(buf, 0, len);
                        if (mCallBack != null)
                            mCallBack.onProgress(total, current);
                    }
                    fos.flush();
                    if (mCallBack != null)
                        mCallBack.onSuccess(mStringBuilder.toString());
                } catch (IOException e) {
                    if (mCallBack != null)
                        mCallBack.onFail();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        if (mCallBack != null)
                            mCallBack.onException(e);
                    }
                }
            }
        });
    }
}
