package com.example.serverdoancn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.serverdoancn.fragment.CartFragment;
import com.example.serverdoancn.fragment.HomeFragment;
import com.example.serverdoancn.fragment.InfoFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 2)
            return new InfoFragment();
        else if (position == 1) {
            return new CartFragment();
        }else
            return new  HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
