package com.example.pm1p2_tareavideo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView videoView;
    Button btnVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);
        btnVideo = findViewById(R.id.btnGuardar);

    }

    public void grabarVideo(View v)
    {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }

    private String ArchivoMP4()
    {
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";

        return nombre;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

            try
            {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream inputStream = videoAsset.createInputStream();
                FileOutputStream archivo = openFileOutput(ArchivoMP4(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];

                int len;
                while ((len = inputStream.read(buf)) > 0){
                    archivo.write(buf, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                Toast.makeText(this, "No se ha podido grabar el video", Toast.LENGTH_SHORT).show();
            }
        }
    }






}