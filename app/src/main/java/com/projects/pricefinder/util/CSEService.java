package com.projects.pricefinder.util;

import android.content.Context;
import android.util.Log;
import com.priefinder.projects.R;
import com.projects.pricefinder.entities.Item;
import com.projects.pricefinder.entities.Result;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiao on 02/23/2015.
 */
public class CSEService {
    private String API_KEY;
    private String baseUrl;
    private String CX;

    public CSEService(Context ctx){
        API_KEY =  ctx.getString(R.string.CSE_APIKey);
        baseUrl = ctx.getString(R.string.CSE_BaseURL);
        CX = ctx.getString(R.string.CSE_CX);

    }
    public  URL bundleUrl(String keyword) throws Exception{
        return new URL(baseUrl + "?key=" + API_KEY + "&cx=" + CX + "&q=" + URLEncoder.encode(keyword) + "&alt=json");
    }

    public synchronized Result Search(String keyword) throws Exception {
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
                    Log.i("return-object", responseResult);
                    return deserialize(responseResult);
            }

            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public static Result deserialize(String object) throws JSONException
    {
        Result result = new Result();
        final List<Item> items = new ArrayList<Item>();

        JSONObject jSonObject = new JSONObject(object);
        JSONArray jSonObjectArray = jSonObject.getJSONArray("items");

        for(int count = 0; count < jSonObjectArray.length();count++){
            JSONObject jsonItem = (JSONObject) jSonObjectArray.get(count);
            Item item = new Item();
            item.setTitle(jsonItem.getString("title"));
            item.setHtmlTitle(jsonItem.getString("htmlTitle"));
            item.setLink(jsonItem.getString("link"));
            item.setDisplayLink(jsonItem.getString("displayLink"));
            item.setSnippet(jsonItem.getString("snippet"));
            item.setHtmlSnippet(jsonItem.getString("htmlSnippet"));
            items.add(item);
        }
        result.setItems(items);
        return result;
    }
}
