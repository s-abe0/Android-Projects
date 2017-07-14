package com.sabe0.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
{
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
