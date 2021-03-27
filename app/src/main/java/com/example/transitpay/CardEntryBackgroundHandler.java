package com.example.transitpay;

import android.content.res.Resources;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

import sqip.Call;
import sqip.CardDetails;
import sqip.CardEntryActivityCommand;
import sqip.CardNonceBackgroundHandler;

public class CardEntryBackgroundHandler implements CardNonceBackgroundHandler {

  private final ChargeCall.Factory chargeCallFactory;
  private final Resources resources;
  FirebaseDatabase rootNode;
  DatabaseReference reference;

  public CardEntryBackgroundHandler(ChargeCall.Factory chargeCallFactory,
      Resources resources) {
    this.chargeCallFactory = chargeCallFactory;
    this.resources = resources;
  }

  @Override
  public CardEntryActivityCommand handleEnteredCardInBackground(CardDetails cardDetails) {
    String fareType = CheckoutActivity.getFareType();

    assignFareType(fareType);

    if (!ConfigHelper.serverHostSet()) {
      ConfigHelper.printCurlCommand(cardDetails.getNonce());
      return new CardEntryActivityCommand.Finish();
    }

    Call<ChargeResult> chargeCall = chargeCallFactory.create(cardDetails.getNonce());
    ChargeResult chargeResult = chargeCall.execute();

    if (chargeResult.success) {
      return new CardEntryActivityCommand.Finish();
    } else if (chargeResult.networkError) {
      return new CardEntryActivityCommand.ShowError(resources.getString(R.string.network_failure));
    } else {
      return new CardEntryActivityCommand.ShowError(chargeResult.errorMessage);
    }
  }

  private void assignFareType(String fare) {
    String fareType = getFareType(fare);

    rootNode = FirebaseDatabase.getInstance();

    String phoneNumber = null;
    if (LoginActivity.getUser() != null) {
      User user = LoginActivity.getUser();
      phoneNumber = user.phone;
    }

    reference = rootNode.getReference("user/"+phoneNumber);

    reference.child("Fare Type").setValue(fareType);
  }

  @Nullable
  private String getFareType(String fare) {
    String fareType = null;
    if(fare.equals("Single Trip Fare")){
      fareType = "1";
    }
    else if(fare.equals("Two Trip Fare")){
      fareType = "2";
    }
    else if(fare.equals("10 Trip Fare")){
      fareType = "10";
    }
    else if(fare.equals("Three Day Fare")){
      fareType = "3Day";
    }
    else if(fare.equals("Weekly Fare")){
      fareType = "W";
    }
    else if(fare.equals("Monthly Fare")){
      fareType = "M";
    }
    return fareType;
  }
}
