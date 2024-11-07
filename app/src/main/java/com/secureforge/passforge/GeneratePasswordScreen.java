package com.secureforge.passforge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ProgressBar passwordStrengthMeter;


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
        Button buttonCopyPassword = findViewById(R.id.buttonCopyPassword);

        passwordStrengthMeter = findViewById(R.id.passwordStrengthMeter);

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

            updatePasswordStrengthMeter(password);
        });

        buttonCopyPassword.setOnClickListener(v -> {
            String passwordToCopy = txtGeneratedPassword.getText().toString();

            if (!TextUtils.isEmpty(passwordToCopy)) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Password", passwordToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(GeneratePasswordScreen.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GeneratePasswordScreen.this, "No password to copy", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updatePasswordStrengthMeter(String password) {
        int strengthPercentage = calculatePasswordStrength(password);
        passwordStrengthMeter.setProgress(strengthPercentage);

        // Change color based on strength
        if (strengthPercentage < 40) {
            passwordStrengthMeter.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (strengthPercentage < 70) {
            passwordStrengthMeter.getProgressDrawable().setColorFilter(
                    Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            passwordStrengthMeter.getProgressDrawable().setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private int calculatePasswordStrength(String password) {
        int lengthScore = Math.min(password.length() * 5, 30); // Length contributes up to 30%
        int varietyScore = 0;

        // Check for character variety (uppercase, lowercase, digits, special characters)
        if (password.matches(".*[A-Z].*")) varietyScore += 15; // Uppercase letters
        if (password.matches(".*[a-z].*")) varietyScore += 15; // Lowercase letters
        if (password.matches(".*\\d.*")) varietyScore += 20;   // Digits
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
            varietyScore += 20; // Special characters

        // Total strength score, capped at 100%
        int totalScore = lengthScore + varietyScore;
        return Math.min(totalScore, 100);
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