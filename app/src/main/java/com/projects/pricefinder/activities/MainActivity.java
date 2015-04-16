package com.projects.pricefinder.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.projects.pricefinder.R;
import com.projects.pricefinder.dal.provider.ProductDBProvider;
import com.projects.pricefinder.entities.Result;
import com.projects.pricefinder.util.CSEService;
import com.projects.pricefinder.util.CustomAdapter;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;


import com.projects.pricefinder.models.*;
import com.projects.pricefinder.util.UPCAPIService;


public class MainActivity extends ActionBarActivity  {

    private static ProductDBProvider productDB;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private int startIdx=1;
    private String keyword;
    private ArrayList<Product> productList;
    private CustomAdapter customAdapter ;
    private ListView resultListView;
    private CSEService cse;
    private UPCAPIService upcAS;
    private UPC upc;
    private Result result;
    private Button btnLoadMore;
    private EditText txtResult;
    private Menu menuMain;
    private MenuItem menuItemSortPrice;
    private MenuItem menuItemSortName;

    final Handler handler = new Handler();
    final Runnable updateUPCKeywordItem = new Runnable(){
        @Override
        public void run(){
            updateUPCKeywordInUI();
        }
    };
    final Runnable updateItems = new Runnable(){
        @Override
        public void run(){
            updateItemsInUI();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultListView = (ListView) findViewById(R.id.resultListView);
        result = new Result();
        cse = new CSEService(getBaseContext());
        upcAS = new UPCAPIService(getBaseContext());
        txtResult = (EditText) findViewById(R.id.txtSearch);
        txtResult.setText("");
        btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
        btnLoadMore.setVisibility(View.INVISIBLE);
        customAdapter = new CustomAdapter(this);
        resultListView.setAdapter(customAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3)
            {
                Product product = (Product)arg0.getItemAtPosition(arg2);
                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("ProductDisplay", product);

                startActivity(intent);

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9DC76A")));


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            final String scanContent = scanningResult.getContents();
            final  String scanFormat = scanningResult.getFormatName();
            upc = new UPC();
            upc.setUpc( scanContent);

            Thread tUPC = new Thread(){
                public void run(){
                    try {
                        upc = searchUPC(upc.getUpc());
                        handler.post(updateUPCKeywordItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            tUPC.start();
        }

         else  if(resultCode == RESULT_OK) {
            ArrayList<String> textMatchList = intent
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!textMatchList.isEmpty()) {
               // upc.setUpc( textMatchList.get(0));
                txtResult.setText( textMatchList.get(0));
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menuMain = menu;
        menuItemSortPrice = menu.findItem(R.id.action_sort_by_price);
        menuItemSortName = menu.findItem(R.id.action_sort_by_name);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan) {
            upc = new UPC();
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            return true;
        }
        else if (id == R.id.action_voice){

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                    .getPackage().getName());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, txtResult.getText()
                    .toString());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
            return true;
        }
        else if (id == R.id.action_sort_by_price){
            customAdapter.sortByPriceAsc();
            productList = new ArrayList<Product>(customAdapter.getItems());
            boolean sortP =customAdapter.isPriceSortAsc();
            boolean sortN =customAdapter.isNameSortAsc();
            customAdapter = new CustomAdapter(this,productList,sortP,sortN);
            resultListView.setAdapter(customAdapter);
            return true;
        }
        else if (id == R.id.action_sort_by_name){
            customAdapter.sortByNameAsc();
            productList = new ArrayList<Product>(customAdapter.getItems());
            boolean sortP =customAdapter.isPriceSortAsc();
            boolean sortN =customAdapter.isNameSortAsc();
            customAdapter = new CustomAdapter(this,productList,sortP,sortN);
            resultListView.setAdapter(customAdapter);
            return true;
        }
        /*else if (id == R.id.action_my_favorites){

            return true;
        }*/
        else if(id==R.id.action_settings){
            Intent intent = new Intent(this,UserSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(result==null || 0 == result.getItems().size())
            menuItemSortPrice.setVisible(false);
            menuItemSortName.setVisible(false);
        return true;
    }

    protected UPC searchUPC(String keyword){
        UPC upc = new UPC();
        try {
            upc = upcAS.Search(keyword);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  upc;
    }

    protected Result searchResult(String keyword){
        Result newResult = new Result();
        try {
            newResult = cse.Search(keyword,getStartIdx(),getPrefence());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  newResult;
    }

    public String getPrefence(){
        boolean ret=false;
        String CountryCodes ="";
        SharedPreferences appPrefs =
                getSharedPreferences("appPreferences", MODE_PRIVATE);
        try {
            CountryCodes = appPrefs.getBoolean("countryCA", ret) ? "countryCA" : "";
            if (appPrefs.getBoolean("countryUS", ret)){
                if (!CountryCodes.equalsIgnoreCase("") ){
                    CountryCodes += "|";
                }
                CountryCodes += "countryUS";
            }
        }
        catch (ClassCastException e){
            CountryCodes = "countryCA";
        }
        if (CountryCodes==""){CountryCodes ="countryCA"; }
        return CountryCodes;
    }

    protected void updateUPCKeywordInUI() {
        String keyword = "Code does not exist";
        if(upc!=null){
            keyword = upc.getValid().equalsIgnoreCase("true")? upc.getAlias(): upc.getReason();
        }
        txtResult.setText(keyword);
    }
    protected void updateItemsInUI() {
        if(result==null || 0 == result.getItems().size()) {
            Toast.makeText(this, "NOT Found", Toast.LENGTH_SHORT).show();
            menuItemSortPrice.setVisible(false);
            menuItemSortName.setVisible(false);
        }
        else {
            if(result.getItems().size()>0){
                menuItemSortPrice.setVisible(true);
                menuItemSortName.setVisible(true);
                for(int i=0;i<result.getItems().size();i++){
                    Product p = new Product();
                    p.setId(getStartIdx()+i);
                    p.setName(result.getItems().get(i).getTitle());
                    p.setDesc(result.getItems().get(i).getHtmlSnippet());
                    p.setUrl(result.getItems().get(i).getLink());

                    try {
                        String price = result.getItems().get(i).getPagemap().getOffer().getPrice().toString().replace("$", "");

                        p.setPrice(Double.parseDouble(price));
                    }
                    catch ( Exception e){}
                    try {
                        p.setPricecurrency(result.getItems().get(i).getPagemap().getOffer().getPricecurrency());
                    }
                    catch ( Exception e){}
                    try {
                        p.setCountry(result.getItems().get(i).getPagemap().getOffer().getCountry());
                    }
                    catch ( Exception e){}
                    try {
                        String imageUri = result.getItems().get(i).getPagemap().getCSE_thumbnail().getSRC();
                        if (!imageUri.isEmpty()) p.setImageUrl(imageUri);
                    }
                    catch ( Exception e){}

                    customAdapter.addItem(p);

                }
                setStartIdx(getStartIdx() + 10);
            }


        }
    }

    public void onClickSearch(View view) throws IOException, JSONException {

        customAdapter.clear();
        Thread t = new Thread(){
            public void run(){
                String keyword = ((TextView)findViewById(R.id.txtSearch)).getText().toString();
                setKeyword(keyword);    //reset keyword
                setStartIdx(1);         //reset index
                result = null;
                result = searchResult(keyword);

                handler.post(updateItems);

            }
        };
        Toast.makeText(this,"Searching....",Toast.LENGTH_SHORT).show();
        t.start();
        hideInputMethod();
        if (btnLoadMore.getVisibility()==View.INVISIBLE){ btnLoadMore.setVisibility(View.VISIBLE);}

    }
    public void onClickLoadMore(View view) throws IOException, JSONException {
        result = null;
        Thread tLM = new Thread(){
            public void run(){
                String keyword = ((TextView)findViewById(R.id.txtSearch)).getText().toString();
                result = searchResult(keyword);
                handler.post(updateItems);
            }
        };
        Toast.makeText(this,"Loading....",Toast.LENGTH_SHORT).show();
        tLM.start();
        hideInputMethod();
    }


    private void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((TextView)findViewById(R.id.txtSearch)).getWindowToken(), 0);
    }


    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
