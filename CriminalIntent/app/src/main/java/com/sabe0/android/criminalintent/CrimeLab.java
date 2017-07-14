package com.sabe0.android.criminalintent;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
    CrimeLab is a singleton class; only one instance of it can exist while the application is in memory.
*/
public class CrimeLab
{
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    // get the instance of the CrimeLab, or create a new one if it doesn't exist yet.
    public static CrimeLab get(Context context)
    {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);

        return sCrimeLab;
    }

    // private constructor that is called through the get method.
    private CrimeLab(Context context)
    {
        mCrimes = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);    // every other one
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes()
    {
        return mCrimes;
    }

    public Crime getCrime(UUID id)
    {
        for(Crime crime : mCrimes)
        {
            if(crime.getId().equals(id))
                return crime;
        }

        return null;   // If crime was not found, return null
    }
}
