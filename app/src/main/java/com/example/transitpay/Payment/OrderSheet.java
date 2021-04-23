package com.example.transitpay.Payment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.transitpay.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class OrderSheet {

  private static final String SHOWING_KEY = "showing";
  private Runnable onPayWithCardClickListener;
  private Runnable onPayWithGoogleClickListener;
  private View payWithGoogleButton;
  private boolean payWithGoogleButtonEnabled;
  private boolean showing;

  TextView fare;
  TextView price;

  public void setOnPayWithCardClickListener(Runnable listener) {
    onPayWithCardClickListener = listener;
  }

  public void setOnPayWithGoogleClickListener(Runnable listener) {
    onPayWithGoogleClickListener = listener;
  }

  public void setPayWithGoogleEnabled(boolean enabled) {
    payWithGoogleButtonEnabled = enabled;
    if (payWithGoogleButton != null) {
      payWithGoogleButton.setEnabled(enabled);
    }
  }

  public void show(Activity activity) {
    showing = true;
    BottomSheetDialog dialog = new BottomSheetDialog(activity);
    View sheetView = LayoutInflater.from(activity).inflate(R.layout.sheet_order, null);

    fare = sheetView.findViewById(R.id.fare_type);

    price = sheetView.findViewById(R.id.price);

    String fareType = CheckoutActivity.getFareType();

    String farePrice = CheckoutActivity.getFarePrice();

    fare.setText(fareType);
    price.setText("$"+farePrice);

    View closeButton = sheetView.findViewById(R.id.close_sheet_button);
    View payWithCardButton = sheetView.findViewById(R.id.pay_with_card_button);
    payWithCardButton.setOnClickListener(v -> {
      dialog.dismiss();
      showing = false;
      onPayWithCardClickListener.run();
    });


    closeButton.setOnClickListener(v -> dialog.dismiss());

    dialog.setContentView(sheetView);
    BottomSheetBehavior behavior = BottomSheetBehavior.from((View) sheetView.getParent());
    dialog.setOnShowListener(dialogInterface -> behavior.setPeekHeight(sheetView.getHeight()));
    dialog.setOnCancelListener(dialog1 -> showing = false);
    dialog.setCancelable(true);

    dialog.show();
  }

  public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(SHOWING_KEY, showing);
  }

  public void onRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
    boolean showing = savedInstanceState.getBoolean(SHOWING_KEY);
    if (showing) {
      show(activity);
    }
  }
}
