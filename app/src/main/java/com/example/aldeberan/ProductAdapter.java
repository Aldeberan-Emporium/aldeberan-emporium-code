package com.example.aldeberan;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aldeberan.models.ProductModel;
import com.example.aldeberan.structures.Product;

import org.w3c.dom.Text;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Product> mData;
    ProductModel pm = new ProductModel();
    public int currentPos;

    public ProductAdapter(Context mContext, List<Product> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.updateBtn:
                Toast.makeText(mContext, "update btn clicked, id: " +currentPos, Toast.LENGTH_SHORT).show();
                //Show update screen
                break;
            case R.id.deleteBtn:
                Toast.makeText(mContext, "delete btn clicked, id: " +currentPos, Toast.LENGTH_SHORT).show();
                //Alert dialog to delete product
                //pm.deleteProduct(mData.get(currentPos).getProdID());
                break;
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView prodID;
        TextView prodName;
        TextView prodSKU;
        TextView prodAvail;
        TextView prodStock;
        TextView prodPrice;
        ImageView prodImg;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            prodID = itemView.findViewById(R.id.prodIDLbl);
            prodName = itemView.findViewById(R.id.prodNameLbl);
            prodSKU = itemView.findViewById(R.id.prodSKULbl);
            prodAvail = itemView.findViewById(R.id.prodAvailLbl);
            prodStock = itemView.findViewById(R.id.prodStockLbl);
            prodPrice = itemView.findViewById(R.id.prodPriceLbl);
            prodImg = itemView.findViewById(R.id.prodImgView);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.admin_panel_product_row, parent, false);

        view.findViewById(R.id.updateBtn).setOnClickListener(this);
        view.findViewById(R.id.deleteBtn).setOnClickListener(this);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        currentPos = position;
        holder.prodName.setText(mData.get(position).getProdName());
        holder.prodSKU.setText("SKU: " + mData.get(position).getProdSKU());
        holder.prodID.setText("ID: (" + mData.get(position).getProdID() + ")");

        String prodAvail = mData.get(position).getProdAvail() ? "Active" : "Inactive";

        holder.prodAvail.setText("Availability: " + prodAvail);
        holder.prodStock.setText("Stock: " + mData.get(position).getProdStock());
        holder.prodPrice.setText("Price: RM " + mData.get(position).getProdPrice());

        Glide.with(mContext).load(mData.get(position).getProdImg()).override(450, 450).into(holder.prodImg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
