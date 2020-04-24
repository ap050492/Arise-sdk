package com.rs.arise_sdk;

import android.os.AsyncTask;
import android.os.Environment;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.LibIndy;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidResults;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Arnima {

    public static String createWallet(String id, String key) {
        try {
            String myWalletConfig = new JSONObject().put("id", id).toString();
            String myWalletCredentials= new JSONObject().put("key", key).toString();
            Wallet.createWallet(myWalletConfig, myWalletCredentials).get();
            Wallet myWallet = Wallet.openWallet(myWalletConfig, myWalletCredentials).get();

            DidResults.CreateAndStoreMyDidResult createMyDidResult = Did.createAndStoreMyDid(myWallet, "{}").get();
            String did = createMyDidResult.getDid();

            myWallet.closeWallet().get();
            return  did;
        } catch (Exception e) {
            IndySdkRejectResponse rejectResponse = new IndySdkRejectResponse(e);
            return rejectResponse.getMessage();
        }
    }


    public static class IndySdkRejectResponse {
        private String code;
        private String message;

        private IndySdkRejectResponse(Throwable e) {
            String code = "0";

            if (e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if (cause instanceof IndyException) {
                    IndyException indyException = (IndyException) cause;
                    code = String.valueOf(indyException.getSdkErrorCode());
                }
            }

            String message = e.getMessage();

            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String toJson() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
}
