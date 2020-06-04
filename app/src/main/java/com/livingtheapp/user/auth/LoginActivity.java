package com.livingtheapp.user.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.livingtheapp.user.MainActivity;
import com.livingtheapp.user.R;
import com.livingtheapp.user.utils.AppConstant;
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
        findViewById(R.id.txtForgetPass).setOnClickListener(v -> frorgetPassAlert());
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
                                    String userToken = object.getString("token");
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
                                    CustomPerference.putString(LoginActivity.this,CustomPerference.USER_TOKEN,
                                            userToken);
                                    CustomPerference.putBoolean(LoginActivity.this,CustomPerference.ISLOGIN,
                                            true);
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

    void frorgetPassAlert()
    {
        Dialog dialog_auth = new Dialog(this);
        dialog_auth.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_auth.setContentView(R.layout.alert_forgetpass);
        dialog_auth.show();
        dialog_auth.setCanceledOnTouchOutside(false);
        dialog_auth.setCancelable(false);

        Window window = dialog_auth.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txtCancel = dialog_auth.findViewById(R.id.txtCancel);
        TextView txtOk = dialog_auth.findViewById(R.id.txtOk);
        EditText inputEmail = dialog_auth.findViewById(R.id.inputEmail);


        txtCancel.setOnClickListener(v -> dialog_auth.dismiss());

        txtOk.setOnClickListener(v -> {
            String mailId = inputEmail.getText().toString().trim();
            System.out.println("maid"+mailId);

            if(!TextUtils.isEmpty(mailId)){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",mailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppUrl.forgetPasswordService, jsonObject, response -> {

            System.out.println("responce>>>"+response);

            try {
                if(response.getString("status").equalsIgnoreCase("1"))
                {
                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 2000, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        dialog_auth.dismiss();
            }
        else Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_LONG).show();
        });
    }
}
