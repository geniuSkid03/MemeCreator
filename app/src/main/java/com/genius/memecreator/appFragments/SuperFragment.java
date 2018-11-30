package com.genius.memecreator.appFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.genius.memecreator.appDatas.DataStorage;
import com.genius.memecreator.appUtils.AppHelper;
import com.genius.memecreator.appUtils.AppPermissionsHelper;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SuperFragment extends Fragment {

    protected Unbinder unbinder;
    protected DataStorage dataStorage;
    protected Gson gson = new Gson();
    protected ProgressDialog progressDialog;
    public AppPermissionsHelper appPermissionsHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataStorage = new DataStorage(getContext());

        progressDialog = new ProgressDialog(getContext());

        appPermissionsHelper = new AppPermissionsHelper(getActivity(), dataStorage);

        AppHelper.print("OnCreate Fragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppHelper.print("OnViewCreated Fragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppHelper.print("OnCreateView Fragment");

        View view = inflateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    //for starting activity with single intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                getActivity().finish();
            }
        }
    }

    protected void showProgress(String msg) {
        if(progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    protected void cancelProgress() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showToast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
