package com.example.Frag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;

import com.example.Adapter.YouListAdapter;
import com.example.NetWork.AsyncTaskCallBack;
import com.example.NetWork.YouAsyncTask;
import com.example.rank.R;
import com.example.rank.YouSearch;

import java.util.ArrayList;

public class YouFrag extends Fragment implements AdapterView.OnItemClickListener {

    YouListAdapter adapter ;
    private ListView listView;
    private ProgressDialog dialog;
    private TextView empty;
    private PullRefreshLayout youRefresh;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.youfrag, container, false);
        adapter = new YouListAdapter() ;

        listView = (ListView)rootView.findViewById(R.id.you_list);
        listView.setAdapter(adapter);
        youRefresh = rootView.findViewById(R.id.youRefresh);
        youRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialog = new ProgressDialog(getContext());
                dialog.setMessage(getContext().getString(R.string.dialog_text));
                dialog.setCancelable(false);
                dialog.show();
                final ArrayList<YouSearch> arr = new ArrayList<>();
                new YouAsyncTask(getContext(), arr, YouAsyncTask.YOUTUBE_SITE, new AsyncTaskCallBack() {
                    @Override
                    public void onSuccess() {
                        doOnSuccess(arr);
                    }
                }).execute();
                youRefresh.setRefreshing(false);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getString(R.string.dialog_text));
        dialog.setCancelable(false);
        dialog.show();
        final ArrayList<YouSearch> arr = new ArrayList<>();
        new YouAsyncTask(getContext(), arr, YouAsyncTask.YOUTUBE_SITE, new AsyncTaskCallBack() {
            @Override
            public void onSuccess() {
                doOnSuccess(arr);
            }
        }).execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == listView) {
            YouSearch item = (YouSearch) listView.getItemAtPosition(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            startActivity(intent);
        }
    }
    private void doOnSuccess(ArrayList<YouSearch> arr) {
        if (arr.size() > 0) {
            adapter.getListItemList().clear();
            for (int i = 0; i < 10; i++) {
                adapter.addItem(arr.get(i));
            }
            adapter.notifyDataSetChanged();
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
