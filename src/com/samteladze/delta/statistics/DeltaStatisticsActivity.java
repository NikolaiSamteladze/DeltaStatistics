package com.samteladze.delta.statistics;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.samteladze.delta.statistics.utils.*;

public class DeltaStatisticsActivity extends Activity 
{
	// Time delay in AlarmManager before the first alarm is fired
	public static final int ALARM_DELAY = 120000;
	// Time between alarms in AlarmManager
	public static final long ALARM_PERIOD = AlarmManager.INTERVAL_HOUR;
	private static Context _context;
	// Unique request code for PendingIntent (used to distinguish intents)
	public static final int INTENT_REQUEST_CODE = 0;
	
    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
        DeltaStatisticsActivity._context = getApplicationContext();
        
        CreateRequiredFoldersAndFile();
        
	    // Get AlarmManager
 		AlarmManager alarmManager = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
 		
 		// Create an Intent to start OnAlarmReceiver
 		Intent alarmIntent = new Intent(_context, OnAlarmReceiver.class);
 		// Create a PendingIntent from alarmIntent
 		PendingIntent alarmTestPendingIntent = PendingIntent.getBroadcast(_context, INTENT_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_NO_CREATE);
 		
 		// If there is no such intent
 		if (alarmTestPendingIntent == null)
 		{
 			PendingIntent alarmPendingIntent = 
 					PendingIntent.getBroadcast(_context, INTENT_REQUEST_CODE, alarmIntent, 0);
 			// Set repeating alarm that will invoke OnAlarmReceiver
 	 		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
 	 								  SystemClock.elapsedRealtime() + ALARM_DELAY, 
 	 								  ALARM_PERIOD, alarmPendingIntent);   
 	 		
 	 		LogManager.Log(DeltaStatisticsActivity.class.getSimpleName(), "Repeating alarm was set");
 		} 
 		else
 		{
 			LogManager.Log(DeltaStatisticsActivity.class.getSimpleName(), "Alarm alredy exists. New alarm was not set");
 		}
    }    
    
    @SuppressWarnings("unused")
	private void SendAppStatisticsToMail(Intent sendIntent)
    {
    	try 
    	{
    		startActivity(Intent.createChooser(sendIntent, "Send e-mail..."));
        } 
    	catch (Exception e) 
    	{
    		e.printStackTrace(System.err);
    		
    		LogManager.Log(CommunicationManager.class.getSimpleName(), e.toString());
    		
            Toast.makeText(_context, "No e-mail clients installed", Toast.LENGTH_LONG).show();
    	}
    }
    
    private void CreateRequiredFoldersAndFile()
    {
        if (!FileManager.HasFolderStructure())
        {
        	FileManager.CreateFolderStructure();
        }
        
        if (!LogManager.LogExists())
        {
        	LogManager.CreateLog();
        }
    }
    
    public void OnClickCollect(View view)
    {
    	WakefulIntentService.sendWakefulWork(getApplicationContext(), UserAppStatisticsService.class);
		
		LogManager.Log(DeltaStatisticsActivity.class.getSimpleName(), "Collecting statistics");
    }
}