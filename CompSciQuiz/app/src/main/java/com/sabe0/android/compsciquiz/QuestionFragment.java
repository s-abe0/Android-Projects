package com.sabe0.android.compsciquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class QuestionFragment extends Fragment
{
    private Question[] mQuestionBank;
    private int mCurrentIndex = 0;
    private boolean eoq_flag = false;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mCorrectTextView;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeQuestions();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        mTrueButton = (Button) v.findViewById(R.id.true_button);
        mFalseButton = (Button) v.findViewById(R.id.false_button);
        mNextButton = (Button) v.findViewById(R.id.next_button);
        mQuestionTextView = (TextView) v.findViewById(R.id.question_textview);
        mCorrectTextView = (TextView) v.findViewById(R.id.correct_textview);

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

        // Next button is used as both 'next question' and 'retry quiz'
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex += 1;

                // If the end of the quiz is reached and user hits 'Retry', reset question index pointer,
                // next button text, end of quiz flag, and get the first question.
                if(eoq_flag)
                {
                    mCurrentIndex = 0;
                    mNextButton.setText(R.string.next_button);
                    eoq_flag = false;
                    updateQuestion();
                }
                else if(mCurrentIndex >= mQuestionBank.length)      // If all questions answered,
                {
                    eoq_flag = true;                                // set the end of quiz flag,
                    mTrueButton.setEnabled(false);                  // disable true/false buttons,
                    mFalseButton.setEnabled(false);                 // let the user know the quiz is finished,
                    mNextButton.setText(R.string.retry_button);     // set next button text to 'Retry',
                    mQuestionTextView.setText(R.string.end_of_quiz);
                    gradeQuiz();                                    // and grade the quiz.
                }
                else
                {
                    updateQuestion();                               // Else, get the next question.
                }
            }
        });

        return v;
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
        mNextButton.setVisibility(View.VISIBLE);                      // Show the next button
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

        // Remove the next button, re-enable the true/false buttons and remove the Correct/Incorrect text display.
        mNextButton.setVisibility(View.INVISIBLE);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mCorrectTextView.setText("");
    }

    /*
        Initialize the mQuestionBank with new questions.
    */
    private void initializeQuestions()
    {
        mQuestionBank = new Question[] {
                new Question(R.string.question_abstraction, false),
                new Question(R.string.question_abstractUML, true),
                new Question(R.string.question_abstractMethod, false),
                new Question(R.string.question_abstractSubclass, true),
                new Question(R.string.question_abstractSubclassConcrete, false),

                new Question(R.string.question_interfaces, true),
                new Question(R.string.question_interfaceConstructor, true),
                new Question(R.string.question_interfaceImplement, true),
                new Question(R.string.question_interfaceDescription, true),
                new Question(R.string.question_interfaceFields, false)
        };
    }
}
