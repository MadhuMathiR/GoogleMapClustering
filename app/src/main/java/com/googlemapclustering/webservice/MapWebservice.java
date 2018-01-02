package com.googlemapclustering.webservice;

import android.content.Context;
import android.util.Log;

import com.googlemapclustering.R;
import com.googlemapclustering.models.MapModels;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MapWebservice {
    WebServiceListener webServiceListener;
    Context context;

    public MapWebservice(WebServiceListener webServiceListener, Context context) {
        this.webServiceListener = webServiceListener;
        this.context = context;
    }

    public void getLocationList() {
        try {
            String URL = WebServiceApi.LOCATION_API+context.getString(R.string.google_maps_key);

            OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            Request request = new Request.Builder().url(URL).build();
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            Log.d("Meyda", URL);
            if (response.isSuccessful()) {
                JSONObject jsonObjectResponse = new JSONObject(responseBody.string());
                String StringResponse = jsonObjectResponse.toString();
                Log.d("Stringresponse", StringResponse);


                JSONArray jsonArray = jsonObjectResponse.optJSONArray("results");
                ArrayList<MapModels> mapModelsArrayList = MapLocationParser.parseLocationListData(jsonArray);

                webServiceListener.onSuccess("Success", "", mapModelsArrayList);
            } else {
                webServiceListener.onError(context.getString(R.string.error), "");
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
