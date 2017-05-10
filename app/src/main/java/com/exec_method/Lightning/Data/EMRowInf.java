package com.exec_method.Lightning.Data;

import java.io.*;
import android.graphics.drawable.*;
import org.apache.http.*;
import android.service.notification.*;

public interface EMRowInf extends Serializable
{
  public String getName();
  public Double getValue();
  public String getFormat();
}

