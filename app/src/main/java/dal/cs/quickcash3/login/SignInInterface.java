package dal.cs.quickcash3.login;

import androidx.annotation.NonNull;

public interface SignInInterface {
    void moveToDashboard();
    void setStatusMessage(@NonNull String message);
}
