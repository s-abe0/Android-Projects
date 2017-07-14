package com.sabe0.android.compsciquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
    This program is a knock-off of the GeoQuiz program from the book Android Programming: The Big
    Nerd Ranch Guide, 3rd edition. It is mainly for personal use, to help study and keep my mind fresh
    with computer science/programming concepts.

    Currently still in development. Need to add more questions.

    Author: Shane Abe
    Version:
*/

public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null)
        {
            fragment = new QuestionFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
