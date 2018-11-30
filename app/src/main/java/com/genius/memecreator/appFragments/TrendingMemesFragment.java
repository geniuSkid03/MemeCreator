package com.genius.memecreator.appFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.memecreator.R;
import com.genius.memecreator.appActivities.EditorActivity;
import com.genius.memecreator.appAdapters.TrendingImagesAdapter;
import com.genius.memecreator.appDatas.TrendingMemes;
import com.genius.memecreator.appInterfaces.FragmentRefreshser;
import com.genius.memecreator.appInterfaces.TrendingImageClickListener;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.Keys;
import com.genius.memecreator.appUtils.LocalDbConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrendingMemesFragment extends SuperFragment {

    private RecyclerView trendingRv;
    private LinearLayout emptyTrendingLayout;
    private RelativeLayout recyclerViewLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TrendingImagesAdapter trendingImagesAdapter;
    private ArrayList<TrendingMemes> trendingMemesArrayList;

    private FragmentRefreshser fragmentRefreshser;
    private boolean isLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trendingMemesArrayList = new ArrayList<>();

        trendingImagesAdapter = new TrendingImagesAdapter(getActivity(), trendingMemesArrayList);
        trendingImagesAdapter.setTrendingImageClickListener(new TrendingImageClickListener() {
            @Override
            public void onShareClicked(ImageView trendingIv, int position) {
                shareImageClicked(trendingMemesArrayList.get(position), trendingIv);
            }

            @Override
            public void onEditClicked(int position) {
                editImageClicked(trendingMemesArrayList.get(position));
            }
        });
    }

    private void editImageClicked(TrendingMemes trendingMemes) {
        goTo(getActivity(), EditorActivity.class, false, Keys.TO_EDIT, gson.toJson(trendingMemes));
    }

    private Uri shareImageUri;
    private void shareImageClicked(TrendingMemes trendingMemes, ImageView trendingIv) {
        if (AppHelper.isAboveM()) {
            if (appPermissionsHelper.isStoragePermissionsAvailable()) {
                shareImageUri = AppHelper.getUriFromImage(getActivity(), trendingIv);
            } else {
                appPermissionsHelper.initPermissions(new String[]{Keys.READ_STORAGE, Keys.WRITE_STORAGE});
                appPermissionsHelper.askAllPermission();
            }
        } else {
            shareImageUri = AppHelper.getUriFromImage(getActivity(), trendingIv);
        }

        if (shareImageUri != null) {
            startActivity(Intent.createChooser(
                    new Intent().setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, shareImageUri)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setType("image/*"), "Share MEME via"
            ));
        } else {
            showToast(getActivity(), "Sharing failed!");
        }
    }

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(layoutInflater.inflate(R.layout.fragment_trending_memes, container, false));
    }

    private View createView(View view) {

        trendingRv = view.findViewById(R.id.trending_recycler);
        emptyTrendingLayout = view.findViewById(R.id.empty_trending_view);
        recyclerViewLayout = view.findViewById(R.id.rv_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTrendingMemes();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        trendingRv.setAdapter(trendingImagesAdapter);
    }

    public void refreshTrendingMemes() {
        if (getActivity() == null) {
            AppHelper.print("Context null in home fragment");
            return;
        }

        if (!AppHelper.isNetworkAvailable(getActivity())) {
            AppHelper.print("HomeFragment no internet");
            return;
        }

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        showProgress(getString(R.string.loading));

        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, LocalDbConnection.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.print("Error in parsing data: " + error.toString());
                cancelProgress();
                parseResponse(null);
            }
        }));
    }

    private void parseResponse(String response) {
        if (response == null) {
            cancelProgress();
            trendingMemesArrayList.clear();
            updateUi(trendingMemesArrayList);
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(response);

            trendingMemesArrayList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                trendingMemesArrayList.add(new TrendingMemes(
                        jsonObject.getInt(Keys.ID),
                        jsonObject.getString(Keys.NAME),
                        jsonObject.getString(Keys.IMAGE),
                        jsonObject.getInt(Keys.IS_FAV)
                ));

                AppHelper.print(String.valueOf(jsonObject.getInt(Keys.IS_FAV)));

                trendingImagesAdapter.notifyDataSetChanged();
                updateUi(trendingMemesArrayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            cancelProgress();
            AppHelper.print("Exception while parsing data!");
        }
    }

    private void updateUi(ArrayList<TrendingMemes> trendingMemesArrayList) {
        cancelProgress();
        trendingRv.setVisibility(trendingMemesArrayList.size() != 0 ? View.VISIBLE : View.GONE);
        emptyTrendingLayout.setVisibility(trendingMemesArrayList.size() != 0 ? View.GONE : View.VISIBLE);
    }

    public void setFragmentRefreshser(FragmentRefreshser fragmentRefreshser) {
        this.fragmentRefreshser = fragmentRefreshser;
    }

    public FragmentRefreshser getFragmentRefreshser() {
        return fragmentRefreshser;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentRefreshser() != null) {
                fragmentRefreshser.onRefresh();
            }
        }

    }
}
