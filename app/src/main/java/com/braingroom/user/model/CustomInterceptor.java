package com.braingroom.user.model;

import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.Constants;
import com.google.gson.Gson;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static android.content.ContentValues.TAG;


public class CustomInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    Gson gson;

    int versionCode = UserApplication.versionCode;

    // InternetConnection internetConnection = new InternetConnection(UserApplication.getInstance());

    @Inject
    public CustomInterceptor(Gson gson) {
        this.gson = gson;
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("X-App-Type", "BGUSR01")
                .addHeader("X-App-Version", versionCode + "")
                .addHeader("X-App-Geo", Constants.GEO_TAG);
        Request request = requestBuilder.build();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (ConnectException e) {
            //  e.printStackTrace();

            Log.d(TAG, "intercept: no network " + e.getLocalizedMessage());
            UserApplication.getInstance().getInternetStatusBus().onNext(false);
            throw e;
        } catch (SocketTimeoutException e) {
            Log.d(TAG, "intercept: no network " + e.getLocalizedMessage());
            UserApplication.getInstance().getInternetStatusBus().onNext(false);
            throw e;
        } catch (UnknownHostException e) {
            Log.d(TAG, "intercept: no network " + e.getLocalizedMessage());
            UserApplication.getInstance().getInternetStatusBus().onNext(false);
            throw e;
        }
        if (request.url().toString().endsWith("getSegment")) {
            String categoryId = request.header("categoryId");
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String newResponse = null;
                try {
                    SegmentResp segmentResp = gson.fromJson(buffer.clone().readString(charset), SegmentResp.class);
                    segmentResp.setCategoryId(categoryId);
                    newResponse = gson.toJson(segmentResp);
                } catch (Exception e) {
                }
                return response.newBuilder().body(ResponseBody.create(responseBody.contentType(), newResponse.getBytes())).build();
            }

        }
        return response;
    }

    boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}


