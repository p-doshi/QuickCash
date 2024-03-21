package dal.cs.quickcash3.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SignInImplementation implements SignInInterface {
    private final Context mContext;

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
