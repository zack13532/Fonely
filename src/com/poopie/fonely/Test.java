package com.poopie.fonely;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaRecorder;

public class Test extends ActionBarActivity {
	protected static final int RESULT_SPEECH = 1;
	 
    private ImageButton btnSpeak;
    private TextView txtText;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        txtText = (TextView) findViewById(R.id.txtText);
 
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
 
        
        /* When the user pushes the "record button", two things need to happen:
         * 1.) The Speech-to-Text operation runs in a new Activity.
         * 2.) The system must record the audio file to be operated on later. 
         * 
         * TODO: Make sure the recording happens while the new Activity is visible. 
         */
        
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
            	try {
            	MediaRecorder recorder = makeRecorder();
            	recorder.start();
            	parseVoice();
            	recorder.stop();
            	recorder.release();
            	
            	} catch (Exception e){
            		//TODO: Catch block
            	}
          }
        });
 
    }
 
    
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SPEECH: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
 
                txtText.setText(text.get(0));
            }
            break;
        }
 
        }
    }
    
    public void parseVoice(){
    	Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
            txtText.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }

    }
    
    public MediaRecorder makeRecorder() throws IllegalStateException, IOException{
    	MediaRecorder recorder = new MediaRecorder();
    	recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
    	recorder.setOutputFile("temp_audio");
    	recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    	
    	recorder.prepare();
    	
    	return recorder;
    	
    	
    	
    	
    	
    }
    
    
}