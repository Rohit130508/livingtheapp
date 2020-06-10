package com.livingtheapp.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.livingtheapp.adapter.TabsPagerAdapter;
import com.livingtheapp.user.fragment.OffersFrag;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class CategoryDescription extends AppCompatActivity {

    TextView txtTitle;
    private String locid, name, vendorId;
    ImageView imgBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_category_description);

        initView();

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void initView()
    {
        imgBanner = findViewById(R.id.imgBanner);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("name"));

        locid = getIntent().getStringExtra("locId");
        name = getIntent().getStringExtra("name");
        vendorId = getIntent().getStringExtra("vid");

        if(Utils.isNetworkAvailable(this))
            getVendorOffers();

        LoadFragment(new OffersFrag());
    }

    void getVendorOffers()
    {
        JSONObject object = new JSONObject();
        try {
            object.put("locationid", locid);
            object.put("vendorid", vendorId);
            object.put("token", CustomPerference.getString(this,CustomPerference.USER_TOKEN));
        }catch
        (Exception e){}

        System.out.println("res..."+object);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.getVendorOffers,
                object,
                response -> {

                    System.out.println("res..."+response);
                    try {
                        if (response.getString("status").equalsIgnoreCase("1")) {

                            JSONObject jsonObject = response.getJSONObject("data");
                            JSONArray jsonArray = jsonObject.getJSONArray("imagefiles");
                            if(jsonArray.isNull(0))
                            {
//                                Utils.Picasso();
//                                txtNoRec.setVisibility(View.VISIBLE);
                            }
                            else {
//                                Utils.Picasso((String) jsonArray.get(0),imgBanner);

//                                txtNoRec.setVisibility(View.GONE);
//                                SubCategory.SubCatFilterAdapter adapter = new SubCategory.SubCatFilterAdapter(jsonArray);
//                                rvFilterCat.setAdapter(adapter);
                                System.out.println("res..." + response);
                            }
                        }
                    }catch (Exception e){}

                }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy( 10*2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    void LoadFragment(Fragment fragment)
    {

        Bundle bundle = new Bundle();
        bundle.putString("locId", locid);
        bundle.putString("vid", vendorId);
        fragment.setArguments(bundle);


        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();


    }

}
