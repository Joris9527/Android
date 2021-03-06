package com.joris.camer.Onvif.imgLoad;

import android.view.View;
import android.widget.ImageView;

import com.joris.camer.R;

public class ViewCache {

    private View baseView;
    private ImageView imageView;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }


    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.image_url);
        }
        return imageView;
    }

}
