package com.exec_method.Lightning.Engine;
import android.content.*;
import android.telephony.*;
import com.exec_method.Lightning.Data.*;
import java.text.*;
import java.util.*;

public class EMTelephonyMonitor
{
  private TelephonyManager mgr;
  private EMListener lsr;
  
  public EMTelephonyMonitor(Context context, EMListener lsr)
  {
    mgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    this.lsr = lsr;
  }
  
  public List<EMRowInf> getRows()
  {
    List<EMRowInf> rows = new ArrayList<EMRowInf>();
    
    Date date = new Date();
    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String nowtime = formatter.format(date);
    rows.add(new EMRow("time",nowtime));
    
    List<CellInfo> infos = mgr.getAllCellInfo();
    int cnt = 0;
    for(CellInfo info : infos )
    {
      if( info instanceof CellInfoLte )
      {
        CellInfoLte ilte = (CellInfoLte) info;
        CellIdentityLte clte = ilte.getCellIdentity();
        rows.add( getParam("Ci ",cnt,clte.getCi() ));
        rows.add( getParam("Mcc",cnt,clte.getMcc() ));
        rows.add( getParam("Mnc",cnt,clte.getMnc() ));
        rows.add( getParam("Pci",cnt,clte.getPci() ));
        rows.add( getParam("Tac",cnt,clte.getTac() ));
        CellSignalStrengthLte slte = ilte.getCellSignalStrength();
        rows.add(new EMRow("Asu["+cnt+"]","%4.0f",(double)slte.getAsuLevel() ));
        rows.add(new EMRow("Dbm["+cnt+"]","%4.0f",(double)slte.getDbm() ));
        rows.add(new EMRow("Lv ["+cnt+"]","%4.0f",(double)slte.getLevel() ));
        rows.add( getParam("Timing",cnt,slte.getTimingAdvance() ));
      }
      cnt++;
    }
    
    return rows;
  }
  
  private EMRow getParam(String name, int cnt, int value)
  {
    if( Integer.MAX_VALUE == value )
    {
      return new EMRow( name + "[" + cnt + "]", "-");
    }
    else
    {
      return new EMRow( name + "[" + cnt + "]", "%4.0f", (double)value);
    }
  }
  
  public interface EMListener
  {
    
  }
  
}
