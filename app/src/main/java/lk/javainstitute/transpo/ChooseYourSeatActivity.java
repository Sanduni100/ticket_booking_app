package lk.javainstitute.transpo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.javainstitute.transpo.adapter.SeatAdapter;

public class ChooseYourSeatActivity extends AppCompatActivity {

    Context context;
    Button btnCheckout;
    GridView gridView;
    TextView tvSeatNo, tvFrom, tvTo, tvTime, tvPrice, tvPass, tvdate;
    int[] seatBooking = {R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box,
            R.drawable.seat_view_yellow_box, R.drawable.seat_view_green_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_green_box,
            R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box,
            R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_green_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box,
            R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box,
            R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_yellow_box, R.drawable.seat_view_green_box, R.drawable.seat_view_yellow_box,};
    String[] strSeatNo;
    String strFromS, strToS, strTimeS, strPriceS, strPassS, strSeat, strDate, strEmail, strName, resultString, resultSeats, finalSeat, finalResult;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_seat);

        intent = getIntent();
        strFromS = intent.getStringExtra("pick");
        strToS = intent.getStringExtra("drop");
        strTimeS = intent.getStringExtra("time");
        strPriceS = intent.getStringExtra("price");
        strPassS = intent.getStringExtra("pass");
        strDate = intent.getStringExtra("date");
        strEmail = intent.getStringExtra("email");
        strName = intent.getStringExtra("name");

        gridView = findViewById(R.id.gridSeatID);

        tvSeatNo = findViewById(R.id.tv_tripSeatNoID);
        btnCheckout = findViewById(R.id.btnCheckoutID);
        tvFrom = findViewById(R.id.tvSeat_fromID);
        tvTo = findViewById(R.id.tvSeat_toID);
        tvTime = findViewById(R.id.tvSeat_timeID);
        tvPrice = findViewById(R.id.tvSeat_priceID);
        tvPass = findViewById(R.id.tvSeat_passID);
        tvdate = findViewById(R.id.date);

        strSeatNo = getResources().getStringArray(R.array.seat);

        tvFrom.setText(strFromS);
        tvTo.setText(strToS);
        tvdate.setText(strDate);


        try {
            double price = Double.parseDouble(strPriceS);
            double pass = Double.parseDouble(strPassS);

            double result = price * pass;

//            tvPrice.setText(String.valueOf(result));

            resultString = String.format("%.2f", result);
            tvPrice.setText("Rs."+resultString);

        } catch (NumberFormatException e) {

            e.printStackTrace();
            tvPrice.setText("Error");
        }

       // tvPrice.setText(strPriceS);
        tvTime.setText(strTimeS);
        tvPass.setText(strPassS);


        btnCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseYourSeatActivity.this, PaymentActivity.class);
                intent.putExtra("pick", strFromS);
                intent.putExtra("drop", strToS);
                intent.putExtra("time", strTimeS);
                intent.putExtra("price", resultString);
                intent.putExtra("pass", strPassS);
                intent.putExtra("seat", finalResult);
                intent.putExtra("date", strDate);
                intent.putExtra("email", strEmail);
                intent.putExtra("name", strName);

                startActivity(intent);
            }
        });

        SeatAdapter seatAdapter = new SeatAdapter(this,seatBooking,strSeatNo);
        gridView.setAdapter(seatAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //strSeat = strSeatNo[position];
           // tvSeatNo.setText(strSeat);

            try {
                // Convert strPriceS and strPassS to numeric values (assuming they are numbers)
                double seat = Double.parseDouble(strSeatNo[position]);
                double pass = Double.parseDouble(strPassS);

                double bookingSeat = seat + pass - 1;

                if (bookingSeat > 40) {
                    bookingSeat = 0;

                //
                    Toast.makeText(ChooseYourSeatActivity.this, "This bus has only 40 seats", Toast.LENGTH_SHORT).show();
                }


                String resultSeats = String.valueOf((int) bookingSeat);

                strSeat = strSeatNo[position];

                finalResult = "No:" + strSeat + "-" + resultSeats;

                tvSeatNo.setText(finalResult);

            } catch (NumberFormatException e) {

                e.printStackTrace();
                tvSeatNo.setText("Error");
            }



        }
        });


    }


}
