package dal.cs.quickcash3.data;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class CompletedJob extends Job {
    private String employeeId;
    private String completionDate;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employee) {
        this.employeeId = employee;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }
}
