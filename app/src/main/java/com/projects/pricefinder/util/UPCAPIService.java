package com.projects.pricefinder.util;

import android.content.Context;
import android.util.Log;

import com.projects.pricefinder.R;
import com.projects.pricefinder.models.UPC;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 04/16/2015.
 */
public class UPCAPIService {

    private String API_KEY;
    String baseUrl;
    private static boolean DEBUG = false;

    public UPCAPIService(Context ctx){
        API_KEY =  ctx.getString(R.string.UPC_APIKey);
        baseUrl =  ctx.getString(R.string.UPC_BaseURL);
    }

    public  URL bundleUrl(String keyword) throws Exception{
        return new URL(baseUrl +API_KEY+"/"+ keyword);
    }
    //http://api.upcdatabase.org/json/c588c0a459f4ccc6f3dd26518d24707a/0111222333446
    public synchronized UPC Search(String keyword) throws Exception {
        String responseResult = "";

        try {
            URL URL =  bundleUrl(keyword.trim());
            HttpURLConnection conn = (HttpURLConnection) (URL).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int status = conn.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    InputStream inputStream = conn.getInputStream();
                    responseResult = convertStreamToString(inputStream);
                    if(DEBUG) Log.i("return-object", responseResult);

                    conn.disconnect();
                    return deserializeResult(responseResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deserializes Result
     *
     * Valid:
     *  <number>0111222333446</number>
     *  <itemname>UPC Database Testing Code</itemname>
     *  <alias>Testing Code</alias>
     *  <description>http://upcdatabase.org/code/0111222333446</description>
     *  <avgprice>123.45</avgprice>
     *  <rate_up>2</rate_up>
     *  <rate_down>0</rate_down>
     *
     *  Invalid
     *  <valid>false</valid>
     *  <error>301</error>
     *  <reason>Code does not exist!</reason>
     *
     * @param object
     * @return UPC
     * @throws JSONException
     */
    public static UPC deserializeResult(String object) throws JSONException
    {
        UPC result = new UPC();
        if (!object.isEmpty()) {

            JSONObject jsonObject = new JSONObject(object);
            String v = jsonObject.getString("valid");
            if (v.equalsIgnoreCase("true")){
                try {
                    result.setUpc(jsonObject.getString("number"));
                    result.setItemname(jsonObject.getString("itemname"));
                    result.setAlias(jsonObject.getString("alias"));
                    result.setDescription(jsonObject.getString("description"));
                    result.setAvg_price(jsonObject.getString("avg_price"));
                    result.setRate_up(jsonObject.getString("rate_up"));
                    result.setRate_down(jsonObject.getString("rate_down"));
                }
                catch (JSONException e){
                    if(DEBUG) Log.d("UPCAPIService::deserializeResult()",
                            e.getMessage());
                }
            }
            else{
                result.setError( jsonObject.getString("error"));
                result.setReason( jsonObject.getString("reason"));

            }
            result.setValid(v);
        }
        return result;

    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
