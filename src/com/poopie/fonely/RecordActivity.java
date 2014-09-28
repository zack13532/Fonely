package com.poopie.fonely;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
//import com.microsoft.windowsazure.mobileservices.*;

public class RecordActivity extends ActionBarActivity {
	
	private TextView dbgTV; // For displaying debugging text
	
	//constants
	protected static final int RESULT_SPEECH = 1;
	private static final String LOG_TAG = "AudioRecordTest";
	
	//variables
	private Button playButt; //play recording button 
	private Button sendButt; //send finished recording button
	private Button recButt; //record button
	private EditText speechText; 
	private MediaPlayer player; 
	
	private File tmpAudioPlay;
    private File tmpAudioRec; 
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		// Create folder for storing audio files
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
		
		//initialize buttons and textviews
		playButt=(Button)findViewById(R.id.playButt); 
		sendButt=(Button)findViewById(R.id.sendButt); 
		recButt=(Button)findViewById(R.id.recButt);
		speechText = (EditText) findViewById(R.id.speechToText);
		speechText.setVisibility(View.INVISIBLE);
		sendButt.setVisibility(View.INVISIBLE);
		recButt.setVisibility(View.INVISIBLE);
		dbgTV = (TextView) findViewById(R.id.debugText);
		
		
		//initialize audio file-related stuff
		player=new MediaPlayer(); 
		tmpAudioRec = new File(Environment.getExternalStorageDirectory() + "/sound/audiorec.amr");
		tmpAudioPlay = new File(Environment.getExternalStorageDirectory() + "/sound/incomingclip.amr");

		//SQL CALL FOR AUDIO CLIP BLOB
		//save as the tmpAudioPlay file
		
	}
	
	//when play button is pressed
	public void playButtonPressed(View view)
	{
		
		//start playing clip
		playButt.setVisibility(View.INVISIBLE);
		startPlayback(tmpAudioPlay.getAbsolutePath());
		tmpAudioPlay.delete();
		recButt.setVisibility(View.VISIBLE);
	}
	
	//starts playing data file
	private void startPlayback(String path){
		
		try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
	}
	
	
	public void startSpeechRecognition()
	{
		   // Fire an intent to start the speech recognition activity.
		   Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		   // secret parameters that when added provide audio url in the result
		   intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
		   intent.putExtra("android.speech.extra.GET_AUDIO", true);

		   try
		   {
			   startActivityForResult(intent, RESULT_SPEECH);
		   } catch (ActivityNotFoundException a)
		   {
		       Toast t = Toast.makeText(getApplicationContext(),
		                "Opps! Your device doesn't support Speech to Text",
		                Toast.LENGTH_SHORT);
		       t.show();
		   }
	}

	/*
	  	0 AMR 4.75 13
		1 AMR 5.15 14
		2 AMR 5.9 16
		3 AMR 6.7 18
		4 AMR 7.4 20
		5 AMR 7.95 21
		6 AMR 10.2 27
		7 AMR 12.2 32
	 */
	
	// handle result of speech recognition
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    tmpAudioRec.delete();
	    
	    // the resulting text is in the getExtras:
	    speechText.setText(data.getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS).get(0));
	    // the recording url is in getData:
	    Uri audioUri = data.getData();
	    ContentResolver contentResolver = getContentResolver();
	    
	    try {
			InputStream filestream = contentResolver.openInputStream(audioUri);
			FileOutputStream audioFileOut =
					new FileOutputStream(tmpAudioRec.getAbsolutePath());
			int bytesread = 0;
			byte[] buffer = new byte[1024];
			while((bytesread = filestream.read(buffer)) > 0)
			{
				audioFileOut.write(buffer, 0, bytesread);
			}
			
			filestream.close();
			audioFileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    sendButt.setVisibility(View.VISIBLE);
	    speechText.setVisibility(View.VISIBLE);
	    
	    startPlayback(tmpAudioRec.getAbsolutePath());
	}
		
	//when submit button is pressed
	//convert sound to text 
	public void recordButtonPressed(View view){
		
		 startSpeechRecognition();
	}
	
	public void sendButtonPressed(View view)
	{
		dbgTV.setText("send button pressed");
		
	    //SQL CALL TO SUBMIT AUDIO FILE
		tmpAudioRec.delete();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		tmpAudioRec.delete();
	}

}

