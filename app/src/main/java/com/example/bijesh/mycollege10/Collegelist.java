package com.example.bijesh.mycollege10;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.effectivenavigation.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Collegelist extends FragmentActivity {

	HTTPConnection http;

	List<Colleges> college = new ArrayList<Colleges>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.college_list);
		http = new HTTPConnection(getApplicationContext());
		if (http.isNetworkConnection()) {

			//String data = http.HTTPGetData("http://localhost/minorproject/show.php");
			//Toast.makeText(getApplicationContext(),data ,Toast.LENGTH_LONG).show();
			task.execute();
		}
		else {


			Toast.makeText(getApplicationContext(), "check your connection",
					Toast.LENGTH_LONG).show();
		}
	}
//	http://192.168.164.1/mycollege/show.php
	
	AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

		@Override
		protected String doInBackground(Void... params) {
			String data = http.HTTPGetData("https://raw.githubusercontent.com/bijeshniroula/bijeshniroula/master/new.json");
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			populateList(result);
			displayList();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}
	
	};
	protected void populateList(String result) {
		try {
			//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
			JSONObject jobj = new JSONObject(result);
			String res = jobj.getString("success");
			if (!res.equals("true")) {
				Toast.makeText(getApplicationContext(), "JSON Error",
						Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				JSONArray data = jobj.getJSONArray("msg");
			//	Toast.makeText(getApplicationContext(),"successss",Toast.LENGTH_SHORT).show();
				for (int i = 0; i < data.length(); i++) {
					JSONObject col = data.getJSONObject(i);
					Colleges cg = new Colleges( col.getString("name"),
					col.getString("address"),
							col.getString("faculties"),

							col.getString("contact"),
							col.getString("details"),
							col.getString("websites"),
							col.getString("facebook"));

					college.add(cg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
	}

	
	protected void displayList() {
		ArrayAdapter<Colleges> adapter = new ArrayAdapter<Colleges>(this, R.layout.list_item,college){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.list_item,null);
				//set values
				Colleges c = college.get(position);
				((TextView)view.findViewById(R.id.name)).setText(c.getName());
			((TextView)view.findViewById(R.id.address)).setText(c.getAddress());
				/*
((TextView)view.findViewById(R.id.contact)).setText(c.getContact());
				*/
				return view;
			}
			
		};
		
		final ListView collegelistnew = (ListView) findViewById(R.id.listView);
		collegelistnew.setAdapter(adapter);

		collegelistnew.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				/*Toast.makeText(
						getApplicationContext(),
						"You clicked position" + position + "with item name"
								+ college.get(position).getName(),
						Toast.LENGTH_LONG).show();*/


				Intent newIntent =new Intent(getApplicationContext(),CollegeDetails.class);

				newIntent.putExtra("college", college.get(position));
				startActivity(newIntent);



			}

		});
	
	}}

	

