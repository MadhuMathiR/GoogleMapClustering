package com.googlemapclustering.webservice;

import com.googlemapclustering.models.MapModels;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 12/13/2017.
 */

public class MapLocationParser {
    public static ArrayList<MapModels> parseLocationListData(final JSONArray jsonArray) {

        ArrayList<MapModels> mapModelsArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                MapModels poi = new MapModels();
                if (jsonArray.getJSONObject(i).has("name")) {
                    poi.setTitle(jsonArray.getJSONObject(i).optString("name"));
                    poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                    if (jsonArray.getJSONObject(i).has("opening_hours")) {
                        if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                poi.setOpennow("YES");
                            } else {
                                poi.setOpennow("NO");
                            }
                        }
                    } else {
                        poi.setOpennow("Not Known");
                    }
                    if (jsonArray.getJSONObject(i).has("geometry")) {
                        if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location")) {

                            poi.setLatitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat")));
                            poi.setLongitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng")));
                        }
                    }
                    if (jsonArray.getJSONObject(i).has("vicinity")) {
                        poi.setAddress(jsonArray.getJSONObject(i).optString("vicinity"));
                    }
                    if (jsonArray.getJSONObject(i).has("icon")) {
                        poi.setImageURl(jsonArray.getJSONObject(i).optString("icon"));
                    }
                    if (jsonArray.getJSONObject(i).has("types")) {
                        JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                        for (int j = 0; j < typesArray.length(); j++) {
                            poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                        }
                    }
                }
                mapModelsArrayList.add(poi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapModelsArrayList;
    }
}
