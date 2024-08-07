package com.example.loginapicalling.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.loginapicalling.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

public class BarcodeScannerActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 1; // Request code for camera permission
    private DecoratedBarcodeView barcodeView; // View for scanning barcodes or QR codes
    private TextView resultTextView; // TextView to display the scan result
    private LinearLayout shareButton; // Button to share the scan result
    private TextView rescanButton; // Button to trigger rescan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner); // Set the layout for this activity

        // Initialize the views
        barcodeView = findViewById(R.id.zxing_barcode_scanner);
        resultTextView = findViewById(R.id.result_text_view);
        shareButton = findViewById(R.id.shareButton);
        rescanButton = findViewById(R.id.rescan);

        // Check for camera permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            initializeScanner(); // Initialize the scanner if permission is granted
        }

        // Set up click listeners for buttons
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Set up the click listener for the share button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scanResult = resultTextView.getText().toString();
                if (!scanResult.isEmpty()) {
                    shareResult(scanResult); // Share the result if it is not empty
                } else {
                    // Show a toast message if there is no result to share
                    Toast.makeText(BarcodeScannerActivity.this, "Please scan something before sharing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the click listener for the rescan button
        rescanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartScanning(); // Restart scanning when the rescan button is clicked
            }
        });
    }

    private void shareResult(String result) {
        // Create an intent to share the result via other applications
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, result);
        startActivity(Intent.createChooser(shareIntent, "Share result via")); // Show chooser for sharing options
    }

    private void restartScanning() {
        // Pause the barcode view and clear the result
        barcodeView.pause();
        resultTextView.setText("");

        // Add a short delay to ensure the scanner has paused
        barcodeView.postDelayed(() -> {
            // Resume scanning and set up the barcode callback
            barcodeView.resume();
            barcodeView.decodeSingle(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    // Pause scanning after result is obtained
                    barcodeView.pause();
                    String resultText = result.getText();
                    Log.d("BarcodeScanner", "Scanned result: " + resultText); // Log the scanned result
                    resultTextView.setText(resultText); // Display the result
                    Toast.makeText(BarcodeScannerActivity.this, "Scanned result: " + resultText, Toast.LENGTH_LONG).show(); // Show a toast with the result
                }
            });
        }, 100); // Delay of 100 milliseconds
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeScanner(); // Initialize the scanner if permission is granted
            } else {
                // Show a toast message if camera permission is denied
                Toast.makeText(this, "Camera permission is needed to scan barcodes", Toast.LENGTH_LONG).show();
                finish(); // Close the activity if permission is denied
            }
        }
    }

    private void initializeScanner() {
        Log.d("BarcodeScanner", "Initializing scanner");

        // Set up the barcode callback for scanning
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcodeView.pause(); // Pause scanning after result is obtained

                String resultText = result.getText();
                Log.d("BarcodeScanner", "Scanned result: " + resultText); // Log the scanned result
                resultTextView.setText(resultText); // Display the result
                Toast.makeText(BarcodeScannerActivity.this, "Scanned result: " + resultText, Toast.LENGTH_LONG).show(); // Show a toast with the result
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume(); // Resume scanning when the activity is resumed
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause(); // Pause scanning when the activity is paused
    }
}
