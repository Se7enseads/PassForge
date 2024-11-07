package com.secureforge.passforge;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GeneratePasswordScreen extends AppCompatActivity {

    private SeekBar seekBarLength;
    private EditText txtLengthValue;
    private CheckBox checkboxUppercase;
    private CheckBox checkboxLowercase;
    private CheckBox checkboxNumbers;
    private CheckBox checkboxSpecialChars;
    private Button buttonGeneratePassword;
    private TextView txtGeneratedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_password_screen);

        seekBarLength = findViewById(R.id.seekBarLength);
        txtLengthValue = findViewById(R.id.txtLengthValue);
        checkboxUppercase = findViewById(R.id.checkboxUppercase);
        checkboxLowercase = findViewById(R.id.checkboxLowercase);
        checkboxNumbers = findViewById(R.id.checkboxNumbers);
        checkboxSpecialChars = findViewById(R.id.checkboxSpecialChars);
        buttonGeneratePassword = findViewById(R.id.buttonGeneratePassword);
        txtGeneratedPassword = findViewById(R.id.txtGeneratedPassword);

        // Set the initial length value for the password
        txtLengthValue.setText(String.valueOf(seekBarLength.getProgress()));

        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtLengthValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        txtLengthValue.setOnEditorActionListener((v, actionId, event) -> {
            try {
                int length = Integer.parseInt(txtLengthValue.getText().toString());
                if (length >= seekBarLength.getMin() && length <= seekBarLength.getMax()) {
                    seekBarLength.setProgress(length);
                } else {
                    Toast.makeText(GeneratePasswordScreen.this, "Please enter a valid length between " + seekBarLength.getMin() + " and " + seekBarLength.getMax(), Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(GeneratePasswordScreen.this, "Invalid length entered", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        buttonGeneratePassword.setOnClickListener(v -> {
            int length = seekBarLength.getProgress();
            if (length < 1) {
                Toast.makeText(GeneratePasswordScreen.this, "Please select a length greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean includeUppercase = checkboxUppercase.isChecked();
            boolean includeLowercase = checkboxLowercase.isChecked();
            boolean includeNumbers = checkboxNumbers.isChecked();
            boolean includeSpecialChars = checkboxSpecialChars.isChecked();

            if (!includeUppercase && !includeLowercase && !includeNumbers && !includeSpecialChars) {
                Toast.makeText(GeneratePasswordScreen.this, "Please select at least one character type", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = generatePassword(length, includeUppercase, includeLowercase, includeNumbers, includeSpecialChars);
            txtGeneratedPassword.setText(password);
        });
    }

    private String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeNumbers, boolean includeSpecialChars) {
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*()_+[]{}|;:,.<>?/";

        StringBuilder charPool = new StringBuilder();
        if (includeUppercase) charPool.append(uppercaseChars);
        if (includeLowercase) charPool.append(lowercaseChars);
        if (includeNumbers) charPool.append(numberChars);
        if (includeSpecialChars) charPool.append(specialChars);

        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        return password.toString();
    }
}