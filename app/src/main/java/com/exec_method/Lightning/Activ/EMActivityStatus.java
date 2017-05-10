package com.exec_method.Lightning.Activ;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.exec_method.Lightning.*;
import com.exec_method.Lightning.Data.*;
import com.exec_method.Lightning.Service.*;
import com.exec_method.Lightning.Util.*;
import java.util.*;
import android.widget.SeekBar.*;

public class EMActivityStatus extends EMActivity
{
  private List<EMRowInf> rows = new ArrayList<EMRowInf>();
  private BroadcastReceiver receiver = new EMReceiver();
  private IntentFilter intentFilter = new IntentFilter();

  private int interval = 5;
  private int intervalTemp = 5;
  
  // AIzaSyBvAb9_frIePt6tIp33nylBymnLnhmtxeo

  protected int getLayoutId()
  {
    return R.layout.status;
  }

  protected int getRowLayoutId()
  {
    return R.layout.row;
  }

  public void onCreateSafe()
  {
    contentView();

    CheckBox cbx = (CheckBox)findViewById(R.id.status_rec);
    cbx.setOnCheckedChangeListener(new EMOnChechedChange());
    if (isServiceRunning(EMService.class))
    {
      cbx.setChecked(true);
    }

    Button btn;
    EMOnClickListener clisten = new EMOnClickListener();
    btn = (Button)findViewById(R.id.status_map);
    btn.setOnClickListener(clisten);
    btn = (Button)findViewById(R.id.status_cfg);
    btn.setOnClickListener(clisten);
    
    EMOnConfigClickListener clistenCfg = new EMOnConfigClickListener();
    btn = (Button)findViewById(R.id.config_ok);
    btn.setOnClickListener(clistenCfg);
    btn = (Button)findViewById(R.id.config_ng);
    btn.setOnClickListener(clistenCfg);

    EMOnSeekBarChangeListener clistenSb = new EMOnSeekBarChangeListener();
    SeekBar sb = (SeekBar)findViewById(R.id.config_interval);
    sb.setOnSeekBarChangeListener(clistenSb);
    sb.setProgress(interval);
    
    //setIntervalTemp(interval);
    
    intentFilter.addAction(EMService.ACTION);
    registerReceiver(receiver, intentFilter);
  }

  @Override
  public void onResume()
  {
    super.onResume();
    registerReceiver(receiver, intentFilter);
  }

  @Override
  public void onPause()
  {
    super.onPause();
    unregisterReceiver(receiver);
  }

  @Override
  protected List<EMRowInf> getRows()
  {
    return rows;
  }

  protected void rowClick(EMRowInf row)
  {
  }

  private class EMOnClickListener implements OnClickListener
  {
    @Override
    public void onClick(View view)
    {
      try
      {
        if (R.id.status_cfg == view.getId())
        {
          View vConfig = findViewById(R.id.status_config);
          vConfig.setVisibility(View.VISIBLE);
        }
        //Intent intent = new Intent(getBaseContext(), EMActivityMap.class);
        //intent.putExtra(INTENT_ARG_ACT, master);
        //startActivityForResult(intent, 0);
      }
      catch (Exception e)
      {
        EMLog.e(e);
        report(e.getMessage());
      }
    }
  }

  private class EMOnSeekBarChangeListener implements OnSeekBarChangeListener
  {
    @Override
    public void onStartTrackingTouch(SeekBar sb)
    {
      
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb)
    {
      
    }

    @Override
    public void onProgressChanged(SeekBar sb, int progress, boolean manual)
    {
      setIntervalTemp(progress);
    }
  }
  
  private class EMOnConfigClickListener implements OnClickListener
  {
    @Override
    public void onClick(View view)
    {
      try
      {
        if (R.id.config_ok == view.getId())
        {
          interval = intervalTemp;
        }
        else if (R.id.config_ng == view.getId())
        {
          SeekBar sb = (SeekBar)findViewById(R.id.config_interval);
          sb.setProgress(interval);
        }
        View vConfig = findViewById(R.id.status_config);
        vConfig.setVisibility(View.GONE);
      }
      catch (Exception e)
      {
        EMLog.e(e);
        report(e.getMessage());
      }
    }
  }
  
  private class EMOnChechedChange implements CheckBox.OnCheckedChangeListener
  {
    @Override
    public void onCheckedChanged(CompoundButton btn, boolean chk)
    {
      try
      {
        if (chk)
        {
          if (!isServiceRunning(EMService.class))
          {
            report("scan ON");
            Intent intent = new Intent(getBaseContext(), EMService.class);
            intent.putExtra(EMService.ARG_INTERVAL,interval);
            startService(intent);
          }
          Button btnCfg;
          btnCfg = (Button)findViewById(R.id.status_cfg);
          btnCfg.setClickable(false);
          btnCfg.setVisibility(View.INVISIBLE);
        }
        else
        {
          report("scan OFF");
          stopService(new Intent(getBaseContext(), EMService.class));
          Button btnCfg;
          btnCfg = (Button)findViewById(R.id.status_cfg);
          btnCfg.setClickable(true);
          btnCfg.setVisibility(View.VISIBLE);
        }
      }
      catch (Exception e)
      {
        EMLog.e(e);
        report(e.getMessage());
      }
    }
  }

  private class EMReceiver extends BroadcastReceiver
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      EMRow row = (EMRow)intent.getSerializableExtra(EMService.ARG_ROWS);
      rows = row.getRows();
      reload();

      // レシーバーを受信したActivityを利用する場合
      //MainActivity activity = (MainActivity) context;
    }
  }

  private boolean isServiceRunning(Class cls)
  {
    ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
    List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
    boolean found = false; 
    for (ActivityManager.RunningServiceInfo curr : listServiceInfo)
    {
      if (curr.service.getClassName().equals(cls.getName()))
      {
        found = true;
        break;
      }
    }
    return found;
  }
  
  private void setIntervalTemp(int interval)
  {
    if( 0 == interval )
    {
      interval = 1;
    }
    
    this.intervalTemp = interval;
    
    TextView tv = (TextView)findViewById(R.id.config_interval_);
    String fmt = getString(R.string.config_interval);
    tv.setText(String.format(fmt,interval));
  }
}
