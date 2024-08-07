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

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 1; // Request code for camera permission
    private DecoratedBarcodeView barcodeView; // View for barcode scanning
    private TextView resultTextView; // TextView to display scan results
    private LinearLayout shareButton; // Button to share scan results
    private TextView rescanButton; // Button to restart scanning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner); // Set the layout for this activity

        // Initialize views
        barcodeView = findViewById(R.id.zxing_barcode_scanner);
        resultTextView = findViewById(R.id.result_text_view);
        shareButton = findViewById(R.id.shareButton);
        rescanButton = findViewById(R.id.rescan);

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Initialize scanner if permission is already granted
            initializeScanner();
        }

        // Set up click listeners for buttons
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Share button click listener
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scanResult = resultTextView.getText().toString();
                if (!scanResult.isEmpty()) {
                    // Share scan result if available
                    shareResult(scanResult);
                } else {
                    // Prompt user to scan something first
                    Toast.makeText(QRCodeScannerActivity.this, "Please scan something before sharing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Rescan button click listener
        rescanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart scanning process
                restartScanning();
            }
        });
    }

    private void shareResult(String result) {
        // Create and start an intent to share the scan result
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, result);
        startActivity(Intent.createChooser(shareIntent, "Share result via"));
    }

    private void restartScanning() {
        // Stop scanning
        barcodeView.pause();
        resultTextView.setText("");

        // Add a short delay to ensure the scanner has paused
        barcodeView.postDelayed(() -> {
            // Restart scanning
            barcodeView.resume();
            barcodeView.decodeSingle(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    barcodeView.pause();
                    String resultText = result.getText();
                    Log.d("QRCodeScanner", "Scanned result: " + resultText);
                    resultTextView.setText(resultText);
                    Toast.makeText(QRCodeScannerActivity.this, "Scanned result: " + resultText, Toast.LENGTH_LONG).show();
                }
            });
        }, 100); // 100 milliseconds delay
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Initialize scanner if permission is granted
                initializeScanner();
            } else {
                // Inform user about the necessity of camera permission and finish activity
                Toast.makeText(this, "Camera permission is needed to scan QR codes", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void initializeScanner() {
        Log.d("QRCodeScanner", "Initializing scanner");

        // Start scanning and handle barcode results
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcodeView.pause();
                String resultText = result.getText();
                Log.d("QRCodeScanner", "Scanned result: " + resultText);
                resultTextView.setText(resultText);
                Toast.makeText(QRCodeScannerActivity.this, "Scanned result: " + resultText, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume scanning when activity is resumed
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause scanning when activity is paused
        barcodeView.pause();
    }
}
