package com.projects.pricefinder.util;

import android.content.Context;
import android.util.Log;

import com.projects.pricefinder.R;
import com.projects.pricefinder.entities.*;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiao on 02/05/2015.
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

    /**
     * "template": "https://www.googleapis.com/customsearch/v1?q={searchTerms}&num={count?}
     * &start={startIndex?}&lr={language?}&safe={safe?}&cx={cx?}&cref={cref?}&sort={sort?}&filter={filter?}
     * &gl={gl?}&cr={cr?}&googlehost={googleHost?}&c2coff={disableCnTwTranslation?}&hq={hq?}&hl={hl?}
     * &siteSearch={siteSearch?}&siteSearchFilter={siteSearchFilter?}&exactTerms={exactTerms?}
     * &excludeTerms={excludeTerms?}&linkSite={linkSite?}&orTerms={orTerms?}
     * &relatedSite={relatedSite?}&dateRestrict={dateRestrict?}&lowRange={lowRange?}&highRange={highRange?}
     * &searchType={searchType}&fileType={fileType?}&rights={rights?}&imgSize={imgSize?}&imgType={imgType?}
     * &imgColorType={imgColorType?}&imgDominantColor={imgDominantColor?}&alt=json"

     * @param keyword
     * @return base URL
     * @throws Exception
     */
    public  URL bundleUrl(String keyword) throws Exception{
        return new URL(baseUrl + "?key=" + API_KEY + "&cx=" + CX
                + "&q=" + URLEncoder.encode(keyword)
                + "&alt=json");
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
                    return deserializeResult(responseResult);
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

    public static Result deserializeResult(String object) throws JSONException
    {
        Result result = new Result();
        final List<Item> items = new ArrayList<Item>();

        if (!object.isEmpty()) {
            JSONObject jsonObject = new JSONObject(object);
            JSONArray jsonObjectArray = jsonObject.getJSONArray("items");

            for (int j = 0; j < jsonObjectArray.length(); j++) {
                JSONObject jsonItem = (JSONObject) jsonObjectArray.get(j);
                Item item = new Item();
                item.setTitle(jsonItem.getString("title"));
                item.setHtmlTitle(jsonItem.getString("htmlTitle"));
                item.setLink(jsonItem.getString("link"));
                item.setDisplayLink(jsonItem.getString("displayLink"));
                item.setSnippet(jsonItem.getString("snippet"));
                item.setHtmlSnippet(jsonItem.getString("htmlSnippet"));
                //set pagemap
                Pagemap pagemap = new Pagemap();
                JSONObject jsonObjectPagemap = jsonItem.getJSONObject("pagemap");
                try {
                    JSONArray jsonObjectArrayCSE_Thumbnail = jsonObjectPagemap.getJSONArray("cse_thumbnail");
                    //set cse_thumbnail
                    for (int k = 0; k < 1; k++) {//jsonObjectArrayCSE_Thumbnail.length()

                        JSONObject jsonItemCSE_thumbnail = (JSONObject) jsonObjectArrayCSE_Thumbnail.get(k);
                        CSE_thumbnail cse_thumbnail = new CSE_thumbnail();
                        cse_thumbnail.setSRC(jsonItemCSE_thumbnail.getString("src"));
                        cse_thumbnail.setHeight(jsonItemCSE_thumbnail.getString("height"));
                        cse_thumbnail.setWidth(jsonItemCSE_thumbnail.getString("width"));

                        pagemap.setCSE_thumbnail(cse_thumbnail);
                    }
                }
                catch (JSONException e){
                    Log.d("CSEService::deserializeResult()","cse_thumbnail not found in the JSON object["+j+"]");
                }
                item.setPagemap(pagemap);
                items.add(item);
            }
            result.setItems(items);
        }
        return result;
    }
    public static Queries deserializeQueries(String object) throws JSONException
    {
        Queries queries = new Queries();

        if (!object.isEmpty()) {
            JSONObject jsonObject = new JSONObject(object);
            JSONObject jsonObjectQueries = jsonObject.getJSONObject("queries");
            JSONArray jsonObjectArrayNextPage = jsonObjectQueries.getJSONArray("nextPage");
            //set nextPage
            for (int count = 0; count < 1; count++) {//jsonObjectArrayNextPage.length()
                JSONObject jsonItem = (JSONObject) jsonObjectArrayNextPage.get(count);
                NextPage nextPage = new NextPage();
                nextPage.setTitle(jsonItem.getString("title"));
                nextPage.setCount(jsonItem.getInt("count"));
                nextPage.setTotalResults(jsonItem.getString("totalResults"));
                nextPage.setSearchTerms(jsonItem.getString("searchTerms"));
                nextPage.setStartIndex(jsonItem.getInt("startIndex"));
                nextPage.setInputEncoding(jsonItem.getString("inputEncoding"));
                nextPage.setOutputEncoding(jsonItem.getString("outputEncoding"));
                nextPage.setSafe(jsonItem.getString("safe"));
                nextPage.setCx(jsonItem.getString("cx"));
                queries.setNextPage(nextPage);
            }
            JSONArray jsonObjectArrayRequest = jsonObjectQueries.getJSONArray("request");
            //set nextPage
            for (int count = 0; count < 1; count++) {//jsonObjectArrayRequest.length()
                JSONObject jsonItem = (JSONObject) jsonObjectArrayRequest.get(count);
                Request request = new Request();
                request.setTitle(jsonItem.getString("title"));
                request.setCount(jsonItem.getInt("count"));
                request.setTotalResults(jsonItem.getString("totalResults"));
                request.setSearchTerms(jsonItem.getString("searchTerms"));
                request.setStartIndex(jsonItem.getInt("startIndex"));
                request.setInputEncoding(jsonItem.getString("inputEncoding"));
                request.setOutputEncoding(jsonItem.getString("outputEncoding"));
                request.setSafe(jsonItem.getString("safe"));
                request.setCx(jsonItem.getString("cx"));
                queries.setRequest(request);
            }
        }
        return queries;
    }
}
