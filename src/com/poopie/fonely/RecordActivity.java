package com.poopie.fonely;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
//import com.microsoft.windowsazure.mobileservices.*;

public class RecordActivity extends ActionBarActivity {
	
	private MobileServiceClient mClient;
	
	//constants
	protected static final int RESULT_SPEECH = 1;
	private static final String LOG_TAG = "AudioRecordTest";
	private static int fileNum = 1;
	
	//variables
	private Button recButt; //record button
	private Button playRecButt; //play recording button 
	private Button sendButt; 
	private boolean recButtPressed; //whether or not Rec button has been pressed 
	private boolean playButtPressed; 
	private MediaRecorder recorder;
	private MediaPlayer player; 
	private String audioFile; 
	//private TextView text; 
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	//TODO: sendButt.setActivated(false) fucks out for some reason, check above line later... need
	// to talk about compatibility issues
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		File folder = new File(Environment.getExternalStorageDirectory() + "/sound");
		boolean success = true;
		if (!folder.exists()) {
		    success = folder.mkdir();
		}
		if (success) {
            Log.e(LOG_TAG, "Folder created");
		} else {
            Log.e(LOG_TAG, "Folder creation failed"); 
		}
		
		//initialize buttons
		recButt=(Button)findViewById(R.id.recButt);
		playRecButt=(Button)findViewById(R.id.playbackButt); 
		sendButt=(Button)findViewById(R.id.sendButt); 
		playRecButt.setVisibility(View.INVISIBLE); 
		sendButt.setActivated(false); 
		
		//initialize variables
		recButtPressed=false; 
		playButtPressed=false; 
		
		//initialize audio file-related stuff
		player=new MediaPlayer(); 
		recorder=new MediaRecorder(); 
		audioFile = "fonelyClip";

		try {
			mClient = new MobileServiceClient(
				      "https://fonely.azure-mobile.net/",
				      "MPcDeQZZkvxBYoJJYUtodwJYfjHUoA36",
				      this
				);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//<<<<<<< Updated upstream
		
		
		//text = (TextView) findViewById(R.id.text);
		/* #TODO mobile service client
		try {
			mClient = new MobileServiceClient(
				      "https://fonely.azure-mobile.net/",
				      "MPcDeQZZkvxBYoJJYUtodwJYfjHUoA36",
				      this
				);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
//=======
		 	
//>>>>>>> Stashed changes
	}
	
	//when record button is pressed 
	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB) public void recordButtonPressed(View view)
	{
		//if pressed first time, start recording
		if(!recButtPressed){
			recButtPressed=true; 
			recButt.setText("Stop"); 
			playRecButt.setVisibility(View.INVISIBLE); 
			sendButt.setActivated(false); 
			startRecording(); 
			//TODO implement the animation/audio squiggle
		}
		
		//else if button has already been pressed, end recording 
		else{
		
			recButtPressed=false; 
			recButt.setText("Rerecord"); 
			playRecButt.setVisibility(View.VISIBLE); 
			sendButt.setActivated(true); 
			stopRecording(); 
			//TODO implement speechToText
			
		}
		
	}
	
	//starts recording
	private void startRecording(){

		fileNum++;
		
		recorder=new MediaRecorder(); 
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/sound/" + audioFile + fileNum);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

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
            player.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/sound/" + audioFile + fileNum);
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
/*	
	//when submit button is pressed
	//#TODO Actually send the TextView file 
	//convert sound to text 
	public void submitButtonPressed(View view){
		
<<<<<<< Updated upstream
		 Intent intent = new Intent(
                 RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

         intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

         try {
             startActivityForResult(intent, RESULT_SPEECH);
             //text.setText("");
         } catch (ActivityNotFoundException a) {
             Toast t = Toast.makeText(getApplicationContext(),
                     "Opps! Your device doesn't support Speech to Text",
                     Toast.LENGTH_SHORT);
             t.show();
         }
=======
>>>>>>> Stashed changes
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
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
/*
public class Item {
    public String Id;
    public String Text;
}
Copy to clipboard
In the same activity where you defined mClient, add the following code:

Item item = new Item();
item.Text = "Awesome item";
mClient.getTable(Item.class).insert(item, new TableOperationCallback<Item>() {
    public void onCompleted(Item entity, Exception exception, ServiceFilterResponse response) {
          if (exception == null) {
                // Insert succeeded
          } else {
                // Insert failed
          }
    }
});
*/
