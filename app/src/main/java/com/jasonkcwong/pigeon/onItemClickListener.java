package com.jasonkcwong.pigeon;

import android.view.View;

/**
 * Created by jason on 16-08-01.
 */
public interface OnItemClickListener {
    void onCardClick(View view, int position);
    boolean onCardLongClick(View view, int position);
}
