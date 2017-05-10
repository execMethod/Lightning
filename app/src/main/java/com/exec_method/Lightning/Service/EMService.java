package com.exec_method.Lightning.Service;

import android.app.*;
import android.content.*;
import android.os.*;
import com.exec_method.Lightning.*;
import com.exec_method.Lightning.Data.*;
import com.exec_method.Lightning.Engine.*;
import com.exec_method.Lightning.Util.*;
import java.text.*;
import java.util.*;
import android.location.*;

public class EMService extends Service
{
  public static final String ACTION = "EMService";
  public static final String ARG_ROWS = "EMService#ARG_ROWS";
  public static final String ARG_INTERVAL = "EMService#ARG_INTERVAL";
  
  private EMLocationMonitor mntrLocationGps = null;
  private EMLocationMonitor mntrLocationNet = null;
  private EMTelephonyMonitor mntrTelephony = null;
  private Timer timer = null;
  private String fname = "";

  private List<EMRowInf> rowsLocation = null;

  @Override
  public void onCreate()
  {
    EMLog.d();
    try
    {
      EMLocationListen locLis = new EMLocationListen();
      mntrLocationGps = new EMLocationMonitor(getBaseContext(), locLis, LocationManager.GPS_PROVIDER);
      mntrLocationNet = new EMLocationMonitor(getBaseContext(), locLis, LocationManager.NETWORK_PROVIDER);
      mntrTelephony = new EMTelephonyMonitor(getBaseContext(), new EMTelephonyListen());
    }
    catch ( Exception e )
    {
      EMLog.e(e);
    }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) 
  {
    EMLog.d();
    if (timer == null)
    {
      Calendar c = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
      fname = sdf.format(c.getTime());
      
      int interval = intent.getIntExtra(ARG_INTERVAL,1);
      if( 0 >= interval )
      {
        interval = 1;
      }
      
      timer = new Timer();
      timer.schedule(new EMTimerTask(), 0, interval * 1000);
      mntrLocationGps.start();
      mntrLocationNet.start();
    }
    return START_STICKY;
  }

  private class EMTimerTask extends TimerTask
  {
    public void run()
    {
      try
      {
        List<EMRowInf> rows = new ArrayList<EMRowInf>();
        if (null != rowsLocation)
        {
          rows.addAll(rowsLocation);
          rows.addAll(mntrTelephony.getRows());
          
          EMCsv.outRows(fname,rows);
        }
        else
        {
          rows.add(new EMRow(getString(R.string.location_available)));
        }
        
        EMRow row = new EMRow((new Date()).toString());
        row.addRows(rows);
        
        Intent itt = new Intent(ACTION);
        itt.putExtra(ARG_ROWS, row);
        sendBroadcast(itt);
      }
      catch (Exception e)
      {
        EMLog.e(e);
      }
    }
  }

  private class EMLocationListen implements EMLocationListener
  {
    @Override
    public void locationInfo(List<EMRowInf> rowsInfo)
    {
      try
      {
        rowsLocation = rowsInfo;
      }
      catch (Exception e)
      {
        EMLog.e(e);
      }
    }

    @Override
    public void error(Exception e)
    {
      EMLog.e(e);
    }
  }

  private class EMTelephonyListen implements EMTelephonyMonitor.EMListener
  {
  }

  @Override
  public IBinder onBind(Intent intent)
  {
    return null;
  }

  @Override
  public void onDestroy()
  {
    EMLog.d();
    super.onDestroy();
    timer.cancel();
    timer = null;
    mntrLocationGps.stop();
    mntrLocationNet.stop();
  }
}
