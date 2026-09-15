package lk.javainstitute.transpo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class GetStartActivity extends AppCompatActivity {

    private ImageView backgroundImageView;
    private int[] backgroundImages = {R.drawable.background1, R.drawable.background2, R.drawable.background3};
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Transpo_FullScreen);
        setContentView(R.layout.activity_get_start);

        backgroundImageView = findViewById(R.id.backgroundImageView);

        // Change the background image every 5 seconds (5000 milliseconds)
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeBackgroundImage();
                handler.postDelayed(this, 5000);
            }
        }, 1000);
    }

    private void changeBackgroundImage() {
        // Change the background image resource
        backgroundImageView.setImageResource(backgroundImages[currentImageIndex]);

        // Increment the index for the next background image
        currentImageIndex = (currentImageIndex + 1) % backgroundImages.length;



        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStartActivity.this, MainActivity.class));
            }
        });


    }

}