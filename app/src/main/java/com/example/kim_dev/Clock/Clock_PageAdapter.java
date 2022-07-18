package com.example.kim_dev.Clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Clock_PageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mDate;
    private ArrayList<String> name = new ArrayList<>();

    public Clock_PageAdapter(@NonNull FragmentManager fm) {
        super(fm);

        mDate = new ArrayList<>();
        mDate.add(new Clock_Fragment_1());
        mDate.add(new Clock_Fragment_2());
        mDate.add(new Clock_Fragment_3());

        name.add("현재 시간");
        name.add("스탑워치");
        name.add("타이머");
    }

    // 자동 Tab_Title
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  name.get(position);
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public int getCount() {
        return  mDate.size();
    }
}
