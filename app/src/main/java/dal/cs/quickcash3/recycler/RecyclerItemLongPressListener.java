package dal.cs.quickcash3.recycler;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemLongPressListener extends GestureDetector.SimpleOnGestureListener  {
    private final RecyclerView recyclerView;
    private final OnItemClickListener mListener;

    public RecyclerItemLongPressListener(@NonNull RecyclerView recyclerView, @NonNull OnItemClickListener mListener){
        this.recyclerView=recyclerView;
        this.mListener=mListener;
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null) {
            mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
        }
    }

}
