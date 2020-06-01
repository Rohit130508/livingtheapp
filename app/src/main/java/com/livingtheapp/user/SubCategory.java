package com.livingtheapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubCategory extends AppCompatActivity {

    RecyclerView rvCatTypes,rvFilterCat;
    TextView txtTitle;
    ImageView imgBack;

    FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        initView();
        if(Utils.isNetworkAvailable(this))
            getExecuteHorizontal();
    }

    void initView()
    {

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("name"));

        rvFilterCat = findViewById(R.id.rvFilterCat);
        rvCatTypes = findViewById(R.id.rvCatTypes);
        rvFilterCat.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rvCatTypes.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
    }
    void getExecuteHorizontal()
    {
        JSONObject object = new JSONObject();
        try {
            object.put("id",getIntent().getStringExtra("id"));
            object.put("token", CustomPerference.getString(this,CustomPerference.USER_TOKEN));

            System.out.println("res>>>"+object);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.getMainSubCategories,
                    object,
                    response -> {
                        try {
                            if(response.getString("status").equalsIgnoreCase("1"))
                            {
                                JSONArray array = response.getJSONArray("data");
                                SubCatAdapter adapter = new SubCatAdapter(array);
                                rvCatTypes.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {

                    });
            RequestQueue queue = Volley.newRequestQueue(SubCategory.this);
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder>
    {

        JSONArray array;

        public SubCatAdapter(JSONArray array) {
            this.array = array;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcat,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            try {
                JSONObject object = (JSONObject) array.get(position);
                holder.txtSubCatName.setText(object.getString("name"));
                String id = object.getString("id");
                holder.txtSubCatName.setOnClickListener(v -> getFilterSubCat(id));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtSubCatName ;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtSubCatName = itemView.findViewById(R.id.txtSubCatName);
            }
        }
    }

    void getFilterSubCat(String id)
    {

        JSONArray arrayAmenities = new JSONArray();
        JSONArray arrayCityFilter = new JSONArray();
        JSONArray arrayCountryFilter = new JSONArray();
        JSONArray arrayCuisinesFilter = new JSONArray();
        JSONArray arrayFilterWith = new JSONArray();

        JSONObject object = new JSONObject();
        try {
            object.put("amenitiesFilter", arrayAmenities);
            object.put("cityFilter", arrayCityFilter);
            object.put("countryFilter", arrayCountryFilter);
            object.put("cuisinesFilter", arrayCuisinesFilter);
            object.put("filterValue", "");
            object.put("filtersWith", arrayFilterWith);
            object.put("id", id);
            object.put("lat1", 26.846251);
            object.put("lon1", 80.94902);
            object.put("sortby", "asc");
            object.put("token", CustomPerference.getString(SubCategory.this,CustomPerference.USER_TOKEN));
            object.put("unit", "K");
        }catch
        (Exception e){}

        System.out.println("res..."+object);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.getSubCategories,
                object,
                response -> {


            try {
                if (response.getString("status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = response.getJSONArray("data");
                    if(jsonArray.isNull(0))
                    {
                        Toast.makeText(getApplicationContext(),"Nodata",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        SubCatFilterAdapter adapter = new SubCatFilterAdapter(jsonArray);
                        rvFilterCat.setAdapter(adapter);
                        System.out.println("res..." + response);
                    }
                }
            }catch (Exception e){}

                }, error -> {

                });

        RequestQueue queue = Volley.newRequestQueue(SubCategory.this);
        request.setRetryPolicy(new DefaultRetryPolicy( 10*2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    class SubCatFilterAdapter extends RecyclerView.Adapter<SubCatFilterAdapter.ViewHolder>
    {

        JSONArray jsonArray;
        public SubCatFilterAdapter(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_list,
                    parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            try {
                JSONObject object =(JSONObject) jsonArray.get(position);
                holder.txtVendName.setText(object.getString("vendorname"));
                holder.txtLocation.setText(object.getString("location"));
                holder.txtArea.setText(object.getString("cuisinesStr"));
                float dis = object.getLong("distance");
                holder.txtDistance.setText(String.valueOf(dis));
                Utils.Picasso(object.getString("logofile"),holder.imgVen);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtVendName,txtLocation,txtArea,txtDistance;
            ImageView imgVen;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtVendName = itemView.findViewById(R.id.txtVendName);
                txtLocation = itemView.findViewById(R.id.txtLocation);
                txtArea = itemView.findViewById(R.id.txtArea);
                txtDistance = itemView.findViewById(R.id.txtDistance);
                imgVen = itemView.findViewById(R.id.imgVen);
            }
        }
    }


}