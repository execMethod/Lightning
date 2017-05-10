package com.exec_method.Lightning.Engine;
import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.exec_method.Lightning.*;
import com.exec_method.Lightning.Util.*;
import java.text.*;
import java.util.*;
import com.exec_method.Lightning.Data.*;

public class EMLocationMonitor implements LocationListener
{
  private static final int LOCATION_UPDATE_MIN_TIME = 1;
  private static final int LOCATION_UPDATE_MIN_DISTANCE = 1;

  private LocationManager mLocationManager;

  private String msgOutofservive;
  private String msgUnavailable;
  private String msgAvailable;
  private String msgAble;
  private String msgUnable;
  private String msgUnService;

  private String valLongitude;
  private String valLatitude;
  private String valGpstime;
  private String valAccuracy;
  private String valBearing;
  private String valProvider;
  private String valSpeed;

  private EMLocationListener listener;

  private String provider = null;

  public EMLocationMonitor(Context context, EMLocationListener listener, String provider)
  {
    msgOutofservive = context.getString(R.string.location_outofservice);
    msgUnavailable = context.getString(R.string.location_unavailable);
    msgAvailable = context.getString(R.string.location_available);
    msgAble = context.getString(R.string.location_able);
    msgUnable = context.getString(R.string.location_unable);
    msgUnService = context.getString(R.string.location_unservice);

    valLongitude = context.getString(R.string.location_longitude);
    valLatitude = context.getString(R.string.location_latitude);
    valGpstime = context.getString(R.string.location_gpstime);
    valAccuracy = context.getString(R.string.location_accuracy);
    valBearing = context.getString(R.string.location_bearing);
    valProvider = context.getString(R.string.location_provider);
    valSpeed = context.getString(R.string.location_speed);

    this.listener = listener;
    this.provider = provider;
    mLocationManager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);

  }

  public void start()
  {
    try
    {
      requestLocationUpdates();
    }
    catch (Exception e)
    {
      listener.error(e);
    }
  }

  public void stop()
  {
    try
    {
      mLocationManager.removeUpdates(this);
    }
    catch (Exception e)
    {
      listener.error(e);
    }
  }

  @Override
  public void onLocationChanged(Location location)
  {
    EMLog.d();
    showLocation(location);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras)
  {
    EMLog.d();
    try
    {
      switch (status)
      {
        case LocationProvider.OUT_OF_SERVICE:
          showMessage(msgOutofservive);
          break;
        case LocationProvider.TEMPORARILY_UNAVAILABLE:
          showMessage(msgUnavailable);
          break;
        case LocationProvider.AVAILABLE:
          showMessage(msgAvailable);
          requestLocationUpdates();
          break;
      }
    }
    catch (Exception e)
    {
      listener.error(e);
    }
  }

  @Override
  public void onProviderEnabled(String provider)
  {
    EMLog.d();
    try
    {
      String message = provider + msgUnable;
      showMessage(message);
      requestLocationUpdates();
    }
    catch (Exception e)
    {
      listener.error(e);
    }
  }

  @Override
  public void onProviderDisabled(String provider)
  {
    EMLog.d();
    try
    {
      String message = provider + msgUnable;
      showMessage(message);
    }
    catch (Exception e)
    {
      listener.error(e);
    }
  }

  private void requestLocationUpdates()
  {
    EMLog.d();
    //String provider = null;

    //if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    //{
    //  provider = LocationManager.GPS_PROVIDER;
    //}
    //else 
    //if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    //{
    //  provider = LocationManager.NETWORK_PROVIDER;
    //}

    if ((LocationManager.GPS_PROVIDER == provider)
        || (LocationManager.NETWORK_PROVIDER == provider)
        )
    {
      mLocationManager.requestLocationUpdates
      (
        provider,
        LOCATION_UPDATE_MIN_TIME,
        LOCATION_UPDATE_MIN_DISTANCE,
        this
      );
      Location location = mLocationManager.getLastKnownLocation(provider);
      if (location != null)
      {
        showLocation(location);
      }
    }
    else
    {
      showMessage(msgUnService);
    }
  }

  private void showLocation(Location location)
  {
    long time = location.getTime();
    Date date = new Date(time);
    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String gpstime = formatter.format(date);

    List<EMRowInf> rows = new ArrayList<EMRowInf>();
    rows.add(new EMRow(valGpstime, gpstime));
    rows.add(new EMRow(valLongitude, "%.8f", location.getLongitude()));
    rows.add(new EMRow(valLatitude, "%.8f", location.getLatitude()));
    rows.add(new EMRow(valAccuracy, "%.4f", (double)location.getAccuracy()));
    rows.add(new EMRow(valBearing, "%.4f", (double)location.getBearing()));
    rows.add(new EMRow(valSpeed, "%.4f", (double)location.getSpeed()));
    rows.add(new EMRow(valProvider, location.getProvider()));

    EMLog.i(valGpstime + " = " + gpstime);
    EMLog.i(valLongitude + " = " + location.getLongitude());
    EMLog.i(valLatitude + " = " + location.getLatitude());
    EMLog.i(valAccuracy + " = " + location.getAccuracy());
    EMLog.i(valBearing + " = " + location.getBearing());
    EMLog.i(valSpeed + " = " + location.getSpeed());
    EMLog.i(valProvider + " = " + location.getProvider());

    listener.locationInfo(rows);
  }

  private void showMessage(String message)
  {
    List<EMRowInf> rows = new ArrayList<EMRowInf>();
    rows.add(new EMRow(message));
    listener.locationInfo(rows);
  }
}
