package com.livingtheapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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
import com.livingtheapp.user.auth.ModalCountries;
import com.livingtheapp.user.auth.RegistrationActivity;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    ArrayList<ModalCountries> arrayList;

    private ViewPager vp_slider;
    TextView txtCountry,txtImgMsg;
    private int images_vp[] = {R.drawable.a, R.drawable.b, R.drawable.c};
    ArrayList vparray = new ArrayList<String>();

    private SliderPagerAdapter myCustomPagerAdapter;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private CountryListAdapter adapter;
    private RecyclerView rvList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = CustomPerference.getString(this,CustomPerference.USER_ID);
        if (userId!=null)
        setContentView(R.layout.activity_main);
        else setContentView(R.layout.activity_main_notlogin);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        vp_slider = findViewById(R.id.vp_slider);




        myCustomPagerAdapter = new SliderPagerAdapter(this, images_vp);
        vp_slider.setAdapter(myCustomPagerAdapter);

        CircleIndicator indicator =  findViewById(R.id.indicator);
        indicator.setViewPager(vp_slider);

        NUM_PAGES = imageModelArrayList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;

            }

            vp_slider.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);


         arrayList = new ArrayList<>();
        initView();

    }

    void initView()
    {
        txtImgMsg = findViewById(R.id.txtImgMsg);
        txtImgMsg.setText("Welcome "+ CustomPerference.getString(this,CustomPerference.USER_NAME)+" " +
                "Enjoy the best offers and deals here !");
        txtCountry = findViewById(R.id.txtCountry);
//        txtCountry.setOnClickListener(v -> listCountries());
        findViewById(R.id.imgBeau).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
        findViewById(R.id.imgshop).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
        findViewById(R.id.imgcycle).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
        findViewById(R.id.imgDrin).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
        findViewById(R.id.imgRest).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
        findViewById(R.id.imgBag).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"Working on this",Toast.LENGTH_LONG).show());
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for (int value : images_vp) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(value);
            list.add(imageModel);
        }

        return list;
    }

    public static class ImageModel {

        private int image_drawable;

        public int getImage_drawable() {
            return image_drawable;
        }

        public void setImage_drawable(int image_drawable) {
            this.image_drawable = image_drawable;
        }
    }

    class SliderPagerAdapter extends PagerAdapter {
        Context context;
        int[] images;
        LayoutInflater layoutInflater;


        public SliderPagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.vp_slider_item, container, false);

            ImageView iv_vp_slider = itemView.findViewById(R.id.iv_vp_slider);
            iv_vp_slider.setImageResource(images[position]);

            container.addView(itemView);

            //listening to image click
            iv_vp_slider.setOnClickListener(v -> {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
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

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20*2000, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    void listCountries()
    {

        if(Utils.isNetworkAvailable(this))
            getExecuteMethods();



         builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_view_countrieslist,
                viewGroup,
                false);
        builder.setView(dialogView);

        rvList = dialogView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));


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
        public CountryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_list, parent, false);
            return new CountryListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CountryListAdapter.ViewHolder holder, int position) {

            ModalCountries modalCountries =  arrayList.get(position);
            holder.txtCountry.setText("+"+modalCountries.getCallingCode()+" , "+modalCountries.getCountryName());
            holder.txtCountry.setOnClickListener(v ->
            {
                txtCountry.setText(modalCountries.getCountryName());
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


    void getSliderImages()
    {
        JSONObject object = new JSONObject();
        try {
            object.put("token",CustomPerference.getString(this,CustomPerference.USER_TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.getSliderImageUrls,
                object, response -> {

            try {
                if(response.getString("status").equalsIgnoreCase("1"))
                {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i=0; i<jsonArray.length();i++)
                    {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        vparray.add(object1.getString("imageUrl"));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));
        queue.add(request);
    }

}
