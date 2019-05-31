package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tull.application.Controller.PaidTripsFragment;
import tull.application.Controller.UnPaidTripsFragment;


public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


    private int numTabs;

    public ScreenSlidePagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UnPaidTripsFragment unPaidTripsFragment = new UnPaidTripsFragment();
                return unPaidTripsFragment;
            case 1:
               PaidTripsFragment paidTripsFragment = new PaidTripsFragment();
                return paidTripsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}