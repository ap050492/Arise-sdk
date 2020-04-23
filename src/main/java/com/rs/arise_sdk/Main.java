package com.rs.arise_sdk;

import android.os.AsyncTask;
import android.util.Log;

import org.hyperledger.indy.sdk.wallet.Wallet;

public class Main {

    public void createWallet(String configJson, String credentialsJson) {
        try {
            Wallet.createWallet(configJson, credentialsJson).get();
        } catch (Exception e) {
            Log.d("Hi_createWallet",e.toString());
        }
    }

}
