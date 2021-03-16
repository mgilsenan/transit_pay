package com.example.transitpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TODO set up payment options

public class PaymentActivity extends AppCompatActivity {
    EditText cardName, cardNumber, expirationDate, cardCvv;
    Button makePayment;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardName = findViewById(R.id.card_name);
        cardNumber = findViewById(R.id.card_number);
        expirationDate = findViewById(R.id.exp_date);
        cardCvv = findViewById(R.id.cvv);
        makePayment = findViewById(R.id.make_payment);

        makePayment.setOnClickListener(new View.OnClickListener() {
            private Toast toast;

            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                String phoneNumber = null;
                if (LoginActivity.getUser() != null) {
                    User user = LoginActivity.getUser();
                    phoneNumber = user.phone;
                }
                reference = rootNode.getReference("user/"+phoneNumber);

                String name = cardName.getEditableText().toString();
                String number = cardNumber.getEditableText().toString();
                String expiration = expirationDate.getEditableText().toString();
                String cvv = cardCvv.getEditableText().toString();

                PaymentInfo paymentInfo = new PaymentInfo(name, number, expiration, cvv);
                PaymentStatus paymentStatus = new PaymentStatus("True");
                reference.child("payment info").setValue(paymentInfo);
                reference.child("payment status").setValue(paymentStatus);
                toast = Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}