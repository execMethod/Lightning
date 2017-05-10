package com.exec_method.Lightning.Activ;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.exec_method.Lightning.Util.*;
import java.util.*;
import com.exec_method.Lightning.Data.*;

public abstract class EMActivity extends ListActivity
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
      onCreateSafe();
    }
    catch ( Exception e )
    {
      EMLog.e(e);
      report(e.getMessage());
    }
  }

  protected abstract void onCreateSafe();

  public void contentView()
  {
    setContentView(getLayoutId());
    setListAdapter(new ListViewAdapter(this, getRowLayoutId(), getRows()));

    ListView listView = getListView();
    listView.setOnItemClickListener(new ItemClickListener());
  }

  public void finish(int resultCode, Intent intent)
  {
    setResult(resultCode, intent);
    super.finish();
  }
  
  protected abstract int getLayoutId();

  protected abstract int getRowLayoutId();

  protected abstract List<EMRowInf> getRows();

  protected abstract void rowClick(EMRowInf box);

  protected void reload()
  {
    EMLog.d();
    ListViewAdapter adpt = (ListViewAdapter)getListAdapter();
    adpt.clear();
    adpt.addAll(getRows());
    adpt.notifyDataSetChanged();
  }
  
  private class ListViewAdapter extends ArrayAdapter<EMRowInf>
  {
    private LayoutInflater inflater;
    private int layout;
    EMRowInf row;

    public ListViewAdapter(Context context, int layout,  List<EMRowInf> rows)
    {
      super(context, 0, rows);
      this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      try
      {
      EMHolder holder;
      if (convertView == null)
      {
        convertView = inflater.inflate(layout, parent, false);
        holder = new EMHolder();
        holder.findView(convertView);
        convertView.setTag(holder);
      }
      else
      {
        holder = (EMHolder) convertView.getTag();
      }

      row = getItem(position);
      holder.setHoldable(row);
      }
      catch(Exception e)
      {
        EMLog.e(e);
        report(e.getMessage());
      }
      return convertView;
    }
  }

  private class ItemClickListener implements OnItemClickListener
  {
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      EMHolder holder = (EMHolder)view.getTag();
      try
      {
        rowClick(holder.getRowInf());
      }
      catch ( Exception e )
      {
        EMLog.e(e);
        report(e.getMessage());
      }
    }
  }
}

