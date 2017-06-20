package com.sabe0.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity
{
    private boolean mAnswer;
    private boolean isAnswerShown;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private static final String KEY_INDEX = "index";
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.sabe0.android.geoquiz.answer_is_true"; // use package name to prevent collisions from other apps
    private static final String EXTRA_ANSWER_SHOWN =
            "com.sabe0.android.geoquiz.answer_shown";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i("CheatActivity", "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_INDEX, isAnswerShown);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i("CheatActivity", "onCreateBundle");
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null)
        {
            Log.i("CheatActivity", "savedInstance not null");
            isAnswerShown = savedInstanceState.getBoolean(KEY_INDEX, false);

            if(isAnswerShown)
                setAnswerShownResult();
        }

        // Activity.getIntent() always returns the Intent that started the Activity.
        // In this case, we are getting the boolean extra that was put into the intent.
        mAnswer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mAnswer)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);

                // if the user clicks the show answer button, let the calling activity know.
                setAnswerShownResult();
            }
        });
    }

    /*
        Called by parent classes to create new Intents for starting this activity.
        Pass a Context and a Class; Context tells ActivityManager (part of OS) which app package the activity class
        can be found in. Class tells ActivityManager which class to start. This creates an explicit intent.
     */
    public static Intent newIntent(Context packageContext, boolean answer)
    {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);                    // add the questions answer as an extra.
        return intent;
    }

    /*
        Called if the user clicks the Cheat button. Creates a new Intent, puts an extra in it that indicates
        true if the answer was shown, and sets the result code to OK with the new Intent.
     */
    private void setAnswerShownResult()
    {
        isAnswerShown = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result)
    {
        return result.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    }
}
