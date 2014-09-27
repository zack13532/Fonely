package com.poopie.fonely;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private static final String LOG_TAG = "AudioRecordTest";
	private Button recButt; //record button
	private Button playRecButt; //play recording button 
	private boolean recButtPressed; //whether or not Rec button has been pressed 
	private boolean playButtPressed; 
	private MediaRecorder recorder;
	private MediaPlayer player; 
	private String mFile; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		recButt=(Button)findViewById(R.id.record_butt);
		playRecButt=(Button)findViewById(R.id.playback_butt); 
		recButtPressed=false; 
		playButtPressed=false; 
		recorder=new MediaRecorder(); 
		player=new MediaPlayer(); 
		
	}
	
	//when record button is pressed 
	public void recordButtonPressed(View view)
	{
		//if pressed first time, start recording
		if(!recButtPressed){
			recButtPressed=true; 
			recButt.setText("Stop"); 
			
			startRecording(); 
		}
		
		//else if button has already been pressed, end recording 
		else{
		
			recButtPressed=false; 
			recButt.setText("Record"); 
			stopRecording(); 
		}
		
	}
	
	//starts recording
	private void startRecording(){
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    recorder.setOutputFile(mFile);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	    recorder.setOutputFile("/sdcard/sound/" + mFile);

	    try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            e.printStackTrace();
        }

        recorder.start();
	}
	
	//stops recording
	private void stopRecording(){
		
		recorder.stop();
	    recorder.release();
	    recorder = null;
	}
	
	//when playBack button is pressed
	public void playbackButtonPressed(View view)
	{
		
		//start playing clip
		if(!playButtPressed){
			
			playButtPressed=true; 
			startPlayback(); 
		}
		
		//stop playing clip. 
		else{
			
			playButtPressed=false; 
			stopPlayback(); 
		}
	}
	
	//starts playing data file
	private void startPlayback(){
		
		try {
            player.setDataSource(mFile);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
	}
	
	//stops playing data file
	private void stopPlayback(){
		
		 player.release();
	     player = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
