package com.example.csci3130_group_3;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SignInImplementation implements SignInInterface{
    private Context mContext;

    public SignInImplementation(Context context){
        mContext = context;
    }
    @Override
    public void moveToDashboard() {
        Toast.makeText(mContext, "Moving to dashboard...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void setStatusMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
