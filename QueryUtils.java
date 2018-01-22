package com.example.android.quakereport;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private URL urlObj;
    private HttpURLConnection httpURLConnection;
    private QueryUtils() {
        }


    public static  ArrayList<EarthQuake> getEarthQuakeData(String url) {

        //create a url from the string provided
        URL urlObj = getURL(url);

        //after getting URL, start a connection and extract data which we want

        String jsonResponse=null;
        try {
            jsonResponse = getJSONResponse(urlObj); //perform it in try catch
        } catch (IOException e) {
            System.out.print("error while getting json response back from url " + e);
        }

        //when we got full data, extract relevant fields from it
        ArrayList<EarthQuake> earthQuake= getRelevantFields(jsonResponse);

        return earthQuake;
    }

    public static URL getURL(String urlString) {
        if (urlString == null) {
            return null;
        } else {
            URL url=null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                System.out.println("Error with building URL " + e);
            }
            return url;
        }
    }

    public static String getJSONResponse(URL urlObj) throws IOException {

        String jSONResponse = "";

        if (urlObj == null) {
            return null;
        } else {
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                httpURLConnection = (HttpURLConnection) urlObj.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                //now get data from the connection
                //value of http_ok is alo 200, response of success
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)  {
                    inputStream = httpURLConnection.getInputStream();
                    jSONResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
                }
            } catch (IOException e) {
                System.out.println("Cannot get data from url error in inout stream " + e);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jSONResponse;
        }
    }

        //method for line 76 which extract pure string from JSONResponse recieved from URL
        public static String readFromStream(InputStream inputStream) throws IOException
        {
            StringBuilder jsonResponse=new StringBuilder();

            if(inputStream !=null)
            {
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String checkNull= bufferedReader.readLine();
                while(checkNull != null)
                {
                    jsonResponse.append(checkNull);
                    checkNull=bufferedReader.readLine();
                }
            }
            return jsonResponse.toString();
        }

        //method to extract relevant fields and then invoke the earthQuake class to rteurn those fields
    public static ArrayList<EarthQuake> getRelevantFields(String jsonResponse)
    {
        if(TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        else {
            ArrayList<EarthQuake> earthQuakes = new ArrayList<>();
            try {
                JSONObject rootJsonIbject = new JSONObject(jsonResponse);

                JSONArray rootJsonArray = rootJsonIbject.getJSONArray("features");

                if (rootJsonArray.length() > 0) {
                    for (int i = 0; i < rootJsonArray.length(); i++) {
                        JSONObject insideObjects = rootJsonArray.getJSONObject(i);

                        JSONObject getProperties = insideObjects.getJSONObject("properties");

                        String location = getProperties.getString("place");
                        double magnitude = getProperties.getDouble("mag");
                        long time = getProperties.getLong("time");
                        String urlfromJson = getProperties.getString("url");

                        EarthQuake earthQuake = new EarthQuake(magnitude, location, time, urlfromJson);

                        earthQuakes.add(earthQuake);

                    }
                }
            } catch (JSONException e) {
                System.out.println("Error in JSon  while getting relevant fieds " + e);
            }
            return earthQuakes;
        }
    }
}
