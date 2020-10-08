package com.example.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rank.R;
import com.example.rank.YouSearch;

import java.util.ArrayList;

public class YouListAdapter extends BaseAdapter {
    private ArrayList<YouSearch> listViewItemList = new ArrayList<YouSearch>() ;

    public YouListAdapter(){

    }
    public ArrayList<YouSearch> getListItemList(){
        return listViewItemList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(YouSearch item) {

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.you_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
    //    ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        YouSearch listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
  //      iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getTotal());

        return convertView;
    }

}
