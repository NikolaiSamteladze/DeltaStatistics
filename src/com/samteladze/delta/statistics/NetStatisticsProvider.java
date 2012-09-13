package com.samteladze.delta.statistics;

import android.content.Context;
import android.net.ConnectivityManager;

import com.samteladze.delta.statistics.DataModel.NetStatistics;
import com.samteladze.delta.statistics.DataModel.StatisticsFormat;
import com.samteladze.delta.statistics.Utils.FileManager;

public class NetStatisticsProvider
{
	private Context _context;
	private NetStatistics _statistics;
	
	public NetStatisticsProvider(Context context)
	{
		_context = context;
		_statistics = new NetStatistics();
	}
	
	public void CollectStatistics()
	{
		ConnectivityManager mConnectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE );
		
	    _statistics.activeNetInfo = mConnectivity.getActiveNetworkInfo();
	    _statistics.wifiNetInfo = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    _statistics.mobileNetInfo = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    
	    FileManager.Log(NetStatisticsProvider.class.getSimpleName(), "Statistics was collected.");
	}
	
	public String GetStatistics(StatisticsFormat format)
	{
		String statisticsStr = "";
		
		statisticsStr += _statistics.Format(format);
		
		return statisticsStr;
	}
}