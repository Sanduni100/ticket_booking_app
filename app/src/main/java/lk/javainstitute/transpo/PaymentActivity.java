package lk.javainstitute.transpo;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Type;

public class PaymentActivity extends AppCompatActivity {

    CardView cardMaster, cardVisa, cardPaypal;
    String strCardType;
    String strFrom, strTo, strTime, strPrice, strPass, strSeat, strDate, strEmail, strName, resultString;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardMaster = findViewById(R.id.cardBtnMaster_ID);
        cardVisa = findViewById(R.id.cardVisa_ID);
        cardPaypal = findViewById(R.id.cardPaypal_ID);

        intent = getIntent();
        strFrom = intent.getStringExtra("pick");
        strTo = intent.getStringExtra("drop");
        strTime = intent.getStringExtra("time");
        resultString = intent.getStringExtra("price");
        strPass = intent.getStringExtra("pass");
        strSeat = intent.getStringExtra("seat");
        strDate = intent.getStringExtra("date");
        strEmail = intent.getStringExtra("email");
        strName = intent.getStringExtra("name");


        cardMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardType = "master";
                cardForm();
            }

        });
        cardVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardType = "visa";
                cardForm();
            }
        });
        cardPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCardType = "paypal";
                cardForm();
            }
        });
        return;
    }

    private void cardForm() {

        if (strCardType.equals("master") || strCardType.equals("visa")) {
            Intent intent = new Intent(PaymentActivity.this, CardFormActivity.class);
            intent.putExtra("key", strCardType);
            intent.putExtra("pick", strFrom);
            intent.putExtra("drop", strTo);
            intent.putExtra("time", strTime);
            intent.putExtra("price", resultString);
            intent.putExtra("pass", strPass);
            intent.putExtra("seat", strSeat);
            intent.putExtra("date", strDate);
            intent.putExtra("email", strEmail);
            intent.putExtra("name", strName);
            startActivity(intent);
        } else if (strCardType.equals("paypal")) {
            Intent intent = new Intent(PaymentActivity.this, CardFormActivity.class);
            intent.putExtra("key", strCardType);
            intent.putExtra("pick", strFrom);
            intent.putExtra("drop", strTo);
            intent.putExtra("time", strTime);
            intent.putExtra("price", resultString);
            intent.putExtra("pass", strPass);
            intent.putExtra("seat", strSeat);
            intent.putExtra("date", strDate);
            intent.putExtra("email", strEmail);
            intent.putExtra("name", strName);
            startActivity(intent);
        }

    }
}