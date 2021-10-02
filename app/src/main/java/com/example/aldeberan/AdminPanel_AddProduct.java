package com.example.aldeberan;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aldeberan.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminPanel_AddProduct extends AppCompatActivity implements View.OnClickListener{

    ProductModel pm = new ProductModel();
    public Uri imgURI;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel_add_product);

        //Firebase Cloud Storage reference
        storageRef = FirebaseStorage.getInstance().getReference();

        //Submit Button
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

        //PickImg Button
        Button imgBtn = findViewById(R.id.imageBtn);
        imgBtn.setOnClickListener(this);

        //Set SKU when product name on change
        EditText prodName = findViewById(R.id.prodName);
        EditText prodSKU = findViewById(R.id.prodSKU);

        //Regex


        prodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                prodSKU.setText(regexValidator(prodName.getText().toString()));
            }
        });

    }

    private void pickImgFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("requestCode", 102);
        activityResultLauncher.launch(intent);
    }

    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int readExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getResultCode() == 100){
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
                            if (Environment.isExternalStorageManager()){
                                pickImgFromGallery();
                            }
                        }
                    }
                    else{
                        takePermissions();
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgURI = data.getData();
        if (imgURI != null){
            ImageView img = findViewById(R.id.prodImg);
            img.setImageURI(imgURI);
        }
    }

    private void takePermissions(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package%s", getApplicationContext().getPackageName())));
                intent.putExtra("requestCode", 100);
                activityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("requestCode", 100);
                activityResultLauncher.launch(intent);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
        if (view.getId() == R.id.submitBtn) {
            TextView prodNameLbl = findViewById(R.id.prodName);
            TextView prodSKULbl = findViewById(R.id.prodSKU);
            TextView prodStockLbl = findViewById(R.id.prodStock);
            TextView prodPriceLbl = findViewById(R.id.prodPrice);
            Switch prodAvailSwitch = findViewById(R.id.prodAvail);
            int prodAvail = prodAvailSwitch.isChecked() ? 1 : 0;

            String prodName = prodNameLbl.getText().toString();
            String prodSKU = prodSKULbl.getText().toString();
            int prodStock = Integer.parseInt(prodStockLbl.getText().toString());
            Double prodPrice = Double.parseDouble(prodPriceLbl.getText().toString());

            if (prodName != null && prodStock != 0 && prodPrice != null && imgURI != null){
                if (imgURI != null) {
                    StorageReference childRef = storageRef.child("products/" + prodSKU + "." + getFileExt(imgURI));

                    childRef.putFile(imgURI).continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return childRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String prodImg = downloadUri.toString();
                            pm.addProduct(prodName, prodSKU, prodAvail, prodStock, prodPrice, prodImg);
                            Log.i("UP","Upload success: " + downloadUri);
                        } else {
                            Log.i("UP","Upload failed: ");
                        }
                    });
                } else {
                    Toast.makeText(this, "Please select an image!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "All inputs are required!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            if (isPermissionGranted()){
                pickImgFromGallery();
            }
            else{
                takePermissions();
            }
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private String regexValidator(String input){
        char[] charArr = input.toLowerCase().toCharArray();
        String output = "";
        for (int i=0; i<charArr.length;i++) {
            if (charArr[i] == 'a' || charArr[i] == 'b' || charArr[i] == 'c' ||
                    charArr[i] == 'd' || charArr[i] == 'e' || charArr[i] == 'f' ||
                    charArr[i] == 'g' || charArr[i] == 'h' || charArr[i] == 'i' ||
                    charArr[i] == 'j' || charArr[i] == 'k' || charArr[i] == 'l' ||
                    charArr[i] == 'm' || charArr[i] == 'n' || charArr[i] == 'o' ||
                    charArr[i] == 'p' || charArr[i] == 'q' || charArr[i] == 'r' ||
                    charArr[i] == 's' || charArr[i] == 't' || charArr[i] == 'u' ||
                    charArr[i] == 'v' || charArr[i] == 'w' || charArr[i] == 'x' ||
                    charArr[i] == 'y' || charArr[i] == 'z' || charArr[i] == '0' ||
                    charArr[i] == '1' || charArr[i] == '2' || charArr[i] == '3' ||
                    charArr[i] == '4' || charArr[i] == '5' || charArr[i] == '6' ||
                    charArr[i] == '7' || charArr[i] == '8' || charArr[i] == '9') {
                output += String.valueOf(charArr[i]);
            }
            else{
                output += "_";
            }
        }
        return output;
    }
}