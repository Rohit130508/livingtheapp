package com.livingtheapp.user.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.livingtheapp.user.MainActivity;
import com.livingtheapp.user.R;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView txtGetStarted;
    EditText inputEmailId,inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmailId = findViewById(R.id.inputEmailId);
        inputPassword = findViewById(R.id.inputPassword);
        findViewById(R.id.txtGetStarted).setOnClickListener(v -> getAccountLogin());
    }

    void getAccountLogin()
    {


        String emailId = inputEmailId.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(emailId)
                && !TextUtils.isEmpty(pass) ){


                Utils.customProgress(this,"Please Wait...");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password",pass);
                    jsonObject.put("userid",emailId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Req>>>"+jsonObject);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        AppUrl.loginService,
                        jsonObject,
                        response -> {

                            System.out.println("Res>>>"+response);

                            Utils.customProgressStop();

                            try {
                                if(response.getString("status").equalsIgnoreCase("1")) {

                                    JSONObject object = response.getJSONObject("data");

                                    String userName = object.getString("firstName");
                                    String userEmail = object.getString("email");
                                    String userMobile = object.getString("contactNo");
                                    String userPassword = pass;
                                    String regId = object.getString("id");

                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_NAME,
                                            userName);
                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_EMAIL,
                                            userEmail);
                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_MOBILE,
                                            userMobile);
                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_PASSWORD,
                                            userPassword);
                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_ID,
                                            regId);
                                    CustomPerference.putBoolean(LoginActivity.this,CustomPerference.ISLOGIN,true);
                                    startActivity(new Intent(this, MainActivity.class));
                                }
                                else

                                    Utils.CustomDialog(LoginActivity.this,
                                            "Look into the issue",
                                            response.getString("message"));
//                                    Utils.CustomAlert(LoginActivity.this,
//                                            "Some Problem",
//                                            response.getString("message"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, error -> {
                    Utils.customProgressStop();

                });

                RequestQueue queue = Volley.newRequestQueue(this);
                request.setRetryPolicy(new DefaultRetryPolicy(20 * 2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);

        }
        else

            Toast.makeText(getApplicationContext(),"All feilds are mandatory",Toast.LENGTH_LONG).show();

    }
}
