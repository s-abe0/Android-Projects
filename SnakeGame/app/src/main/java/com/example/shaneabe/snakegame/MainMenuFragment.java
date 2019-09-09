package com.example.shaneabe.snakegame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * MainMenuFragment hosts the main menu that is displayed to the user at startup.
 */
public class MainMenuFragment extends Fragment
{
    private Button mStartButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        mStartButton = v.findViewById(R.id.startButton);

        mStartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((SnakeActivity) getActivity()).startGame();        // When the user clicks 'Start Game', the hosting activity's startGame method
            }                                                       // will replace this fragment with a SnakeFragment, which starts the game.
        });

        return v;
    }
}
