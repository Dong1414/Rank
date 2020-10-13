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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;
import com.example.Adapter.SRListAdapter;
import com.example.NetWork.AsyncTaskCallBack;
import com.example.NetWork.SGAsyncTask;
import com.example.rank.R;
import com.example.rank.SGSearch;

import java.util.ArrayList;

public class GoogleFrag extends Fragment implements AdapterView.OnItemClickListener {
    private SearchView search_view;
    private final SRListAdapter googleAdapter = new SRListAdapter();
    private ListView listView;
    private ProgressDialog dialog;
    private TextView empty;
    private PullRefreshLayout googleRefresh;
    public GoogleFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.googlefrag, container, false);
        search_view = rootView.findViewById(R.id.search_view);
        empty = rootView.findViewById(R.id.empty);
        listView = rootView.findViewById(R.id.google_list);
        listView.setAdapter(googleAdapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(empty);


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
//입력받은 문자열 처리
                Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + query));
                startActivity((browerIntent));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//입련란의 문자열이 바뀔 때 처리

                return false;
            }
        });


        googleRefresh = rootView.findViewById(R.id.googleRefresh);
        googleRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialog = new ProgressDialog(getContext());
                dialog.setMessage(getContext().getString(R.string.dialog_text));
                dialog.setCancelable(false);
                dialog.show();
                final ArrayList<SGSearch> arr = new ArrayList<>();
                new SGAsyncTask(getContext(), arr, SGAsyncTask.GOOGLE_SITE, new AsyncTaskCallBack() {
                    @Override
                    public void onSuccess() {
                        doOnSuccess(arr);
                    }
                }).execute();
                googleRefresh.setRefreshing(false);
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
        final ArrayList<SGSearch> arr = new ArrayList<>();
        new SGAsyncTask(getContext(), arr, SGAsyncTask.GOOGLE_SITE, new AsyncTaskCallBack() {
            @Override
            public void onSuccess() {
                doOnSuccess(arr);
            }
        }).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SGSearch item = (SGSearch)listView.getItemAtPosition(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        startActivity(intent);
    }
    private void doOnSuccess(ArrayList<SGSearch> arr) {
        if (arr.size() > 0) {
            googleAdapter.getListItemList().clear();
            for (int i = 0; i < 10; i++) {
                googleAdapter.addItem(arr.get(i));
            }
            googleAdapter.notifyDataSetChanged();
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
