package dal.cs.quickcash3.recycler;

import android.content.Context;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private final OnItemClickListener mListener;
    GestureDetector mGestureDetector;



    public RecyclerItemClickListener(@NonNull Context context, @NonNull final RecyclerView recyclerView,@NonNull OnItemClickListener listener) {
        this.mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
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
        });
    }


    @Override public boolean onInterceptTouchEvent(@NonNull RecyclerView view,@NonNull MotionEvent motionEvent) {
        View childView = view.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && this.mListener != null && mGestureDetector.onTouchEvent(motionEvent)) {
            this.mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    /**
     * This method is called when a touch event is dispatched to a view.
     * Implement this method to handle touch events.Currently not used
     *
     * @param view The RecyclerView that received the touch event.
     * @param motionEvent The MotionEvent object containing full information about the event.
     */
    @Override public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) { //Not used
         }

    /**
     * Called when a child of RecyclerView does not want RecyclerView and its ancestors to intercept touch events.
     * Currently not used
     * @param disallowIntercept True if RecyclerView should disallow intercepting touch events.
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){//Not used
         }
}
