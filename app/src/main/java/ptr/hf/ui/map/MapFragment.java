package ptr.hf.ui.map;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;
import ptr.hf.network.Station;
import ptr.hf.ui.MainActivity;

import static ptr.hf.ui.auth.LoginActivity.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int overview = 0;

    Polyline polylineFinal;

    private ArrayList<Station> stations;
    private MapView mapView;
    private GoogleMap map;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private View popup;
    Unbinder unbinder;

    private ProgressDialog dialog;


    public LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        dialog = ProgressDialog.show(getContext(), "",
                "Térkép betöltése, kérem várjon...", true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        getMyLocation();

//        geoDataClient = Places.getGeoDataClient(getContext(), null);
//
//        placeDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
//        }
//


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private com.google.maps.model.LatLng getMyLocation() {


        final com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(47, 19);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return latLng;

        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
//                                latLng = new com.google.maps.model.LatLng(location.getLatitude(), location.getLongitude());
//                                onLocationChanged(location);
//                                positionCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), map);
                            }
                        }
                    });
        }
        return latLng;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();

        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirectionsDetails(com.google.maps.model.LatLng origin, com.google.maps.model.LatLng destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
//                    .origin(new com.google.maps.model.LatLng(47,19))
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        results
                                .routes[overview]
                                .legs[overview]
                                .startLocation.lat,
                        results
                                .routes[overview]
                                .legs[overview]
                                .startLocation.lng))
                .title(results.routes[overview].legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        results
                                .routes[overview]
                                .legs[overview]
                                .endLocation.lat,
                        results
                                .routes[overview]
                                .legs[overview]
                                .endLocation.lng))
                .title(results
                        .routes[overview]
                        .legs[overview]
                        .startAddress)
                .snippet(getEndLocationTitle(results)));
    }

    private void positionCamera(LatLng route, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route, 12));
    }
    PolylineOptions polylineOptions;


    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        if (polylineFinal != null)
            polylineFinal.remove();
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        PolylineOptions rectLine = new PolylineOptions().width(10).color(getResources().getColor(R.color.white));
        polylineOptions = rectLine.addAll(decodedPath);
        polylineFinal = mMap.addPolyline(polylineOptions);

    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void getStationsFromOCM() {
        ApiHelper.INSTANCE.getStations(new IApiResultListener<ArrayList<Station>>() {
            @Override
            public void success(ArrayList<Station> result) {
                stations = result;
                onMapReady(map);
            }

            @Override
            public void error(ErrorResponse errorResponse) {

            }

            @Override
            public void fail() {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!isAdded())
            return;

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json)
            );
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
//        getMyLocation();

        getStationsFromOCM();

        dialog.dismiss();

        setupGoogleMapScreenSettings(googleMap);

        map = googleMap;
        if (stations != null) {
            for (
                    Station station : stations) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(station.getLatLng()).title(station.getAddressTitle()));
//                markers.add(marker);
            }
        }
        map.setOnMarkerClickListener(this);

//        map.setMyLocationEnabled(true);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()

        {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                popup = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                TextView title = popup.findViewById(R.id.marker_info_title);
                title.setText(marker.getTitle());
                TextView address = popup.findViewById(R.id.marker_info_address);
                address.setText(marker.getSnippet());

//                LinearLayout linearLayout = popup.findViewById(R.id.linlay);
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getDirections(map, new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
//                    }
//                });

//            Station station = (Station) marker.getTag();
////
//            TextView title = ((TextView) popup.findViewById(R.id.marker_info_title));
//            title.setText(station.getAddressTitle());
//            TextView address = ((TextView) popup.findViewById(R.id.marker_info_address));
//            address.setText(station.getAddress());

                return (popup);

            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                getDirections(map, new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
            }
        });

//        map.moveCamera(center);
//        map.animateCamera(zoom);
    }

    private void getDirections(GoogleMap googleMap, com.google.maps.model.LatLng latLng) {

        googleMap.clear();
        com.google.maps.model.LatLng myLoc = getMyLocation();
        DirectionsResult results = getDirectionsDetails(
                myLoc,
                latLng, TravelMode.DRIVING);
        if (results != null) {
            addPolyline(results, googleMap);
//            addMarkersToMap(results, googleMap);
            positionCamera(new LatLng(latLng.lat, latLng.lng), googleMap);
        }

    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(false);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();

        LatLng latLng = new LatLng(latitude, longitude);

        positionCamera(latLng, map);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        onDetach();
    }
}
