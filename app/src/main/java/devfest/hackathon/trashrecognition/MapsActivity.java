package devfest.hackathon.trashrecognition;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng grand = new LatLng(16.048358, 108.226908);
        mMap.addMarker(new MarkerOptions().position(grand).title("Grand Mercure Danang"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.046543, 108.222262)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.050389, 108.220631)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.051358, 108.216377)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.048675, 108.214787)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.053737, 108.239633)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.058528, 108.238715)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.056518, 108.247193)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.039065, 108.244641)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(16.034148, 108.248096)).title("Thu mua phế liệu giá cao tại Đà Nẵng"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(grand));
    }
}
