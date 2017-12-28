package com.googlemapclustering.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.googlemapclustering.R;


/**
 * Created by Ocs pl-79(17.2.2016) on 12/28/2017.
 */

public class Utils {
    /*Common progress bar declaration*/
    public static Dialog dialogProgressBarDeclaration(Activity mActivity) {

        Dialog customProgressBar = new Dialog(mActivity);
        customProgressBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customProgressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressBar.setContentView(R.layout.custom_progress_bar);
        customProgressBar.setCancelable(false);
        return customProgressBar;
    }
}
