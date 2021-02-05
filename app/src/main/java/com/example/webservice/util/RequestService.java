package com.example.webservice.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestService {

    public String post(String url, String json) {

        // Header
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        try {
            String response = HttpUtils.postContents(url, headers, json);
            if (response != null)
                return response;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String postMultipartFormData(String url, Map formParameters, ByteArrayOutputStream byteArrayOutputStream) {

        try {
            String response = HttpUtils.postMultipartFormData(url, formParameters, byteArrayOutputStream);
            if (response != null)
                return response;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String postMultipartFormDataMedia(String url, String fileName, Map formParameters, ByteArrayOutputStream byteArrayOutputStream) {

        try {
            String response = HttpUtils.postMultipartFormDataMedia(url, fileName, formParameters, byteArrayOutputStream);
            if (response != null)
                return response;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String get(String url){
        // Header
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Accept", "application/json; charset=utf-8");


        return HttpUtils.get(headers,url);
    }

    public String getWithAccessToken(String url, String accessToken){
        // Header
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+accessToken);


        return HttpUtils.get(headers,url);
    }
}
