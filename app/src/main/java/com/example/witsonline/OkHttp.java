package com.example.witsonline;

import okhttp3.*;

import java.io.IOException;

public class OkHttp {

    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();

    public void validateURL(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }
    }

}
