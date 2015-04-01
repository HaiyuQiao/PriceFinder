package com.projects.pricefinder.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.projects.pricefinder.R;

public class ViewSiteActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressDialog progDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_site);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9DC76A")));
        progDailog = ProgressDialog.show(this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("productSite");
            setTitle(url.substring(0,30));
            webView = (WebView) findViewById(R.id.productWebView);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyAppWebViewClient());

            webView.loadUrl(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_site, menu);
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

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    private class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progDailog.show();
            if(Uri.parse(url).getHost().length() == 0) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            progDailog.dismiss();
        }
    }
}
