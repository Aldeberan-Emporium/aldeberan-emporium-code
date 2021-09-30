package com.example.aldeberan.models;

import com.codepath.asynchttpclient.RequestParams;
import com.google.gson.Gson;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddressModel extends DatabaseModel{

    //Add address
    public void addAddress(String userID, String addRecipient, String addContact, String addLine1, String addLine2, String addCode, String addCity, String addState, String addCountry) throws JSONException, IOException {
        RequestParams params = new RequestParams();
        params.put("action", "addAddress");
        params.put("user_id", StringEscapeUtils.escapeHtml3(userID));
        params.put("address_recipient", StringEscapeUtils.escapeHtml3(addRecipient));
        params.put("address_contact", StringEscapeUtils.escapeHtml3(addContact));
        params.put("address_line1", StringEscapeUtils.escapeHtml3(addLine1));
        params.put("address_line2", StringEscapeUtils.escapeHtml3(addLine2));
        params.put("address_code", StringEscapeUtils.escapeHtml3(addCode));
        params.put("address_city", StringEscapeUtils.escapeHtml3(addCity));
        params.put("address_state", StringEscapeUtils.escapeHtml3(addState));
        params.put("address_country", StringEscapeUtils.escapeHtml3(addCountry));
        this.postData(params);
    }

    //Update address
    public void updateAddress(int addID, String userID, String addRecipient, String addContact, String addLine1, String addLine2, String addCode, String addCity, String addState, String addCountry) throws JSONException, IOException {
        RequestParams params = new RequestParams();
        params.put("action", "updateAddress");
        params.put("address_id", addID);
        params.put("user_id", StringEscapeUtils.escapeHtml3(userID));
        params.put("address_recipient", StringEscapeUtils.escapeHtml3(addRecipient));
        params.put("address_contact", StringEscapeUtils.escapeHtml3(addContact));
        params.put("address_line1", StringEscapeUtils.escapeHtml3(addLine1));
        params.put("address_line2", StringEscapeUtils.escapeHtml3(addLine2));
        params.put("address_code", StringEscapeUtils.escapeHtml3(addCode));
        params.put("address_city", StringEscapeUtils.escapeHtml3(addCity));
        params.put("address_state", StringEscapeUtils.escapeHtml3(addState));
        params.put("address_country", StringEscapeUtils.escapeHtml3(addCountry));
        this.postData(params);
    }

    //Delete address
    public void deleteAddress(int addID) throws JSONException, IOException {
        RequestParams params = new RequestParams();
        params.put("action", "deleteAddress");
        params.put("address_id", addID);
        this.postData(params);
    }
    /*
    //Read address by user
    public JSONObject readAddressByUser(String userID) throws JSONException, IOException {
        RequestParams params = new RequestParams();
        params.put("action", "readAddressByUser");
        params.put("user_id", StringEscapeUtils.escapeHtml3(userID));
        JSONObject data = new Gson().fromJson(this.postData(json), JSONObject.class);
        return data;
    }

     */
}
