package com.example.shaneabe.snakegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SnakeFragment extends Fragment
{
    private SnakeEngine snakeEngine;             // need a reference to the SnakeEngine object for proper Thread creation
    private ImageView mPauseButton;
    private ImageView mResumeButton;
    private static TextView mScoreView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_snake_game, container, false);

        snakeEngine = v.findViewById(R.id.snakeengineview);

        mScoreView = v.findViewById(R.id.score_view);

        mPauseButton = v.findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeEngine.pause();
            }
        });

        mResumeButton = v.findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeEngine.resume();
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        snakeEngine.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        snakeEngine.pause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        snakeEngine.init();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        snakeEngine.tearDown();
    }
}
