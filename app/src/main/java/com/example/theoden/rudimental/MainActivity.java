package com.example.theoden.rudimental;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MetronomeUI.OnFragmentInteractionListener {

    private Button toggleMetronome, record;
    private SeekBar tempoControl;
    private TextView tempoText;
    private MediaPlayer mp;
    private MediaRecorder mMediaRecorder;
    private Metronome metronome;
    private ViewPager viewPager;
    private ViewPageManager viewPageManager;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPageManager = new ViewPageManager(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(viewPageManager);

//        initializeMetronome();
//        initializeSlider();
    }

    private void initializeSlider() {
        tempoControl = findViewById(R.id.tempoControl);
        tempoControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newTempo = progress + 50;
                metronome.setTempo(newTempo);
                tempoText.setText(Integer.toString(newTempo));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void toggleRecord(View view) {
        // Requests permission if not active, see below
        // https://stackoverflow.com/questions/37290752/java-lang-runtimeexception-setaudiosource-failed
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    10);
        } else if (!isRecording) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
            File newFile = new File(getExternalFilesDir(null), "demoFile.mp3");
            mMediaRecorder.setOutputFile(newFile);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                isRecording = !isRecording;
                record.setText("STOP");
                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "IO Exception", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            isRecording = !isRecording;
            record.setText("Record");
        }
    }

    public void onFragmentInteraction(Uri uri) {

    }

}
