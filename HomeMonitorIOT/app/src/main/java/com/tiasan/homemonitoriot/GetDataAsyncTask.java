/**
 * Author: Tiago Sarmento Santos
 * Github: https://github.com/tiagosarmento/HomeMonitorIOT
 *
 * Software License Agreement
 * The present software is open-source and it is owned by this project contributors. Feel free to
 * use it on your own and to improve it for your needs. You may not combine this software with
 * "viral" open-source software in order to form a larger program. This software is being done as
 * an hobby and a DIY project. It is provided as is and with all possible faults associated.
 * The software contributors shall not, under any circumstances, be liable for special, incidental
 * or consequential damages for any reason whatsoever.
 */

package com.tiasan.homemonitoriot;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GetDataAsyncTask extends AsyncTask<String, Integer, Double> {

    // Set Global data
    private String                     gTag          = "DBG - GetDataAsyncTask";
    private ProgressDialog             pdFetchData   = null;
    private GraphView                  gvPlotData    = null;
    private String                     gDataPort     = null;
    private String                     gCIK          = null;
    private String                     gSensorNameID = null;
    private LineGraphSeries<DataPoint> gsDataSeries  = null;

    public void setParameters(ProgressDialog pdMsg,
                              GraphView gvGraph,
                              String sCIK,
                              String sDataPort,
                              String sSensorNameID) {
        this.pdFetchData   = pdMsg;
        this.gvPlotData    = gvGraph;
        this.gCIK          = sCIK;
        this.gDataPort     = sDataPort;
        this.gSensorNameID = sSensorNameID;
    }

	@Override
	protected Double doInBackground(String... params) {
		// TODO Auto-generated method stub
        Log.d(gTag, "doInBackground" );
	    getExoData();
		return null;
	}

    protected void onPostExecute(Double result){
        Log.d(gTag, "onPostExe");

        // Set color line
        if (this.gSensorNameID == "Temperature") {
            this.gsDataSeries.setTitle("Temperature");
            this.gsDataSeries.setColor(Color.RED);
        } else if (this.gSensorNameID == "Humidity") {
            this.gsDataSeries.setTitle("Humidity");
            this.gsDataSeries.setColor(Color.GREEN);
        } else if (this.gSensorNameID == "Visible Light") {
            this.gsDataSeries.setTitle("Visible Light");
            this.gsDataSeries.setColor(Color.CYAN);
        } else if (this.gSensorNameID == "Pressure") {
            this.gsDataSeries.setTitle("Pressure");
            this.gsDataSeries.setColor(Color.BLUE);
        } else { // default case
            this.gsDataSeries.setColor(Color.BLACK);
        }

        this.gsDataSeries.setDrawDataPoints(false);
        this.gsDataSeries.setDataPointsRadius(1);
        this.gsDataSeries.setThickness(2);

        // activate horizontal zooming and scrolling
        this.gvPlotData.getViewport().setScalable(true);
        this.gvPlotData.getViewport().setScrollable(true);
        
        // activate horizontal and vertical zooming and scrolling
        this.gvPlotData.getViewport().setScalableY(true);
        this.gvPlotData.getViewport().setScrollableY(true);

        // Remove any old data series and add the new series
        this.gvPlotData.removeAllSeries();
        this.gvPlotData.addSeries(this.gsDataSeries);

        // set manual x bounds to have nice steps
        this.gvPlotData.getViewport().setMinX(this.gvPlotData.getViewport().getMinX(true));
        this.gvPlotData.getViewport().setMaxX(this.gvPlotData.getViewport().getMaxX(true));
        this.gvPlotData.getViewport().setXAxisBoundsManual(true);

        this.gvPlotData.getViewport().setMinY(this.gvPlotData.getViewport().getMinY(true));
        this.gvPlotData.getViewport().setMaxY(this.gvPlotData.getViewport().getMaxY(true));
        this.gvPlotData.getViewport().setYAxisBoundsManual(true);

        // set date label formatter
        this.gvPlotData.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // Format X axis values
                    Date dDate = new Date((long) value);
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfTime  = new SimpleDateFormat("hh:mm:ss aa");
                    return sdfDate.format(dDate) + "\n" + sdfTime.format(dDate);
                    //return super.formatLabel(value, isValueX);
                } else {
                    // Format Y axis values
                    if (gSensorNameID == "Temperature") {
                        return super.formatLabel(value, isValueX) + " °C";
                    } else if (gSensorNameID == "Humidity") {
                        return super.formatLabel(value, isValueX) + " %";
                    } else if (gSensorNameID == "Visible Light") {
                        return super.formatLabel(value, isValueX) + " Lux";
                    } else if (gSensorNameID == "Pressure") {
                        return super.formatLabel(value, isValueX) + " mBar";
                    } else { // default case
                        return super.formatLabel(value, isValueX);
                    }
                }
            }
        });

        this.gvPlotData.getGridLabelRenderer().setNumHorizontalLabels(4);
        this.gvPlotData.getGridLabelRenderer().setNumVerticalLabels(4);
        this.gvPlotData.getGridLabelRenderer().setLabelsSpace(10);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        //this.gvPlotData.getGridLabelRenderer().setHumanRounding(false);

        if (this.gSensorNameID == "Temperature") {
            this.gvPlotData.setTitle("Temperature Graph");
        } else if (this.gSensorNameID == "Humidity") {
            this.gvPlotData.setTitle("Humidity Graph");
        } else if (this.gSensorNameID == "Visible Light") {
            this.gvPlotData.setTitle("Visible Light Graph");
        } else if (this.gSensorNameID == "Pressure") {
            this.gvPlotData.setTitle("Pressure Graph");
        } // else - default case without name shown
        //this.gvPlotData.setTitleTextSize(10);

        // Set legend
        this.gvPlotData.getLegendRenderer().setBackgroundColor(Color.GRAY);
        this.gvPlotData.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        this.gvPlotData.getLegendRenderer().setVisible(true);

        this.gvPlotData.setVisibility(View.VISIBLE);

        pdFetchData.dismiss();
    }

	protected void onProgressUpdate(Integer... progress){
        Log.d(gTag, "onProgressUpdate" );
	}

    protected void onPreExecute() {
        Log.d(gTag, "onPreExecute" );
        pdFetchData.setTitle("Fetching " + this.gSensorNameID );
        pdFetchData.setMessage("Please wait a moment...");
        pdFetchData.show();
    }
 
    private void getExoData() {
        // Do sanity check on gCIk and gDataPorts
        URL url_exosite = null;
        String sResData = null;
        // 1pull per minute = 60pulls per hour = 1440pulls per day = 4320 pulls per 3 days
        String json_req = "{\"auth\":{\"cik\":\"" + this.gCIK + "\"},\"calls\":[{\"procedure\":\"read\",\"arguments\":[{\"alias\":\"" + this.gDataPort + "\"}, {\"sort\":\"desc\",\"limit\":720}],\"id\":0}]}";
        try {
            url_exosite = new URL("http://m2.exosite.com/onep:v1/rpc/process");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection url_conn_exosite = null;
        try {
            url_conn_exosite = (HttpURLConnection) url_exosite.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Generic options
        try {
            url_conn_exosite.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // Header properties
        url_conn_exosite.addRequestProperty("Content-type", "application/json; charset=utf-8");
        url_conn_exosite.setDoOutput(true);
        url_conn_exosite.setDoInput(true);
        try {
            url_conn_exosite.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        OutputStream os = null;
        try {
            os = url_conn_exosite.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.write(json_req.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Connect to Exosite
        try {
            url_conn_exosite.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int retCode = 0;
        try {
            retCode = url_conn_exosite.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;

        if (retCode == HttpURLConnection.HTTP_OK) {
            try {
                is = url_conn_exosite.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Convert the InputStream into a string
            //Reader reader = null;
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = buffReader.readLine()) != null) {
                    total.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sResData = total.toString();
            Log.d(gTag, "The response is = " + sResData);
        } else {
            Log.d(gTag, "The response code = " + retCode);
        }

        url_conn_exosite.disconnect();
        Log.d(gTag, "get data done");

        // Parse data
        if (sResData != null) {
            JSONArray jsarrMainResult = null;
            try {
                jsarrMainResult = new JSONArray(sResData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsobjMainResult = null;
            try {
                jsobjMainResult = (JSONObject)jsarrMainResult.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsarrResultData = null;
            try {
                jsarrResultData = jsobjMainResult.getJSONArray("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Create data point holder
            int size = jsarrResultData.length();
            DataPoint[] dpResDataXY = new DataPoint[size];

            for (int i = 0; i < jsarrResultData.length(); i++) {
                JSONArray jsarrPointXY = null;
                try {
                    jsarrPointXY = jsarrResultData.getJSONArray(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    
                    Date x       = new Date(jsarrPointXY.getInt(0) * 1000L); //*1000L to convert s to ms
                    double y     = jsarrPointXY.getDouble(1);
                    DataPoint xy = new DataPoint(x, y);
                    dpResDataXY[size-1-i] = xy;

                    Log.d(gTag, "Point try #" + i + ": " + x.getTime() + " " + y);
                } catch (JSONException e) {
                    e.printStackTrace();
                }       

            }
            gsDataSeries = new LineGraphSeries<DataPoint>(dpResDataXY);

        }

    }
 
}