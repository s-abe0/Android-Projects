package com.sabe0.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
    This program is from the first five chapters of the book Android Programming: The Big Nerd Ranch Guide, 3rd edition,
    with some of the end of chapter challenges completed (I'm not listing them all).
*/

public class QuizActivity extends AppCompatActivity
{
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    public void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);   // save the current mCurrentIndex value
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);  // Inflates a layout and puts it on the screen. Layout to inflate
                                                 // is specified by the passed parameter; the layouts resource ID.

        // Check for previous saved mCurrentIndex value (if user rotated the screen)
        if(savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);   // Get references to the button widgets. findViewById returns a View object,
        mFalseButton = (Button) findViewById(R.id.false_button);  // so a Button cast is needed.
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        updateQuestion();

        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;   // reset for next question.
                updateQuestion();
            }
        });

        // If user clicks previous button, updated the mCurrentIndex value and call the updateQuestion() method.
        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mCurrentIndex == 0)
                {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                else
                {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }

                updateQuestion();
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Start CheatActivity
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

            }
        });
    }

    private void checkAnswer(boolean userAnswer)
    {
        mQuestionBank[mCurrentIndex].setUserAnswer(userAnswer);             // set the users answer (true or false).
        mQuestionBank[mCurrentIndex].setWasAnswered(true);                  // indicate that this answer was answered.
        boolean answer = mQuestionBank[mCurrentIndex].isAnswerTrue();       // get the answer for this question.
        int messageResId = 0;

        // Disable the buttons
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        // Get the answer and indicate the user
        if(mIsCheater)
        {
            messageResId = R.string.judgment_toast;
        }
        else if(userAnswer == answer)
        {
            messageResId = R.string.correct_toast;
        }
        else
        {
            messageResId = R.string.incorrect_toast;
        }

        Toast t = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.TOP, 0, 500);
        t.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // If the user did not cheat, we don't need to do anything.
        if(resultCode != Activity.RESULT_OK)
            return;

        // If this activity result is from the CheatActivity (requestCode == 0)
        if(requestCode == REQUEST_CODE_CHEAT)
        {
            if(data == null)
                return;

            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    /*
        Change to a new question. This method is called each time the previous or next button is clicked.
        The event of the prev/next buttons updates the mCurrentIndex value, which is then used by this method.
    */
    private void updateQuestion()
    {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        // If this question was already answered, disable the true and false buttons.
        if(mQuestionBank[mCurrentIndex].getWasAnswered() == true)
        {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        // Else, enable the true/false buttons if needed.
        else
        {
            if(!mTrueButton.isEnabled())
            {
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }
        }
    }
}
