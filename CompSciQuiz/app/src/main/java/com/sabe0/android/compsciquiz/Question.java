package com.sabe0.android.compsciquiz;


public class Question
{
    private int mTextResId;
    private boolean mAnswer;
    private boolean mWasUserCorrect;

    public Question(int textResId, boolean answer)
    {
        mTextResId = textResId;
        mAnswer = answer;
    }

    public int getTextResId()
    {
        return mTextResId;
    }

    public boolean getAnswer()
    {
        return mAnswer;
    }

    public void setWasUserCorrect(boolean correct)
    {
        mWasUserCorrect = correct;
    }

    public boolean getWasUserCorrect()
    {
        return mWasUserCorrect;
    }
}
