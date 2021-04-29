package com.example.witsonline;
import android.app.Activity;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PHPRequestBuilder {
    String URL;

    public PHPRequestBuilder (String url, String method) {
        URL = url + method + ".php";
    }

    public void doBuild(ArrayList<ArrayList<String>> Parameters) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(URL)).newBuilder();

        for (ArrayList<String> parameter : Parameters) {
            urlBuilder.addQueryParameter(parameter.get(0), parameter.get(1));
        }

        URL = urlBuilder.build().toString();
    }

    public void doRequest(Activity activity, ResponseHandler responseHandler) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = Objects.requireNonNull(response.body()).string();
                    activity.runOnUiThread(() -> {
                        try {
                            responseHandler.processResponse(myResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
