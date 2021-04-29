package com.example.witsonline;

import org.json.JSONException;

public interface ResponseHandler {
    public abstract void processResponse(String response) throws JSONException;
}
