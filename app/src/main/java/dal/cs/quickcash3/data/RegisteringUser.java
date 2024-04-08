package dal.cs.quickcash3.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisteringUser extends User implements Parcelable {
    private String password;
    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        assert getFirstName() != null;
        dest.writeString(getFirstName());
        assert getLastName() != null;
        dest.writeString(getLastName());
        assert getEmail() != null;
        dest.writeString(getEmail());
        assert getBirthDate() != null;
        dest.writeString(getBirthDate().toString());
        assert getAddress() != null;
        dest.writeString(getAddress());
        assert getPassword() != null;
        dest.writeString(getPassword());
    }
}
