package com.googlemapclustering.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.googlemapclustering.R;
import com.googlemapclustering.models.MapModels;
import com.googlemapclustering.utils.DefaultClusterRenderer;
import com.googlemapclustering.utils.InternetConnection;
import com.googlemapclustering.utils.Utils;
import com.googlemapclustering.webservice.MapWebservice;
import com.googlemapclustering.webservice.WebServiceListener;


import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<MapModels> mapModelsArrayList = new ArrayList<>();
    private ClusterManager<MapModels> clusterManager;
    Context mContext;
    Activity mActivity;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;
        mActivity = this;
        dialog = Utils.dialogProgressBarDeclaration(mActivity);
        initializeMap();
        checkInternetAndLaunchLocationListApi();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        initializeMap();
    }

    public void initializeMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

            }

        });
    }

    private void setCluster() {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapModelsArrayList.get(0).getLatitude(), mapModelsArrayList.get(0).getLongitude()), 14f));
        clusterManager = new ClusterManager(mContext, mMap);
        clusterManager.setRenderer(new DefaultClusterRenderer<MapModels>(mContext, mMap, clusterManager));
        mMap.setOnCameraIdleListener(clusterManager);

        for (int i = 0; i < mapModelsArrayList.size(); i++) {
            MapModels infoWindowItem = new MapModels();
            infoWindowItem.setLatitude(mapModelsArrayList.get(i).getLatitude());
            infoWindowItem.setLongitude(mapModelsArrayList.get(i).getLongitude());
            infoWindowItem.setTitle(mapModelsArrayList.get(i).getTitle());
            infoWindowItem.setAddress(mapModelsArrayList.get(i).getAddress());
            clusterManager.addItem(infoWindowItem);
        }
    }

    private void checkInternetAndLaunchLocationListApi() {
        if (InternetConnection.isConnected(mActivity)) {
            launchLocationListApi();
        } else {

            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();

        }

    }

    public void launchLocationListApi() {
        try {

            final MapWebservice webservice = new MapWebservice(new WebServiceListener() {
                @Override
                public void onError(String message, String response) {
                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("message");
                                hideProgressBar();

                            }
                        });
                    }
                }

                @Override
                public void onSuccess(String message, String response, final Object object) {
                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mapModelsArrayList = (ArrayList<MapModels>) object;
                                setCluster();
                                hideProgressBar();
                            }
                        });
                    }
                }

            }, mContext);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    showProgressBar();
                    webservice.getLocationList();


                }
            };
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void showProgressBar() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public void hideProgressBar() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }

}
