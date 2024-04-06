package dal.cs.quickcash3.recycler;

import android.view.View;

import androidx.annotation.NonNull;

public interface OnItemClickListener {
    void onItemClick(@NonNull View view,int position);

    void onLongItemClick(@NonNull View view,int position);

}
