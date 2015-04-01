package com.projects.pricefinder.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
import com.projects.pricefinder.entities.Result;
import com.projects.pricefinder.util.CSEService;
import com.projects.pricefinder.util.CustomAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private ListView resultListView;
    private CSEService cse;
    private Result result;
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private EditText txtResult;
    final Handler handler = new Handler();
    final Runnable updateItems = new Runnable(){
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
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        txtResult = (EditText) findViewById(R.id.txtSearch);
        txtResult.setText("");
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            txtResult.setText(scanContent);
        }

         else  if(resultCode == RESULT_OK) {
            ArrayList<String> textMatchList = intent
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!textMatchList.isEmpty()) {
                txtResult.setText( textMatchList.get(0));
            }
        }
    }



    public void speak(View view){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, txtResult.getText()
                .toString());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected Result searchResult(String keyword){
        Result newResult = new Result();
        try {
            newResult = cse.Search(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newResult;
    }

    protected void updateItemsInUI() {
        if(result==null || 0 == result.getItems().size()) {
            Toast.makeText(this, "NOT Found", Toast.LENGTH_SHORT).show();
        }
        else {
            CustomAdapter CustomAdapter = new CustomAdapter(this, result.getItems());
            resultListView.setAdapter(CustomAdapter);
        }
    }

    public void onClickSearch(View view) throws IOException, JSONException {
        Thread t = new Thread(){
            public void run(){
                String keyword = ((TextView)findViewById(R.id.txtSearch)).getText().toString();
                result = searchResult(keyword);
                handler.post(updateItems);
            }
        };
        Toast.makeText(this,"Searching....",Toast.LENGTH_SHORT).show();
        t.start();
        hideInputMethod();

    }

    private void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((TextView)findViewById(R.id.txtSearch)).getWindowToken(), 0);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getItems().get(arg2).getLink()));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();

        }
    }
}
