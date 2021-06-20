package com.example.aisleassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    MaterialButton loginContinue;
    TextView instrText, topText, countDown;
    EditText areaCode, phoneNumber, otpInput;
    LinearLayout phoneNumberLayout, loginLayout;
    String phoneNo, otp, authString;
    ImageView createIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        instrText = findViewById(R.id.instr_text);
        topText = findViewById(R.id.top_text);
        areaCode = findViewById(R.id.area_code);
        phoneNumber = findViewById(R.id.phone_no);
        otpInput = findViewById(R.id.otp);
        phoneNumberLayout = findViewById(R.id.phone_number);
        countDown = findViewById(R.id.countdown);
        loginLayout = findViewById(R.id.login_layout);
        createIcon = findViewById(R.id.create_icon);

        loginContinue = findViewById(R.id.login_continue);
        loginContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberLayout.getVisibility() == View.VISIBLE) {
                    String phoneNoInp = phoneNumber.getText().toString();
                    if (phoneNoInp.length() == 10 && phoneNoInp.charAt(0) != '0') {
                        phoneNo = areaCode.getText().toString() + phoneNoInp;
                        JSONObject params = new JSONObject();
                        try {
                            params.put("number", phoneNo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://testa2.aisle.co/V1/users/phone_number_login", params,
                                response -> {
                                    try {
                                        if (response.getBoolean("status")) {
                                            topText.setText(phoneNo);
                                            instrText.setText(R.string.enter_otp);
                                            phoneNumberLayout.setVisibility(View.GONE);
                                            otpInput.setVisibility(View.VISIBLE);
                                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) loginLayout.getLayoutParams();
                                            layoutParams.width = (int) (170 * getResources().getDisplayMetrics().density);
                                            loginLayout.setLayoutParams(layoutParams);
                                            countDown.setVisibility(View.VISIBLE);
                                            createIcon.setVisibility(View.VISIBLE);
                                            new CountDownTimer(120000, 1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    countDown.setText((millisUntilFinished / 60000) + ":" + (millisUntilFinished / 1000) % 60);
                                                }

                                                @Override
                                                public void onFinish() {
                                                    countDown.setVisibility(View.GONE);
                                                }
                                            }.start();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(LoginActivity.this, "Error Parsing Response", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> Toast.makeText(LoginActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show()) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Cookie", "__cfduid=df9b865983bd04a5de2cf5017994bbbc71618565720");
                                return headers;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    otp = otpInput.getText().toString();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("number", phoneNo);
                        params.put("otp", otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://testa2.aisle.co/V1/users/verify_otp", params,
                            response -> {
                                try {
                                    if (response.has("token")) {
                                        authString = response.getString("token");
                                        startActivity(new Intent(LoginActivity.this, NotesActivity.class).putExtra("auth", authString));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error Parsing Response", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Error Parsing Response", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(LoginActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show()) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Cookie", "__cfduid=df9b865983bd04a5de2cf5017994bbbc71618565720");
                            return headers;
                        }
                    };
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (phoneNumberLayout.getVisibility() == View.VISIBLE) {
            this.finishAffinity();
        } else {
            topText.setText(R.string.get_otp);
            instrText.setText(R.string.enter_phone_number);
            phoneNumberLayout.setVisibility(View.VISIBLE);
            otpInput.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) loginLayout.getLayoutParams();
            layoutParams.width = (int) (220 * getResources().getDisplayMetrics().density);
            loginLayout.setLayoutParams(layoutParams);
            countDown.setVisibility(View.GONE);
            createIcon.setVisibility(View.GONE);
        }
    }
}