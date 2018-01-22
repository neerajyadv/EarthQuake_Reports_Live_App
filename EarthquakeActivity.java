/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

  private static final String URL_TO_GET_DATA= "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    private ArrayList<EarthQuake> arrayList;
     private EartrhQuakeAdapter eartrhQuakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        ListView listView=(ListView)findViewById(R.id.list);

       eartrhQuakeAdapter=new EartrhQuakeAdapter(this, new ArrayList<EarthQuake>());

        listView.setAdapter(eartrhQuakeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuake currentEarthquake = eartrhQuakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        GetDataInBackground gettingdata= new GetDataInBackground();
        gettingdata.execute(URL_TO_GET_DATA);

    }

    ///changes new

    private class GetDataInBackground extends AsyncTask<String,Void, ArrayList<EarthQuake>>
    {

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... urls) {
            if (urls.length < 1 || urls == null) {
                return null;
            } else {
                arrayList = QueryUtils.getEarthQuakeData(urls[0]);
                return arrayList;
            }
        }

            public void onPostExecute( ArrayList<EarthQuake> arrayList)
               {
                   eartrhQuakeAdapter.clear();

                   if(arrayList != null && !arrayList.isEmpty())
                   {
                       eartrhQuakeAdapter.addAll(arrayList);
                   }
               }

        }
    }
