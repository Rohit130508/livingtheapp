package com.livingtheapp.user.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.livingtheapp.user.R;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OffersFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OffersFrag  extends Fragment {
    private static final String TAG = "SpeedDial";


    RecyclerView rvList;
    private String locid, vendorId;

    public OffersFrag() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static OffersFrag newInstance() {
        return new OffersFrag();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        rvList = root.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        locid = getArguments().getString("locId");
        vendorId = getArguments().getString("vid");

        if(Utils.isNetworkAvailable(getActivity()))
            getVendorOffers();
        return root;
    }
    class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder>
    {

        JSONArray jsonArray1;
        public OfferAdapter(JSONArray jsonArray1) {
            this.jsonArray1 = jsonArray1;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer,
                    parent, false);


            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return jsonArray1.length();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    void getVendorOffers()
    {
        JSONObject object = new JSONObject();
        try {
            object.put("locationid", locid);
            object.put("vendorid", vendorId);
            object.put("token", CustomPerference.getString(getActivity(),CustomPerference.USER_TOKEN));
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("offersList");

                            if(jsonArray1.isNull(0))
                                return;

                            else {

                                OfferAdapter adapter = new OfferAdapter(jsonArray1);
                                rvList.setAdapter(adapter);
                            }
                        }
                    }catch (Exception e){}

                }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy( 10*2000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



}
