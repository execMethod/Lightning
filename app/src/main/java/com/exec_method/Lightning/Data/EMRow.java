package com.exec_method.Lightning.Data;
import java.util.*;

public class EMRow implements EMRowInf
{
  private String name;
  private Double value;
  private String format;
  private List<EMRowInf> list;
  
  public EMRow( String name )
  {
    this.name = name;
    this.value = null;
    this.format = null;
    this.list = new ArrayList<EMRowInf>();
  }
  
  public EMRow( String name, String format )
  {
    this.name = name;
    this.value = null;
    this.format = format;
    this.list = new ArrayList<EMRowInf>();
  }
  
  public EMRow( String name, String format, Double value )
  {
    this.name = name;
    this.value = value;
    this.format = format;
    this.list = new ArrayList<EMRowInf>();
  }
  
  public void addRows( List<EMRowInf> rows )
  {
    list.addAll(rows);
  }
  
  public void addRow( EMRowInf row )
  {
    list.add( row );
  }
  
  public List<EMRowInf> getRows()
  {
    return list;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setValue(Double value)
  {
    this.value = value;
  }

  public Double getValue()
  {
    return value;
  }

  public void setFormat(String format)
  {
    this.format = format;
  }

  public String getFormat()
  {
    return format;
  }}
