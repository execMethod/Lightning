package com.exec_method.Lightning.Util;

import android.os.*;
import android.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import com.exec_method.Lightning.Data.*;

public final class EMCsv
{
  private static final String PATH_DIR = "/exec_method/TeleLog_%s.csv";
  
  private static final String crlf = System.getProperty("line.separator");
  private static final String tab = "\t";
  
  private static void outHeader(String path, List<EMRowInf> rows)
  {
    if(rows.isEmpty())
    {
      return;
    }
    StringBuffer sb = new StringBuffer();
    sb.append("date");
    for(EMRowInf row : rows)
    {
      sb.append(tab);
      sb.append(row.getName());
    }
    sb.append(crlf);
    fileout( path, sb.toString() );
  }
  
  public static void outRows(String path, List<EMRowInf> rows)
  {
    if(rows.isEmpty())
    {
      return;
    }
    String fpath = String.format(PATH_DIR,path);
    String filePath = Environment.getExternalStorageDirectory() + fpath;
    File file = new File(filePath);
    if (!file.exists())
    {
      outHeader(path,rows);
    }
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    //sdf.format(c.getTime());

    StringBuffer sb = new StringBuffer();
    sb.append(sdf.format(c.getTime()));
    for(EMRowInf row : rows)
    {
      sb.append(tab);
      sb.append(String.format(row.getFormat(),row.getValue()));
    }
    sb.append(crlf);
    fileout( path, sb.toString() );
  }

  private static void fileout(String path, String msg)
  {
    String fpath = String.format(PATH_DIR,path);
    String filePath = Environment.getExternalStorageDirectory() + fpath;
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

  public static void clear(String path)
  {
    String fpath = String.format(PATH_DIR,path);
    String filePath = Environment.getExternalStorageDirectory() + fpath;
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
