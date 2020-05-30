package com.livingtheapp.user.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.livingtheapp.user.R;
import com.livingtheapp.user.utils.AppConstant;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RegistrationActivity extends AppCompatActivity {


    ArrayList<ModalCountries> arrayList = new ArrayList<>();
    private TextView txtCountryList,inputDateOfBirth;
    private CountryListAdapter adapter;
    private RecyclerView rvList;
    private AlertDialog alertDialog;

    EditText inputEmailId,inputmobile,inputFName,inputLName,inputPassword,inputConfirmPass;
    private DatePickerDialog datePickerDialog;
    private String sendDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        initView();

    }



    void initView()
    {


        inputmobile = findViewById(R.id.inputmobile);
        inputFName = findViewById(R.id.inputFName);
        inputLName = findViewById(R.id.inputLName);
        inputEmailId = findViewById(R.id.inputEmailId);
        inputDateOfBirth = findViewById(R.id.inputDateOfBirth);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPass = findViewById(R.id.inputConfirmPass);


        txtCountryList = findViewById(R.id.txtCountryList);
        txtCountryList.setOnClickListener(v -> listCountries());

        findViewById(R.id.txtGetStarted).setOnClickListener(v -> getRegisteredAccount());

        getDateCalendar();
        inputDateOfBirth.setOnClickListener(v ->
        {

            datePickerDialog.show();
        });



    }

    void getDateCalendar()
    {


        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    // GET CALENDAR INSTANCE
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    // SET VALUES
                    sendDate = AppConstant.dateFormatterYear.format(newDate.getTime());
                    inputDateOfBirth.setText(AppConstant.dateFormatter.format(newDate.getTime()));

                },
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        try {
            datePickerDialog.getDatePicker().setMinDate(AppConstant.dateFormatter.parse("01/01/1950").getTime());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.updateDate(1950,01,01);

        } catch (ParseException e) {}


    }
    void getExecuteMethods()
    {

        String URL_ = "http://living.indo3dworld.com/api/getCountriesDataService";
        Utils.customProgress(this,"Please Wait ...");
        arrayList.clear();



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppUrl.getCountriesDataService,
                null, response -> {


            try {
                if(response.getString("status").equalsIgnoreCase("1"))
                {
                    System.out.println("Responce..."+response);
                    JSONArray jsonArray = response.getJSONArray("data");



                    for (int i=0; i<jsonArray.length();i++)
                    {
                        Utils.customProgressStop();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ModalCountries modal = new ModalCountries();
                        modal.setId(jsonObject.getString("id"));
                        modal.setCountryName(jsonObject.getString("countryName"));
                        modal.setCountryStatus(jsonObject.getString("countryStatus"));
//                        modal.countryFlag(jsonObject.getString("countryStatus"));
                        modal.setCountryCode(jsonObject.getString("countryCode"));
                        modal.setCallingCode(jsonObject.getString("callingCode"));

                        arrayList.add(modal);

                    }

                    System.out.println("srra"+arrayList.size());

                    adapter = new CountryListAdapter(arrayList);
                    rvList.setAdapter(adapter);


                }
            } catch (JSONException e) {
                Utils.customProgressStop();
                e.printStackTrace();
            }


        }, error -> {
            Utils.customProgressStop();

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(20*2000, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    void listCountries()
    {

        if(Utils.isNetworkAvailable(this))
            getExecuteMethods();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_view_countrieslist,
                viewGroup,
                false);
        builder.setView(dialogView);

        rvList = dialogView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


        alertDialog = builder.create();
        alertDialog.show();
    }

    class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>
    {
        ArrayList<ModalCountries> arrayList;
        public CountryListAdapter(ArrayList<ModalCountries> arrayList) {

            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            ModalCountries modalCountries =  arrayList.get(position);
            holder.txtCountry.setText("+"+modalCountries.getCallingCode()+" , "+modalCountries.getCountryName());
            holder.txtCountry.setOnClickListener(v ->
            {
                txtCountryList.setText("+"+modalCountries.getCallingCode());
                alertDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtCountry;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtCountry = itemView.findViewById(R.id.txtCountry);
            }
        }
    }


    void getRegisteredAccount()
    {

        String fName = inputFName.getText().toString().trim();
        String lName = inputLName.getText().toString().trim();
        String mobile = inputmobile.getText().toString().trim();
        String emailId = inputEmailId.getText().toString().trim();
        String dob = inputDateOfBirth.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();
        String conPass = inputConfirmPass.getText().toString().trim();
        String countryCode = txtCountryList.getText().toString().trim();

        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(emailId)
                && !TextUtils.isEmpty(pass) ){

            if(pass.equalsIgnoreCase(conPass)) {

                Utils.customProgress(this,"Please Wait...");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("confirmPassword",conPass);
                    jsonObject.put("contactNo",mobile);
                    jsonObject.put("countryCode",countryCode);
                    jsonObject.put("dateOfBirth",sendDate);
                    jsonObject.put("email",emailId);
                    jsonObject.put("firstName",fName);
                    jsonObject.put("gender","");
                    jsonObject.put("lastName",lName);
                    jsonObject.put("password",pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Req>>>"+jsonObject);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        AppUrl.registrationService,
                        jsonObject,
                        response -> {

                            System.out.println("Res>>>"+response);

                            Utils.customProgressStop();

                            try {
                                if(response.getString("status").equalsIgnoreCase("1"))
                                    startActivity(new Intent(this,LoginActivity.class));
                                else
                                    Utils.CustomDialog(this,
                                            "Look into the issue",
                                            response.getString("message"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, error -> {
                    Utils.customProgressStop();

                        });

                RequestQueue queue = Volley.newRequestQueue(this);
                request.setRetryPolicy(new DefaultRetryPolicy(20 * 2000, 2,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);

            }else
                Toast.makeText(getApplicationContext(), "Password Mismatch!!!", Toast.LENGTH_LONG).show();
        }
        else

            Toast.makeText(getApplicationContext(),"All feilds are mandatory",Toast.LENGTH_LONG).show();

    }
}

