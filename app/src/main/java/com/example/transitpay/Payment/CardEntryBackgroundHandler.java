package com.example.transitpay.Payment;

import android.content.res.Resources;

import com.example.transitpay.Authenticate.LoginActivity;
import com.example.transitpay.Model.User;
import com.example.transitpay.R;
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
  private int daysLeft = 0;
  private int ticketsLeft = 0;

  public CardEntryBackgroundHandler(ChargeCall.Factory chargeCallFactory,
      Resources resources) {
    this.chargeCallFactory = chargeCallFactory;
    this.resources = resources;
  }


  @Override
  public CardEntryActivityCommand handleEnteredCardInBackground(CardDetails cardDetails) {


    if (!ConfigHelper.serverHostSet()) {
      ConfigHelper.printCurlCommand(cardDetails.getNonce());
      return new CardEntryActivityCommand.Finish();
    }

    Call<ChargeResult> chargeCall = chargeCallFactory.create(cardDetails.getNonce());
    ChargeResult chargeResult = chargeCall.execute();

    if (chargeResult.success) {
      String fareType = CheckoutActivity.getFareType();

      assignFareType(fareType);

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

    //PaymentStatus paymentStatus = new PaymentStatus("True");

    //reference.child("Payment status").setValue(paymentStatus);

    if(fareType.equals("3Day")||fareType.equals("Weekly")||fareType.equals("Monthly")){
      reference.child("daysLeft").setValue(daysLeft);
      //boolean isZero =reference.child("Days Left").equals(0);
      //reference.child("Zero").setValue(isZero);
    }

    if(fareType.equals("One")||fareType.equals("Two")||fareType.equals("Ten")){
      reference.child("ticketsLeft").setValue(ticketsLeft);
    }

    reference.child("Last Purchased Fare").setValue(fareType);
  }

  @Nullable
  private String getFareType(String fare) {
    String fareType = null;
    if(fare.equals("Single Trip Fare")){
      fareType = "One";
      ticketsLeft = 1;
    }
    else if(fare.equals("Two Trip Fare")){
      fareType = "Two";
      ticketsLeft = 2;
    }
    else if(fare.equals("10 Trip Fare")){
      fareType = "Ten";
      ticketsLeft = 10;
    }
    else if(fare.equals("Three Day Fare")){
      fareType = "3Day";
      daysLeft = 3;
    }
    else if(fare.equals("Weekly Fare")){
      fareType = "Weekly";
      daysLeft = 7;
    }
    else if(fare.equals("Monthly Fare")){
      fareType = "Monthly";
      daysLeft = 30;
    }
    return fareType;
  }

}
