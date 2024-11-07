package com.secureforge.passforge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtSavedPasswordsCount = findViewById(R.id.txtSavedPasswordsCount);
        TextView txtGeneratedPasswordsCount = findViewById(R.id.txtGeneratedPasswordsCount);

        // Example: Set the count (replace with your logic to fetch actual counts)
        txtSavedPasswordsCount.setText("5");
        txtGeneratedPasswordsCount.setText("12");

        Button buttonGeneratePassword = findViewById(R.id.buttonGeneratePassword);
        Button buttonViewHistory = findViewById(R.id.buttonViewHistory);
        Button buttonViewSavedPasswords = findViewById(R.id.buttonViewSavedPasswords);

        buttonGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GeneratePasswordScreen.class));
            }
        });

        buttonViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryScreen.class));
            }
        });

        buttonViewSavedPasswords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SavedPasswordsScreen.class));
            }
        });
    }
}
