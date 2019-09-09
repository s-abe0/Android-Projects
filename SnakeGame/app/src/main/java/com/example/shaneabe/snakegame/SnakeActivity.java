package com.example.shaneabe.snakegame;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

/**
 * SnakeActivity's purpose is to host and manage game fragments.
 */
public class SnakeActivity extends FragmentActivity
{
    private MainMenuFragment mainMenu;
    private SnakeFragment snakeFragment;

    /*
        Initialize this activity with a new MainMenuFragment.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mainMenu = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
    }

    /**
        Replaces the MainMenuFragment with a new SnakeFragment, which initializes the game.
     */
    public void startGame()
    {
        snakeFragment = new SnakeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, snakeFragment).commit();
    }
}