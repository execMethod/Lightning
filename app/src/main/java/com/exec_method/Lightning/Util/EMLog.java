package com.exec_method.Lightning.Util;

import android.os.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;

public final class EMLog
{
  private static final String TAG = "Lightning";
  private static final String PATH_LOG = "/exec_method/Lightning.log";

  private static final String crlf = System.getProperty("line.separator");
  
  public static void d()
  {
    Log.d(TAG,"");
    fileout(formatMessage());
  }
  
  public static void i(String msg)
  {
    Log.i(TAG, msg);
    fileout(formatMessage(msg));
  }

  public static void e(Throwable t)
  {
    Log.e(TAG, "", t);
    fileout(formatMessage(t.getMessage()));
    printStackTrace(t);
  }

  private static void printStackTrace(Throwable th)
  {
    String filePath = Environment.getExternalStorageDirectory() + PATH_LOG;
    File file = new File(filePath);
    if (!file.getParentFile().exists())
    {
      file.getParentFile().mkdir();
    }

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file, true);
      OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
      PrintWriter pw = new PrintWriter(osw);
      th.printStackTrace(pw);
      pw.flush();
      pw.close();
    }
    catch (Exception e)
    {

    }
    finally
    {
      if( null != fos )
      {
        try
        {
          fos.close();
        }
        catch( Exception ex )
        {
          
        }
      }
    }
  }
  
  private static String formatMessage()
  {
    int rank = 4;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss.SSS");
    sdf.format(c.getTime());

    Thread th = Thread.currentThread();
    StackTraceElement st[] = th.getStackTrace();
    StringBuffer sb = new StringBuffer();

    if (st.length < rank-1)
    {
      return "";
    }

    sb.append(sdf.format(c.getTime()));
    sb.append("#");
    sb.append(st[rank].getFileName());
    sb.append("[");
    sb.append(st[rank].getLineNumber());
    sb.append("] @");
    sb.append(st[rank].getMethodName());
    sb.append(crlf);

    return sb.toString();
  }
  
  private static String formatMessage(String msg)
  {
    int rank = 4;
    
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss.SSS");
    sdf.format(c.getTime());

    Thread th = Thread.currentThread();
    StackTraceElement st[] = th.getStackTrace();
    StringBuffer sb = new StringBuffer();

    if (st.length < rank-1)
    {
      return "";
    }

    sb.append(sdf.format(c.getTime()));
    sb.append("#");
    sb.append(st[rank].getFileName());
    sb.append("[");
    sb.append(st[rank].getLineNumber());
    sb.append("]:");
    sb.append(msg);
    sb.append(crlf);

    return sb.toString();
  }

  private static void fileout(String msg)
  {
    String filePath = Environment.getExternalStorageDirectory() + PATH_LOG;
    File file = new File(filePath);
    if (!file.getParentFile().exists())
    {
      file.getParentFile().mkdir();
    }

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file, true);
      OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
      BufferedWriter bw = new BufferedWriter(osw);
      bw.write(msg);
      bw.flush();
      bw.close();
    }
    catch (Exception e)
    {

    }
    finally
    {
      if( null != fos )
      {
        try
        {
          fos.close();
        }
        catch( Exception ex )
        {
          
        }
      }
    }
  }
  
  public static void clear()
  {
    String filePath = Environment.getExternalStorageDirectory() + PATH_LOG;
    File file = new File(filePath);
    if (!file.getParentFile().exists())
    {
      file.getParentFile().mkdir();
    }

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file, false);
      OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
      BufferedWriter bw = new BufferedWriter(osw);
      bw.write("");
      bw.flush();
      bw.close();
    }
    catch (Exception e)
    {

    }
    finally
    {
      if( null != fos )
      {
        try
        {
          fos.close();
        }
        catch( Exception ex )
        {

        }
      }
    }
  }
}

