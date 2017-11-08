package ptr.hf.ui.map;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ptr.hf.R;
import ptr.hf.network.ApiHelper;
import ptr.hf.network.ErrorResponse;
import ptr.hf.network.IApiResultListener;
import ptr.hf.network.Station;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.InfoWindowAdapter {

    private ArrayList<Station> stations;
    private MapView mapView;
    private GoogleMap map;
    private GeoDataClient geoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Unbinder unbinder;

    private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

//        geoDataClient = Places.getGeoDataClient(getContext(), null);
//
//        placeDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
//        }
//
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
////                            currentLocation = location;
//                            onLocationChanged(location);
//                        }
//                    }
//                });
        getStationsFromOCM();

        unbinder = ButterKnife.bind(this, view);
        return view;
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
        map = googleMap;
        if (stations != null)
            for (Station station : stations) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(station.getLatLng()).title(station.getAddressTitle()));
//                markers.add(marker);
            }
        googleMap.setOnMarkerClickListener(this);

        googleMap.setMyLocationEnabled(true);

//        map.moveCamera(center);
//        map.animateCamera(zoom);
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

        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(18)
                .bearing(location.getBearing())
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        map.animateCamera(camUpd3);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = layoutInflater.inflate(R.layout.popup, null);
        TextView tv=(TextView)popup.findViewById(R.id.title);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());

        return(popup);
    }
}
