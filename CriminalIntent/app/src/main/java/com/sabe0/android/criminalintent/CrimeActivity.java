package com.sabe0.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity
{
    // Implementation of abstract method createFragment(). Simply returns a new CrimeFragment object.
    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragment();
    }
}
