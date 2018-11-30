package com.genius.memecreator.appUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.memecreator.appAdapters.TrendingImagesAdapter;
import com.genius.memecreator.appDatas.TrendingMemes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocalDbConnection {

    public static final String URL = "http://192.168.1.7/Android/MemeFactory/api.php";

    private ArrayList<TrendingMemes> trendingMemesArrayList = new ArrayList<>();
    private TrendingMemes trendingMemes;
    private RecyclerView recyclerView;
    private TrendingImagesAdapter trendingImagesAdapter;
    private Context context;

    public LocalDbConnection(Context context, RecyclerView recyclerView, TrendingImagesAdapter trendingImagesAdapter, TrendingMemes trendingMemes, ArrayList<TrendingMemes> trendingMemesArrayList) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.trendingMemes = trendingMemes;
        this.trendingMemesArrayList = trendingMemesArrayList;
        this.trendingImagesAdapter = trendingImagesAdapter;

        //loadMemes();
    }

    private void loadMemes() {

        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                trendingMemesArrayList.add(new TrendingMemes(
//                        jsonObject.getInt(Keys.ID),
//                        jsonObject.getString(Keys.NAME),
//                        jsonObject.getString(Keys.IMAGE)
//                        //,jsonObject.getInt(Keys.IS_FAV)
//                ));

                recyclerView.setAdapter(trendingImagesAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    interface serverFunction {
        void onStart();
        void onFinish();
    }
}
