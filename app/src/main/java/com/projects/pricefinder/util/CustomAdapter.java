package com.projects.pricefinder.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.pricefinder.R;
import com.projects.pricefinder.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CustomAdapter extends BaseAdapter {

    private LayoutInflater vi;
    private Context context;
    private ArrayList<Product> items;
    private  boolean priceSortAsc;
    private  boolean nameSortAsc;

    public CustomAdapter(Context context) {

        this.context = context;
        vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = new ArrayList<Product>();

        nameSortAsc = priceSortAsc = false;
    }
    public CustomAdapter(Context context, ArrayList<Product> items,boolean sp, boolean sn) {
        this.context = context;
        vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        setPriceSortAsc(sp);
        setNameSortAsc(sn);
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //RelativeLayout rowLayout;

        if (convertView == null) {
            convertView =   vi.inflate(R.layout.listviewitem, parent, false);
        }
        Product item = (Product) getItem(position);
        try {
            TextView itemTitle = (TextView) convertView.findViewById(R.id.textViewItemTitle);
            itemTitle.setText(item.getName() + "..");//.substring(0, 60)
        }
        catch ( Exception e){}
        try {
            Double price = item.getPrice();
            TextView itemPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
            itemPrice.setText( price.toString() );
        }
        catch ( Exception e){}

        try {
            TextView itemCountry = (TextView) convertView.findViewById(R.id.textViewCountry);
            itemCountry.setText(item.getCountry() );
        }
        catch ( Exception e){}

        try {
            ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
            String imageUri;
            imageUri = item.getImageUrl();
            if (!imageUri.isEmpty()){ new ImageLoadService(imageUri, itemImage).execute();}
        }
        catch ( Exception e){}

        return convertView;
    }
    public void addItem(Product item) {
        items.add(item);
        this.notifyDataSetChanged();
    }
    public void clear() {
        items.clear();
        this.notifyDataSetChanged();
    }
    // Sort by Price
    public void sortByPriceAsc() {
        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product l, Product r) {
                return (!priceSortAsc?
                        Double.compare(l.getPrice(), r.getPrice())
                        :Double.compare(r.getPrice(), l.getPrice()));
            }
        };
        try {
            Collections.sort(items, comparator);
        }
        catch (Exception e){
            Log.e("", e.toString());
        }
        this.notifyDataSetChanged();
        setPriceSortAsc(isPriceSortAsc() ? false:true);
    }
    // Sort by Name
    public void sortByNameAsc() {
        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product l, Product r) {
                return (!nameSortAsc?
                        l.getName().toString().compareToIgnoreCase( r.getName().toString())
                        :r.getName().toString().compareToIgnoreCase( l.getName().toString()));
            }
        };
        Collections.sort(items, comparator);
        this.notifyDataSetChanged();
        setNameSortAsc(isNameSortAsc() ? false:true);
    }

    public boolean isPriceSortAsc() {
        return priceSortAsc;
    }

    public void setPriceSortAsc(boolean priceSortAsc) {
        this.priceSortAsc = priceSortAsc;
    }

    public boolean isNameSortAsc() {
        return nameSortAsc;
    }

    public void setNameSortAsc(boolean nameSortAsc) {
        this.nameSortAsc = nameSortAsc;
    }
}