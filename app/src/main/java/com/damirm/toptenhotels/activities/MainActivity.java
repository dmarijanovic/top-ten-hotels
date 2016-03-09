package com.damirm.toptenhotels.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.damirm.toptenhotels.adapters.PlaceAdapter;
import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.models.PlaceData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String[] PLACE_IDS = {
            "ChIJ-4PswvnWZUcREtjgku_DPV4", // Esplanade Zagreb Hotel
            "ChIJByx7Skd2ZkcREXobqNdnON0", // HOTEL ASTORIA
            "ChIJ9VHZHwDXZUcRE_Rjjwcuyjk", // Hotel Jadran Zagreb
            "ChIJN-YgP_nWZUcRcKsl33CgitI", // Palace hotel Zagreb
            "ChIJQT3IU4nVZUcREAPjpI8mx-U", // Hotel Aristos Zagreb
            "ChIJT7O1I53XZUcRWLQkN4ZFJqE", // Hotel Vila Tina
            "ChIJt-OxIEPRZUcRB4QcXV4bxVQ", // Hotel Antunović Zagreb
            "ChIJByx7Skd2ZkcREXobqNdnON0", // Hotel Croatia
            "ChIJi-35ToPTZUcR6AsVi6vGSTU", // Hotel Calypso
            "ChIJsXTemhfXZUcR6WuHj6aa-rs" // Hotel AS Zagreb
    };

    private ListView listView;
    private PlaceAdapter placeAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initToolbar();

        listView = (ListView) findViewById(R.id.listView);

        placeAdapter = new PlaceAdapter(getBaseContext(), apiClient);
        listView.setAdapter(placeAdapter);
        listView.setOnItemClickListener(onPlaceClickListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getText(R.string.loading_message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            getPlaces();
        } else {
            Toast.makeText(getBaseContext(), R.string.no_network,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetch list of places from google api and push it to list adapter
     */
    private void getPlaces() {
        progressDialog.show();
        Places.GeoDataApi.getPlaceById(apiClient, PLACE_IDS)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            Iterator<Place> placeIterator = places.iterator();
                            List<PlaceData> placesData = new ArrayList<>();

                            while (placeIterator.hasNext()) {
                                Place place = placeIterator.next();
                                placesData.add(new PlaceData(place));
                            }
                            placeAdapter.setPlaces(placesData);
                        }
                        places.release();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private AdapterView.OnItemClickListener onPlaceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceData placeData = (PlaceData) placeAdapter.getItem(position);
            Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
            intent.putExtra(PlaceData.class.getName(), placeData);

            startActivity(intent);
        }
    };

}
