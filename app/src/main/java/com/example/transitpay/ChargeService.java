package com.example.transitpay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChargeService {
  @POST("/chargeForCookie")
  Call<Void> charge(@Body ChargeRequest request);

  class ChargeErrorResponse {
    String errorMessage;
  }

  class ChargeRequest {
    final String nonce;
    final String fare;
    final String price;

    ChargeRequest(String nonce, String fare, String price) {
      this.nonce = nonce;
      this.fare = fare;
      this.price = price;
    }
  }
}
