package com.projects.pricefinder.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.projects.pricefinder.R;
import com.projects.pricefinder.models.Product;
import com.projects.pricefinder.util.ImageLoadService;

import org.json.JSONException;

import java.io.IOException;

public class ProductDetailsActivity extends ActionBarActivity {
    private Product product;

    private TextView title;
    private TextView price;
    private ImageView image;
    private TextView desc;
    private Button btnGoSite;
    private Button bntAddtoFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9DC76A")));

        setProduct((Product) getIntent().getSerializableExtra("ProductDisplay"));
        title = (TextView) findViewById(R.id.textViewProductName);
        price = (TextView) findViewById(R.id.textViewProductPrice);
        desc = (TextView) findViewById(R.id.textViewDescription);
        image = (ImageView) findViewById(R.id.itemImageView);
        btnGoSite = (Button) findViewById(R.id.btnGoSite);
        bntAddtoFavorite= (Button) findViewById(R.id.bntAddtoFavorite);
        if (getProduct() != null) {
            title.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            desc.setText(product.getDesc());
            if(product.isInterest()){
                bntAddtoFavorite.setText("Remove Favorite");
            }
            try {
                String imageUri;
                imageUri = product.getImageUrl();
                if (!imageUri.isEmpty()){ new ImageLoadService(imageUri, image).execute();}
            }
            catch ( Exception e){}

        }
        bntAddtoFavorite.setVisibility(View.INVISIBLE);

    }
    public void onClickViewSite(View view) throws IOException, JSONException {
        String url = product.getUrl();
        Intent viewSiteIntent = new Intent(this, ViewSiteActivity.class);
        viewSiteIntent.putExtra("productSite", url);
        startActivity(viewSiteIntent);
    }
    public void onClickAddtoFavorite(View view) throws IOException, JSONException {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_details, menu);
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

    public Product getProduct()
    {
        return this.product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

}
