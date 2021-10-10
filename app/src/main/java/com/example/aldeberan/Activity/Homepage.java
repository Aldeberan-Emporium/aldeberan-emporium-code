package com.example.aldeberan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aldeberan.Adapter.ProductListingDetailAdapter;
import com.example.aldeberan.Adapter.ProductListingDetailVerticalAdapter;
import com.example.aldeberan.R;
import com.example.aldeberan.UserFragment.homeProductFragment;
import com.example.aldeberan.models.CartModel;
import com.example.aldeberan.models.ProductModel;
import com.example.aldeberan.storage.UserStorage;
import com.example.aldeberan.structures.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    List<Product> productList;
    ProductListingDetailAdapter adapterBS;
    ProductListingDetailVerticalAdapter adapterNA;
    RecyclerView bestSellerBox;
    RecyclerView newArrivalBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getSupportActionBar().hide();

        bestSellerBox = findViewById(R.id.bestSellerBox);
        newArrivalBox = findViewById(R.id.newArrivalBox);
        productList = new ArrayList<>();

        ConstructRecyclerView();
    }

    private void ConstructRecyclerView(){
        ProductModel pm = new ProductModel();
        try {
            pm.readProductAll((response) -> {
                productList = response;
                PutDataIntoBestSellerBox(response);
                PutDataIntoNewArrivalBox(response);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ProductListingDetailAdapter.FragmentCommunication bestSellerComm = (prodName, prodID, prodImg, prodPrice) -> {
        homeProductFragment homepage = new homeProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodName", prodName);
        bundle.putString("prodID", prodID);
        //bundle.putString("prodSKU", prodSKU);
        bundle.putString("prodImg", prodImg);
        //bundle.putString("prodStock", prodStock);
        //bundle.putString("prodAvail", prodAvail);
        bundle.putString("prodPrice", prodPrice);
        homepage.setArguments(bundle);
    };

    private void PutDataIntoBestSellerBox(List<Product> productList){
        adapterBS = new ProductListingDetailAdapter(this, productList, bestSellerComm);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bestSellerBox.setLayoutManager(horizontalLayoutManagaer);
        bestSellerBox.setAdapter(adapterBS);
        Log.i("PLOPE", String.valueOf(productList));
    }

    ProductListingDetailVerticalAdapter.FragmentCommunication newArrivalComm = (prodName, prodID, prodImg, prodPrice) -> {
        homeProductFragment homepage = new homeProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("prodName", prodName);
        bundle.putString("prodID", prodID);
        //bundle.putString("prodSKU", prodSKU);
        bundle.putString("prodImg", prodImg);
        //bundle.putString("prodStock", prodStock);
        //bundle.putString("prodAvail", prodAvail);
        bundle.putString("prodPrice", prodPrice);
        homepage.setArguments(bundle);
    };

    private void PutDataIntoNewArrivalBox(List<Product> productList){
        adapterNA = new ProductListingDetailVerticalAdapter(this, productList, newArrivalComm);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateScreenWidth());
        newArrivalBox.setLayoutManager(gridLayoutManager);
        newArrivalBox.setAdapter(adapterNA);
        Log.i("PLOPE", String.valueOf(productList));
        calculateScreenWidth();
    }

    private int calculateScreenWidth () {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Log.i("SCREENWIDTH", String.valueOf(width));
        int cardWidth = 540;
        return width/cardWidth;
    }
}