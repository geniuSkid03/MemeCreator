package com.genius.memecreator.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appDatas.TemplatePool;
import com.genius.memecreator.appUtils.AppHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class TemplatesFragment extends SuperFragment {

    private ListView templatesLv;

    private ArrayAdapter<TemplatePool> templatePoolArrayAdapter;
    private ArrayList<TemplatePool> templatePoolArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        templatePoolArrayList = new ArrayList<>();

        templatePoolArrayList.add(new TemplatePool("Vadivelu", String.valueOf(AppHelper.getUriFromDrawable(getContext(), R.drawable.vaidvelu)), 150));
        templatePoolArrayList.add(new TemplatePool("Santhanam", String.valueOf(AppHelper.getUriFromDrawable(getContext(), R.drawable.santhanam)), 65));

        templatePoolArrayAdapter = new ArrayAdapter<TemplatePool>(getContext(), R.layout.template_pool_item, templatePoolArrayList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_pool_item, parent, false);
                }

                ((TextView) convertView.findViewById(R.id.t_name)).setText(templatePoolArrayList.get(position).gettName());
                ((TextView) convertView.findViewById(R.id.t_count)).setText(String.format(Locale.getDefault(), "%d %s", templatePoolArrayList.get(position).gettCount(), getString(R.string.items)));
                Picasso.get().load(templatePoolArrayList.get(position).gettUrl()).into((ImageView) convertView.findViewById(R.id.t_img));

                return convertView;
            }

            @Override
            public int getCount() {
                return templatePoolArrayList.size();
            }

            @Nullable
            @Override
            public TemplatePool getItem(int position) {
                return templatePoolArrayList.get(position);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        templatesLv.setAdapter(templatePoolArrayAdapter);
    }

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(layoutInflater.inflate(R.layout.fragment_templates, container, false));
    }

    private View initView(View view) {
        templatesLv = view.findViewById(R.id.templates);
        templatesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openClicked(position);
            }
        });

        return view;
    }

    private void openClicked(int position) {

    }


}
