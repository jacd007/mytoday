package com.zippyttech.mytoday;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by zippyttech on 28/03/18.
 */

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick, AppCompatActivity ap);
}
