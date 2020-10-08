package com.example.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Frag.GoogleFrag;
import com.example.Frag.NaverFrag;
import com.example.Frag.YouFrag;

public class ViewAdapter extends FragmentStateAdapter {
    public int mCount;

    public ViewAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new GoogleFrag();
        else if(index==1) return new NaverFrag();
        else return new YouFrag();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) { return position % (mCount); }
}
