package com.usm.jyd.usemista.events;

import android.view.View;

/**
 * Created by der_w on 10/10/2015.
 */
public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
