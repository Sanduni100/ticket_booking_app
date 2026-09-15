package lk.javainstitute.transpo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CardFormActivity extends AppCompatActivity {

    Button btnPayDone;
    ImageView ivCard;
    Intent intent;
    String strCardKey;
    String strFrom, strTo, strTime, strPrice, strPass, strSeat, strDate, strEmail, strName, resultString;
    EditText cardNo, expire, cvv, chname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);

        btnPayDone = findViewById(R.id.btnPayDone_ID);
        ivCard = findViewById(R.id.imgView_CardID);
        intent = getIntent();
        strCardKey = intent.getStringExtra("key");
        strFrom = intent.getStringExtra("pick");
        strTo = intent.getStringExtra("drop");
        strTime = intent.getStringExtra("time");
        resultString = intent.getStringExtra("price");
        strPass = intent.getStringExtra("pass");
        strSeat = intent.getStringExtra("seat");
        strDate = intent.getStringExtra("date");
        strEmail = intent.getStringExtra("email");
        strName = intent.getStringExtra("name");



//        try {
//            double price = Double.parseDouble(strPrice);
//            double pass = Double.parseDouble(strPass);
//
//            double result = price * pass;
//
////            tvPrice.setText(String.valueOf(result));
//
//            resultString = String.format("%.2f", result);
//            btnPayDone.setText("Pay "+resultString);
//
//        } catch (NumberFormatException e) {
//
//            e.printStackTrace();
//            btnPayDone.setText("Error");
//        }
        btnPayDone.setText("Pay Rs."+resultString);


        if(strCardKey.matches("master")){
            ivCard.setImageResource(R.drawable.mastercard_icon);
        }
        if(strCardKey.matches("visa")){
            ivCard.setImageResource(R.drawable.visa_card_icon);
        }
        if(strCardKey.matches("paypal")){
            ivCard.setImageResource(R.drawable.paypal_card);
        }


btnPayDone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            Intent intent = new Intent(CardFormActivity.this, DownloadTicketActivity.class);
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

    });
}

}
