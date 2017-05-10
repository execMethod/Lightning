package com.exec_method.Lightning.Data;

import android.view.*;
import android.widget.*;
import com.exec_method.Lightning.*;

public class EMHolder
{
  private TextView name = null;
  private TextView value = null;
  private EMRowInf row = null;
  
  public void setHoldable(EMRowInf row)
  {
    this.row = row;
    
    if( null != row.getName() )
    {
      name.setText( row.getName() );
      name.setVisibility(View.VISIBLE);
    }
    else
    {
      name.setVisibility(View.INVISIBLE);
    }
    
    if( null != row.getValue() && null != row.getFormat() )
    {
      value.setText( String.format( row.getFormat(), row.getValue().doubleValue() ) );
      value.setVisibility(View.VISIBLE);
    }
    else if( null != row.getFormat() )
    {
      value.setText( row.getFormat() );
      value.setVisibility(View.VISIBLE);
    }
    else
    {
      value.setVisibility(View.GONE);
    }
  }
  
  public EMRowInf getRowInf()
  {
    return row;
  }
  
  public void findView(View view)
  {
    name = (TextView) view.findViewById(R.id.row_name);
    value = (TextView) view.findViewById(R.id.row_value);
  }
}
