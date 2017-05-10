package com.exec_method.Lightning.Engine;
import com.exec_method.Lightning.Data.*;
import java.util.*;

public interface EMLocationListener
{
  public void locationInfo( List<EMRowInf> rows );
  
  public void error(Exception e);
}

