package com.example.aldeberan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.aldeberan.R;
import com.example.aldeberan.UserFragment.UserSettings.UserAddAddressFragment;
import com.example.aldeberan.UserFragment.UserSettings.UserAddressFragment;
import com.example.aldeberan.UserFragment.UserSettings.UserUpdateAddressFragment;

public class AddressSelectionToBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection_to_book);

        getSupportActionBar().setTitle("Address Book");
    }

    public void setTitleBar(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            finish();
            Intent addressIntent = new Intent(AddressSelectionToBook.this, AddressSelection.class);
            startActivity(addressIntent);
        }
        else {
            final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.addressFragmentView);
            if (fragmentInFrame instanceof UserAddressFragment){
                super.onBackPressed();
                finish();
                Intent addressIntent = new Intent(AddressSelectionToBook.this, AddressSelection.class);
                startActivity(addressIntent);
            }
            else{
                setTitleBar("Address Book");
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}