package com.exec_method.Lightning.Activ;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import com.exec_method.Lightning.*;
import com.exec_method.Lightning.Util.*;

public class EMActivityConfig extends Activity
{
  public void report(String msg)
  {
    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
    super.onActivityResult(requestCode, resultCode, intent);

    if( 0 < resultCode - 1)
    {
      finish( resultCode - 1, getIntent() );
    }
    return;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    try
    {
      setContentView(R.layout.config);
    }
    catch ( Exception e )
    {
      EMLog.e(e);
      report(e.getMessage());
    }
  }

  public void finish(int resultCode, Intent intent)
  {
    setResult(resultCode, intent);
    super.finish();
  }
  
}
