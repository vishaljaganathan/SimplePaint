package com.vishcorp.paint;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;

import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;


public class MainActivity extends AppCompatActivity {
   private  DrawView paint;

    // creating objects of type button
    private ImageView save, color, stroke, undo;

    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private RangeSlider rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                paint = (DrawView) findViewById(R.id.draw_view);
                rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
                undo = (ImageView) findViewById(R.id.btn_undo);
                save = (ImageView) findViewById(R.id.btn_save);
                color = (ImageView) findViewById(R.id.btn_color);
                stroke = (ImageView) findViewById(R.id.btn_stroke);


                undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        paint.undo();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Bitmap bmp = paint.save();

                        OutputStream imageOutStream = null;

                        ContentValues cv = new ContentValues();

                        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

                        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                        try {
                            imageOutStream = getContentResolver().openOutputStream(uri);

                            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);

                            imageOutStream.close();
                            Toast.makeText(getApplicationContext(),"saved Successfully",Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }

                });
        //save.setOnClickListener(v-> Toast.makeText(this,"saved Successfully", Toast.LENGTH_SHORT).show());

                color.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                                    @Override
                                    public void setOnFastChooseColorListener(int position, int color) {

                                        paint.setColor(color);
                                    }

                                    @Override
                                    public void onCancel() {
                                        colorPicker.dismissDialog();
                                    }
                                })
                                .setColumns(5)
                                .setDefaultColorButton(Color.parseColor("#000000"))
                                .show();

                  }
                });


                stroke.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rangeSlider.getVisibility() == View.VISIBLE)
                            rangeSlider.setVisibility(View.GONE);
                        else
                            rangeSlider.setVisibility(View.VISIBLE);
                    }
                });


                rangeSlider.setValueFrom(0.0f);
                rangeSlider.setValueTo(100.0f);
                rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                        paint.setStrokeWidth((int) value);
                    }
                });

                ViewTreeObserver vto = paint.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = paint.getMeasuredWidth();
                        int height = paint.getMeasuredHeight();
                        paint.init(height, width);
                    }
                });
            }
        }