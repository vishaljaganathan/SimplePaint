package com.vishcorp.paint;

import android.graphics.Path;

public class stroke {

    public int color;


    public int strokeWidth;


    public Path path;

    public stroke(int color, int strokeWidth,Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
