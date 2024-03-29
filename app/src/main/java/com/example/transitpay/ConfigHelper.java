package com.example.transitpay;

import android.util.Log;

import com.squareup.moshi.Moshi;

import java.util.UUID;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ConfigHelper {

  public static final String GOOGLE_PAY_MERCHANT_ID = "L0XZV3TM660BJ";
  private static final String CHARGE_SERVER_HOST = "test3-transit-pay.herokuapp.com/";
  private static final String CHARGE_SERVER_URL = "https://" + CHARGE_SERVER_HOST + "/";

  public static boolean serverHostSet() {
    return !CHARGE_SERVER_HOST.equals("REPLACE_ME");
  }

  public static boolean merchantIdSet() {
    return !GOOGLE_PAY_MERCHANT_ID.equals("REPLACE_ME");
  }

  public static void printCurlCommand(String nonce) {
    String uuid = UUID.randomUUID().toString();
    Log.i("ExampleApplication",
        "Run this curl command to charge the nonce:\n"
            + "curl --request POST https://connect.squareupsandbox.com/v2/payments \\\n"
            + "--header \"Content-Type: application/json\" \\\n"
            + "--header \"Authorization: Bearer EAAAEJbgpfhe_tcJFO0QEXp9Sjr5xcbt550XZkCL0hU6rarv5MfZwaTerSyQoxMy\" \\\n"
            + "--header \"Accept: application/json\" \\\n"
            + "--data \'{\n"
            + "\"idempotency_key\": \"" + uuid + "\",\n"
            + "\"amount_money\": {\n"
            + "\"amount\": 100,\n"
            + "\"currency\": \"CAD\"},\n"
            + "\"source_id\": \"" + nonce + "\""
            + "}\'");
  }

  public static Retrofit createRetrofitInstance() {
    Moshi moshi = new Moshi.Builder().build();
    return new Retrofit
            .Builder()
            .baseUrl(ConfigHelper.CHARGE_SERVER_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build();
  }
}
