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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;
import com.example.Adapter.SRListAdapter;
import com.example.NetWork.AsyncTaskCallBack;
import com.example.NetWork.SGAsyncTask;
import com.example.rank.MainActivity;
import com.example.rank.R;
import com.example.rank.SGSearch;

import java.util.ArrayList;

public class NaverFrag extends Fragment implements AdapterView.OnItemClickListener{

    private final SRListAdapter naverAdapter = new SRListAdapter();
    private ListView listView;
    private SearchView search_view ;
    private ProgressDialog dialog;
    private TextView empty;
    private PullRefreshLayout naverRefresh;

    public NaverFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.naverfrag, container, false);
        search_view = rootView.findViewById(R.id.search_view);
        empty = rootView.findViewById(R.id.empty);
        listView = rootView.findViewById(R.id.naver_list);
        listView.setAdapter(naverAdapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(empty);


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
//입력받은 문자열 처리
                Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=" + query));
                startActivity((browerIntent));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//입련란의 문자열이 바뀔 때 처리

                return false;
            }
        });

        naverRefresh = rootView.findViewById(R.id.naverRefresh);
        naverRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialog = new ProgressDialog(getContext());
                dialog.setMessage(getContext().getString(R.string.dialog_text));
                dialog.setCancelable(false);
                dialog.show();
                final ArrayList<SGSearch> arr = new ArrayList<>();
                new SGAsyncTask(getContext(), arr, SGAsyncTask.NAVER_SITE, new AsyncTaskCallBack() {
                    @Override
                    public void onSuccess() {
                        doOnSuccess(arr);
                    }
                }).execute();
                naverRefresh.setRefreshing(false);
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
        new SGAsyncTask(getContext(), arr, SGAsyncTask.NAVER_SITE, new AsyncTaskCallBack() {
            @Override
            public void onSuccess() {
                doOnSuccess(arr);
            }
        }).execute();
    }



    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == listView) {
            SGSearch item = (SGSearch) listView.getItemAtPosition(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            startActivity(intent);
        }

    }

    private void doOnSuccess(ArrayList<SGSearch> arr) {
        if (arr.size() > 0) {
            naverAdapter.getListItemList().clear();

            for (int i = 0; i < arr.size(); i++) {
                naverAdapter.addItem(arr.get(i));
            }
            naverAdapter.notifyDataSetChanged();

        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
