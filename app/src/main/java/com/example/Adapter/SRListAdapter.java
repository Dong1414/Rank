package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rank.R;
import com.example.rank.SGSearch;

import java.util.ArrayList;

public class SRListAdapter extends BaseAdapter {
    private ArrayList<SGSearch> listItemList = new ArrayList<>();
    public SRListAdapter(){
    }

    public ArrayList<SGSearch> getListItemList(){
        return listItemList;
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder)view.getTag();
        } else {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.rank_listview_item, null);
            holder.titleView = view.findViewById(R.id.item_text);

            view.setTag(holder);
        }

        final SGSearch listItem = listItemList.get(position);
        holder.titleView.setText(listItem.getRank()+listItem.getTitle());

        return view;
    }
    public void addItem(SGSearch item){
        listItemList.add(item);
    }
    private class ViewHolder{
        private TextView titleView;
    }
}
