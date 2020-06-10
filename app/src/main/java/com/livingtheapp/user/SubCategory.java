package com.livingtheapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SubCategory extends AppCompatActivity {

    RecyclerView rvCatTypes,rvFilterCat;
    TextView txtTitle,txtNoRec;
    ImageView imgBack,imgBackblur;

    FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;
    int PERMISSION_ID = 44;
    String blankId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        initView();



    }

    void initView()
    {

        txtNoRec = findViewById(R.id.txtNoRec);
        imgBackblur = findViewById(R.id.imgBackblur);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("name"));

        rvFilterCat = findViewById(R.id.rvFilterCat);
        rvCatTypes = findViewById(R.id.rvCatTypes);
        rvFilterCat.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rvCatTypes.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        Picasso.get()
                .load(R.drawable.nightlifesubcat)
                .transform(new BlurTransformation(SubCategory.this, 25, 1))
                .into(imgBackblur);

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
                    }, error -> { });

            RequestQueue queue = Volley.newRequestQueue(SubCategory.this);
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 2000, 2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                JSONObject object2 = (JSONObject) array.get(0);
                String defId = object.getString("id");
                holder.txtSubCatName.setText(object.getString("name"));
                String id = object.getString("id");
                String catid = getIntent().getStringExtra("catid");

//                getFilterSubCat(object2.getString("id"));
                holder.txtSubCatName.setOnClickListener(v -> getFilterSubCat(id,catid));

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

    void getFilterSubCat(String id, String catid)
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
            object.put("catid", catid);
            object.put("id", id);
            object.put("lat1", latitude);
            object.put("lon1", longitude);
            object.put("sortby", "asc");
            object.put("token", CustomPerference.getString(SubCategory.this,CustomPerference.USER_TOKEN));
            object.put("unit", "K");
        }catch
        (Exception e){}

        System.out.println("res..."+object);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.getSubCategories,
                object,
                response -> {

                    System.out.println("res..."+response);
            try {
                if (response.getString("status").equalsIgnoreCase("1")) {

                    JSONArray jsonArray = response.getJSONArray("data");
                    if(jsonArray.isNull(0))
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtNoRec.setVisibility(View.GONE);
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
                holder.txtDistance.setText(String.valueOf(dis)+" K.M");
                holder.cardView.setOnClickListener(v ->
                {
                    try {
                        startActivity(new Intent(SubCategory.this,CategoryDescription.class)
                        .putExtra("name",object.getString("vendorname"))
                        .putExtra("locId",object.getString("locationid"))
                        .putExtra("vid",object.getString("vendorid")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

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
            CardView cardView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.CardView);


                txtVendName = itemView.findViewById(R.id.txtVendName);
                txtLocation = itemView.findViewById(R.id.txtLocation);
                txtArea = itemView.findViewById(R.id.txtArea);
                txtDistance = itemView.findViewById(R.id.txtDistance);
                imgVen = itemView.findViewById(R.id.imgVen);
            }
        }
    }







    /*GPSLOCATION SERVICES ENABLE*/



    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }
    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                System.out.println("Lat"+location.getLatitude());
                                System.out.println("Lang"+location.getLongitude());

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                getCompleteAddress();
                                if(Utils.isNetworkAvailable(this))
                                    getExecuteHorizontal();
                                    getFilterSubCat(blankId,getIntent().getStringExtra("catid"));
//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            System.out.println("Lat"+mLastLocation.getLatitude());
            System.out.println("Lang"+mLastLocation.getLongitude());

            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

//            latTextView.setText(mLastLocation.getLatitude()+"");
//            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };
    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
    void getCompleteAddress()
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}