package com.livingtheapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.livingtheapp.user.utils.AppUrl;
import com.livingtheapp.user.utils.CustomPerference;
import com.livingtheapp.user.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubCategory extends AppCompatActivity {

    RecyclerView rvCatTypes;
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
        rvCatTypes = findViewById(R.id.rvCatTypes);
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



}