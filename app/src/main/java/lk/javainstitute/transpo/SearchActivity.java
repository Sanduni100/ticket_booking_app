package lk.javainstitute.transpo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sFrom, sTo, sType;
    String strFrom, strTo, strType, strTotalPass, strDate, stremail, strfullName, strUserName;
    int passValue;
    Button btnFindRide;
    ImageButton logout;
    ImageButton imgBtnPlus, imgBtnMin, btnDatePicker;
    TextView tvPass, tvDate, tvName, tvEmail;
    Intent intent;
    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    public static final String TAG = RegistrationActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        intent = getIntent();
        stremail = intent.getStringExtra("email");



        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reference = firebaseFirestore.collection("UserProfile").document(stremail);
        tvPass = findViewById(R.id.tv_passengersID);
        imgBtnPlus = findViewById(R.id.imgBtnPlus);
        imgBtnMin = findViewById(R.id.imgBtnMinus);
        sFrom = findViewById(R.id.sFrom);
        sTo = findViewById(R.id.sTo);
        sType = findViewById(R.id.sType);
        btnFindRide = findViewById(R.id.btnFindRide);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        tvDate = findViewById(R.id.tvDate);
        tvName = findViewById(R.id.tvUserName_ID);
        logout = findViewById(R.id.btnLogout);


 // set name on the top

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    strfullName = documentSnapshot.getString("FullName");
                    tvName.setText("Hi, Mr/Miss."+strfullName);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w(TAG, "error");

            }
        });




        // Logout code

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(SearchActivity.this, "Logout Successful !", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,R.array.travel_from, R.layout.search_from);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sFrom.setAdapter(adapterFrom);
        sFrom.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this,R.array.travel_to, R.layout.search_to);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTo.setAdapter(adapterTo);
        sTo.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterBusType = ArrayAdapter.createFromResource(this,R.array.bus_type, R.layout.search_type);
        adapterBusType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(adapterBusType);
        sType.setOnItemSelectedListener(this);


        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTotalPass = tvPass.getText().toString();
                passValue = Integer.parseInt(strTotalPass);
                if (passValue < 10) {
                    passValue++;
                }

                tvPass.setText(String.valueOf(passValue));
            }
        });

        imgBtnMin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                strTotalPass =tvPass.getText().toString();
                passValue =Integer.parseInt(strTotalPass);
                if (passValue > 0) {
                    passValue--;
                }

                tvPass.setText(String.valueOf(passValue));
            }
        });



        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Travel Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override

            public void onPositiveButtonClick(Long selection) {

            strDate =new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(new Date(selection));
            tvDate.setText(MessageFormat.format("{0}",strDate));
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
});



        btnFindRide.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            strFrom = sFrom.getSelectedItem().toString();
            strTo = sTo.getSelectedItem().toString();
            strType = sType.getSelectedItem().toString();
            int tPass = Integer.parseInt(strTotalPass) + 1;
            String strTotalPass = String.valueOf(tPass);
            Intent intent = new Intent(SearchActivity.this, BookingActivity.class);
            intent.putExtra("from", strFrom);
            intent.putExtra("to", strTo);
            intent.putExtra("date", strDate);
            intent.putExtra("tPass", strTotalPass);
            intent.putExtra("email", stremail);
            intent.putExtra("name", strfullName);
            startActivity(intent);
        }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}