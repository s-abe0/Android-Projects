package com.sabe0.android.compsciquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

/*
    This program is a knock-off of the GeoQuiz program from the book Android Programming: The Big
    Nerd Ranch Guide, 3rd edition. It is mainly for personal use, to help study and keep my mind fresh
    with computer science/programming concepts while job hunting.

    Currently still in development. Need to add more questions.

    Author: Shane Abe
    Version:
*/

public class MainActivity extends AppCompatActivity
{
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mCorrectTextView;
    private int mCurrentIndex = 0;
    private Question[] mQuestionBank;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeQuestions();                              // Create the questions.

        // Get references to the various buttons and question text view.
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mQuestionTextView = (TextView) findViewById(R.id.question_textview);
        mCorrectTextView = (TextView) findViewById(R.id.correct_textview);

        updateQuestion();                                   // Display the first question.

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
                mCurrentIndex += 1;
                if(mCurrentIndex >= mQuestionBank.length)           // If all questions answered,
                {
                    mTrueButton.setEnabled(false);                  // disable all the buttons,
                    mFalseButton.setEnabled(false);                 // let the user know the quiz is finished,
                    mNextButton.setEnabled(false);
                    mQuestionTextView.setText("End of quiz!");
                    gradeQuiz();                                    // and grade the quiz.
                }
                else
                {
                    updateQuestion();                               // Else, get the next question.
                }
            }
        });
    }

    /*
        Check if the user answered correctly or not. Once the user hits True or False,
        the true/false buttons are disabled, the answer for the question is updated accordingly,
        and the user gets immediate feedback.
    */
    private void checkAnswer(boolean userAnswer)
    {
        boolean answer = mQuestionBank[mCurrentIndex].getAnswer();    // Get the answer to this question.
        int messageResId = 0;

        mTrueButton.setEnabled(false);                                // Disable the buttons.
        mFalseButton.setEnabled(false);

        if(userAnswer == answer)                                      // If the user was correct, display 'Correct!'
        {
            messageResId = R.string.correct;
            mQuestionBank[mCurrentIndex].setWasUserCorrect(true);
        }
        else                                                          // Else, display 'Incorrect!'
        {
            messageResId = R.string.incorrect;
            mQuestionBank[mCurrentIndex].setWasUserCorrect(false);
        }

        mCorrectTextView.setText(messageResId);                       // Let the user know if they were right or wrong.
    }

    /*
        Grade the quiz. Called after the user answers all the questions.
        Currently, displays the grade in a percentage format.
    */
    private void gradeQuiz()
    {
        int correctAnswers = 0;
        int i;
        for(i = 0; i < mQuestionBank.length; i++)
        {
            if(mQuestionBank[i].getWasUserCorrect())
                correctAnswers++;
        }

        float percentage = ((float) correctAnswers / (float) i) * 100;
        DecimalFormat form = new DecimalFormat("0.##");

        mCorrectTextView.setText("Your final score:  %" + form.format(percentage));
    }

    /*
        Display a new question. Called at the beginning of the program and each
        time the Next button is tapped.
    */
    private void updateQuestion()
    {
        // Get the next question and display it.
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        // Re-enable the true/false buttons and remove the Correct/Incorrect text display.
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mCorrectTextView.setText("");
    }

    /*
        Initialize the mQuestionBank with new questions.

        Note: Will be adding more questions soon.
    */
    private void initializeQuestions()
    {
        mQuestionBank = new Question[] {
                new Question(R.string.question_abstraction, false),
                new Question(R.string.question_abstractUML, true),
                new Question(R.string.question_abstractMethod, false),
        };
    }
}
