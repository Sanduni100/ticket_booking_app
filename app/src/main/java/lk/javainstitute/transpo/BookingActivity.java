package lk.javainstitute.transpo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lk.javainstitute.transpo.model.trip;
import lk.javainstitute.transpo.adapter.tripAdapter;

public class BookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvBookFrom, tvBookTo, tvBookDate, tvBookPass;
    ArrayList<trip> arrayList;
    tripAdapter adapter;
    FirebaseFirestore db;
    String strGetFrom, strGetTo, strGetDate, strGetPass, strGetEmail, strGetName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        intent = getIntent();
        strGetFrom = intent.getStringExtra("from");
        strGetTo = intent.getStringExtra("to");
        strGetDate = intent.getStringExtra("date");
        strGetPass = intent.getStringExtra("tPass");
        strGetEmail = intent.getStringExtra("email");
        strGetName = intent.getStringExtra("name");

        recyclerView = findViewById(R.id.recy_tripID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvBookFrom = findViewById(R.id.tv_bookingFromID);
        tvBookTo = findViewById(R.id.tv_bookingToID);
        tvBookDate = findViewById(R.id.tv_bookingDateID);
        tvBookPass = findViewById(R.id.tv_bookingPassID);

        tvBookFrom.setText(strGetFrom);
        tvBookTo.setText(strGetTo);
        tvBookDate.setText(strGetDate);
        tvBookPass.setText(strGetPass);

    db = FirebaseFirestore.getInstance();
    arrayList = new ArrayList<trip>();

    adapter = new tripAdapter(BookingActivity.this, arrayList, tvBookPass, tvBookDate, strGetEmail, strGetName);
    recyclerView.setAdapter(adapter);


        if (strGetFrom.matches("Galle") && strGetTo.matches("Colombo")) {
            GalleColombo();
        } else if (strGetFrom.matches("Kandy") && strGetTo.matches("Colombo")) {
            KandyColombo();
        } else if (strGetFrom.matches("Colombo") && strGetTo.matches("Katunayake")) {
            ColomboKatunayake();
        } else if (strGetFrom.matches("Mathara") && strGetTo.matches("Colombo")) {
            MatharaColombo();
        } else if (strGetFrom.matches("Colombo") && strGetTo.matches("Maththala")) {
            ColomboMaththala();
        } else if (strGetFrom.matches("Meerigama") && strGetTo.matches("Kurunagala")) {
            MeerigamaKurunagala();
        }

}


private void KandyColombo() {

    db.collection("KandyColombo").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            for (DocumentChange dc: value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    trip currentTrip = dc.getDocument().toObject(trip.class);

                    String tripDate = currentTrip.getDate();

                    // Check the trip date is equal to strGetDate
                    if (strGetDate.equals(tripDate)) {
                        arrayList.add(currentTrip);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    });
}

//private void GalleColombo() {
//    db.collection("GalleColombo").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
//        @Override
//        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//            for (DocumentChange dc: value.getDocumentChanges()) {
//                if (dc.getType() == DocumentChange.Type.ADDED) {
//                    arrayList.add(dc.getDocument().toObject(trip.class));
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }
//    });


 //   }

    private void GalleColombo() {
        db.collection("GalleColombo").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        trip currentTrip = dc.getDocument().toObject(trip.class);

                        String tripDate = currentTrip.getDate();

                        // Check the trip date is equal to strGetDate
                        if (strGetDate.equals(tripDate)) {
                            arrayList.add(currentTrip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }


    private void ColomboKatunayake() {

        db.collection("ColomboKatunayake").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        trip currentTrip = dc.getDocument().toObject(trip.class);

                        String tripDate = currentTrip.getDate();

                        // Check the trip date is equal to strGetDate
                        if (strGetDate.equals(tripDate)) {
                            arrayList.add(currentTrip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void MatharaColombo() {

        db.collection("MatharaColombo").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        trip currentTrip = dc.getDocument().toObject(trip.class);

                        String tripDate = currentTrip.getDate();

                        // Check the trip date is equal to strGetDate
                        if (strGetDate.equals(tripDate)) {
                            arrayList.add(currentTrip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void ColomboMaththala() {

        db.collection("ColomboMaththala").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        trip currentTrip = dc.getDocument().toObject(trip.class);

                        String tripDate = currentTrip.getDate();

                        // Check the trip date is equal to strGetDate
                        if (strGetDate.equals(tripDate)) {
                            arrayList.add(currentTrip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void MeerigamaKurunagala() {

        db.collection("MeerigamaKurunagala").orderBy("rTime", Query.Direction.ASCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        trip currentTrip = dc.getDocument().toObject(trip.class);

                        String tripDate = currentTrip.getDate();

                        // Check the trip date is equal to strGetDate
                        if (strGetDate.equals(tripDate)) {
                            arrayList.add(currentTrip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

}