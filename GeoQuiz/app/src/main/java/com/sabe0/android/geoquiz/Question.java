package com.sabe0.android.geoquiz;


public class Question
{
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mWasAnswered;
    private boolean mUserAnswer;

    public Question(int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mWasAnswered = false;
    }

    public boolean getUserAnswer()
    {
        return mUserAnswer;
    }

    public void setUserAnswer(boolean userAnswer)
    {
        mUserAnswer = userAnswer;
    }

    public boolean getWasAnswered()
    {
        return mWasAnswered;
    }

    public void setWasAnswered(boolean wasAnswered)
    {
        mWasAnswered = wasAnswered;
    }

    public int getTextResId()
    {
        return mTextResId;
    }

    public void setTextResId(int textResId)
    {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue()
    {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue)
    {
        mAnswerTrue = answerTrue;
    }
}
