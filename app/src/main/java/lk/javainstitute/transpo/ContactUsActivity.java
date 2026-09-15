package lk.javainstitute.transpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;


        LatLng defaultLocation = new LatLng(7.018580, 79.937550);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));


        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_json));

            if (!success) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        map = googleMap;
//        map.setMapType((GoogleMap.MAP_TYPE_SATELLITE));
//        map.getUiSettings().setZoomControlsEnabled(true);
//        LatLng latLng = new LatLng(6.9069634, 79.9161765);
//        map.addMarker(new MarkerOptions().position (latLng).title("Location"));
//        map.moveCamera (CameraUpdateFactory.newLatLngZoom (latLng, 100));

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
            }
        });
    }


}