package lk.javainstitute.transpo;

import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DownloadTicketActivity extends AppCompatActivity {

    String strFrom, strTo, strTime, strPass, strSeat, strDate, strEmail, strName, resultString;
    Intent intent;
    TextView tvFrom, tvTo, tvDate, tvTime, tvPass, tvSeat, tvTicketID, barCodeID;
    ImageView barCodeIcon;
    Button btnDown;
    private LinearLayout linearLayout;
    private Bitmap bitmap;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_ticket);


        firebaseFirestore = FirebaseFirestore.getInstance();
        tvFrom = findViewById(R.id.dndTvFromID);
        tvTo = findViewById(R.id.dndTvToID);
        tvDate = findViewById(R.id.dndTvDateID);
        tvTime = findViewById(R.id.dndTvTimeID);
        tvPass = findViewById(R.id.dndTvPassID);
        tvSeat = findViewById(R.id.dndTvSeatID);
        btnDown = findViewById(R.id.btnDownload_ID);
        tvTicketID = findViewById(R.id.dndTvTicketID);
        barCodeID = findViewById(R.id.barCodeID);
        barCodeIcon = findViewById(R.id.barCodeIcon);


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

        tvFrom.setText(strFrom);
        tvTo.setText(strTo);
        tvDate.setText(strDate);
        tvTime.setText(strTime);
        tvPass.setText(strPass);
        tvSeat.setText(strSeat);

        String barcodeData = generateBarcodeData();
        Bitmap barcodeBitmap = generateBarcodeBitmap(barcodeData);

        // Set barcode to the ImageView
        barCodeIcon.setImageBitmap(barcodeBitmap);


        linearLayout = findViewById(R.id.linear);


        // Generate  ticket ID
        try {
            String ticketPrefix = "RT";
            String randomNumber = String.valueOf((int) (Math.random() * 100));
            char randomCharacter = (char) ('A' + (Math.random() * 26));

            String ticketID = ticketPrefix + randomNumber + "-" + randomCharacter;

            tvTicketID.setText(ticketID);

        } catch (NumberFormatException e) {

            e.printStackTrace();
            tvTicketID.setText("Error");
        }


        // Generate code
        try {
            String ticketPrefix = "TICKET";
            String timestamp = String.valueOf(System.currentTimeMillis());
            String randomSuffix = String.valueOf((int) (Math.random() * 1000));

            String ticketID = ticketPrefix + timestamp + randomSuffix;

            barCodeID.setText(ticketID);

        } catch (NumberFormatException e) {

            e.printStackTrace();
            barCodeID.setText("Error");
        }




        //Download Button Process
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("size", "" + linearLayout.getWidth() + " " + linearLayout.getWidth());
                bitmap = LoadBitmap(linearLayout, linearLayout.getWidth(), linearLayout.getHeight());
                createPdf();

                Map<String, String> bookingData = new HashMap<>();
                bookingData.put("From", strFrom);
                bookingData.put("To", strTo);
                bookingData.put("Date", strDate);
                bookingData.put("Time", strTime);
                bookingData.put("Passenger", strPass);
                bookingData.put("Seat", strSeat);
                bookingData.put("Price", resultString);
                bookingData.put("Name", strName);

                firebaseFirestore.collection("BookingData").document(strEmail).set(bookingData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        showDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DownloadTicketActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void showDialog(){

        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        final AlertDialog.Builder alert = new AlertDialog.Builder(DownloadTicketActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_end, null);

        mView.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        mView.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        mView.findViewById(R.id.btnBackID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent1 = new Intent(DownloadTicketActivity.this, SearchActivity.class);
               intent1.putExtra("email", strEmail);
               startActivity(intent1);
            }
        });
        alertDialog.show();
    }



    private String generateBarcodeData() {
        // Your logic to generate barcode data (e.g., ticket ID)
        // Example: return "TICKET123456";
        return "TICKET" + System.currentTimeMillis();
    }

    private Bitmap generateBarcodeBitmap(String data) {
        try {
            // Encode data into a barcode
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            com.google.zxing.common.BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.CODE_128, 600, 300);

            // Convert the BitMatrix to a Bitmap
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }



    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write document content
        String targetPdf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/ticket.pdf";
      //  String targetPdf = Environment.getExternalStorageDirectory() + "/new_ticket.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "successfully ticket downloaded!", Toast.LENGTH_SHORT).show();


        /////////send notification
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        // Create notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
//                // .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Transpo Ticket Booking Service")
//                .setContentText("Successfully Downloaded your Ticket!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        // Show the notification
//        long notificationId = System.currentTimeMillis();
//        notificationManager.notify((int) notificationId, builder.build());
/////////////////////////////////////////////////////////////////////////////////////////////


        openPdf();

    }

    private void openPdf() {
        File file = new File("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), downloadFileName");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
            }
        }
    }


}