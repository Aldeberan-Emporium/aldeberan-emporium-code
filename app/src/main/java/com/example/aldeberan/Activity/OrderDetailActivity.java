package com.example.aldeberan.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aldeberan.Adapter.OrderDetailAdapter;
import com.example.aldeberan.R;
import com.example.aldeberan.models.OrderModel;
import com.example.aldeberan.storage.UserStorage;
import com.example.aldeberan.structures.Order;

import java.io.Serializable;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements Serializable {
    private OrderModel om;
    private UserStorage us;
    private List<Order> orderList;
    private OrderDetailAdapter orderDetailAdapter;
    private RecyclerView recyclerView;
    private ImageView orderShippingImg;
    private ImageView orderDeliveredImg;
    private String orderStatus;
    private TextView orderStatusText;
    private TextView addressText;
    private TextView paymentMethodText;
    private TextView productTotalText;
    private TextView detailTotalPriceText;

    private int orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);
        recyclerView = findViewById(R.id.orderDetailRecyclerView);
        orderShippingImg = findViewById(R.id.orderShippingImg);
        orderDeliveredImg = findViewById(R.id.orderDeliveredImg);
        orderStatusText = findViewById(R.id.orderStatusText);
        addressText = findViewById(R.id.addressDetail);
        paymentMethodText = findViewById(R.id.paymentMethod);
        productTotalText = findViewById(R.id.productTotal);
        detailTotalPriceText = findViewById(R.id.detailTotalPrice);

        String address = (String) getIntent().getSerializableExtra ("address");
        String paymentMethod = (String) getIntent().getSerializableExtra ("paymentMethod");
        String productTotal = (String) getIntent().getSerializableExtra("productTotal");
        String orderTotal = (String) getIntent().getSerializableExtra("orderTotal");
        //double orderTotal = Integer.parseInt(productTotal) + 5;
        orderID = (int) getIntent().getSerializableExtra("orderID");

        System.out.println("lanjiao product total: " + productTotal);
        System.out.println("lanjiao order total: " + orderTotal);

        om = new OrderModel();

        String orderStatus = (String) getIntent().getSerializableExtra("orderStatus");

        System.out.println("lanjiao" + orderStatus);

        if (orderStatus.contains("shipping")){
            orderStatusText.setText("Your order is on the way!");
            orderShippingImg.setVisibility(View.VISIBLE);
            orderDeliveredImg.setVisibility(View.GONE);
        }
        else{
            orderStatusText.setText("Your order is completed!");
            orderShippingImg.setVisibility(View.GONE);
            orderDeliveredImg.setVisibility(View.VISIBLE);
        }

        addressText.setText(address);

        ConstructRecyclerView();

        paymentMethodText.setText(paymentMethod);
        productTotalText.setText(productTotal);
        detailTotalPriceText.setText(String.valueOf(orderTotal));
    }


    private void ConstructRecyclerView(){
        om = new OrderModel();
        us = new UserStorage(this);

        om.readOrderItem(orderID, response -> {
            orderList = response;
            PutDataIntoRecyclerView(response);
        });
    }

    public void PutDataIntoRecyclerView(List<Order> orderList) {
        orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderDetailAdapter);
    }

}
