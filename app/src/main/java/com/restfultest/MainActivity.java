package com.restfultest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends ActionBarActivity {

    ListView mListView;
    private Collection<Feriado> feriados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        new GetFeriados().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetFeriados extends AsyncTask<Void, Void, String>{

        FeriadoApi api;
        @Override
        protected String doInBackground(Void... params) {

            api = new FeriadoApi();
            feriados = api.getDiasFeriados();

            if (feriados == null){
                return "Ha ocurrido un error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (TextUtils.isEmpty(s)){
                FeriadoAdapter adapter = new FeriadoAdapter(feriados, MainActivity.this);
                mListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                mListView.invalidateViews();
            }else {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FeriadoAdapter extends BaseAdapter{

        private ArrayList<Feriado> list;
        private Context mContext;

        private FeriadoAdapter(Collection<Feriado> list, Context mContext) {
            this.list = new ArrayList<>(list);
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            TextView textView = (TextView) inflater.inflate(R.layout.item_row, null);

            Feriado item = (Feriado) getItem(position);

            textView.setText(item.getMotivo()+" - "+item.getFecha_original());
            return textView;
        }
    }
}
