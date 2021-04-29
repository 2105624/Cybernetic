package com.example.witsonline;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PHPRequest {
    String url;
    public PHPRequest(String prefix){
        url = prefix;
    }

    public void doRequest(final Activity a, String method, final ResponseHandler rh){
        OkHttpClient client = new OkHttpClient();

       /* HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("username","pravesh");
        url = urlBuilder.build().toString(); */

        Request request = new Request.Builder()
                .url(url+method+".php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){

                    final String myResponse = response.body().string();


                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                rh.processResponse(myResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }
}
