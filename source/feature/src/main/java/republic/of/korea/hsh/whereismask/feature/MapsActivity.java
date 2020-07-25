package republic.of.korea.hsh.whereismask.feature;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.*;
import republic.of.korea.hsh.whereismask.feature.instance_items.Store;
import republic.of.korea.hsh.whereismask.feature.instance_items.StoreResult;
import republic.of.korea.hsh.whereismask.feature.instance_items.StoreSale;
import republic.of.korea.hsh.whereismask.feature.instance_items.StoreSaleResult;
import republic.of.korea.hsh.whereismask.feature.network.DownloadJson;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.os.*;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Handler h;
    LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient;
    Location mCurrentLocation=null;
    private Geocoder geo_coder;
    boolean once =true;
    private void createLocationCallback(){
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                sema.release();
                android.util.Log.e(getClass().toString(),"release for step2-1");
                if(once) {
                    LatLng SEOUL = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    once=false;
                }

                updateLocationUI();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mFusedLocationClient!=null){
            mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mFusedLocationClient = null;
                }
            });
        }
    }

    boolean mLocationPermissionGranted = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if(requestCode==101){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }
    @SuppressWarnings("MissingPermission")
    private void getCurrentLocation(){
        if(mLocationPermissionGranted){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,Looper.myLooper());
            updateLocationUI();
        }
    }
    @SuppressWarnings("MissingPermission")
private void getLocationPermission(){
    if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
        mLocationPermissionGranted = true;
        getCurrentLocation();
    }
    else{

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},101);

    }
}
    @SuppressWarnings("MissingPermission")
    private void updateLocationUI(){
        if(mMap==null)return;
        if(mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else{
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private LocationRequest mLocationRequest;
    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000/24);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
    private void addMarkersByStore(ArrayList<Store> stores){
        for(Store store1 : stores) {
            if(!store1.isGPSnull()) {
                LatLng SEOUL = new LatLng(store1.getLatitude(), store1.getLongitude());
                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(SEOUL);
                markerOptions.title(store1.getName());
                markerOptions.snippet(store1.getAddr());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(markerOptions);
                    }
                });
                try {
                    Thread.sleep(5);
                }catch(Exception e){

                }

            }
        }
        try {
            Thread.sleep(100);
        }catch(Exception e){

        }
    }
    public BitmapDescriptor getMapMarker(String color){
        if(color==null)return BitmapDescriptorFactory.defaultMarker();
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color),hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    private void addMarkersByStoreSale(ArrayList<StoreSale> stores){
        for(StoreSale store1 : stores) {
            if(!store1.isGPSnull()) {
                final LatLng SEOUL = new LatLng(store1.getLatitude(), store1.getLongitude());
                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(SEOUL);

                markerOptions.title(store1.getName());
                markerOptions.snippet(store1.getAddress());
                StoreSale.RemainStat rstat = store1.getRemainStat();
                if(rstat==StoreSale.RemainStat.Plenty){
                    markerOptions.icon(getMapMarker("#6060e0"));
                }
                else if(rstat==StoreSale.RemainStat.Some){
                    markerOptions.icon(getMapMarker("#a0a030"));
                }
                else if(rstat==StoreSale.RemainStat.Empty){
                    markerOptions.icon(getMapMarker("#808080"));
                }
                else if(rstat==StoreSale.RemainStat.Break){
                    markerOptions.icon(getMapMarker("#101010"));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mMap.addMarker(markerOptions);
                    }
                });
                try {
                    Thread.sleep(5);
                }catch(Exception e){

                }

            }
        }
        try {
            Thread.sleep(100);
        }catch(Exception e){

        }
    }
    Semaphore sema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geo_coder = new Geocoder(this);

        once =true;
        sema = new Semaphore(1);
        try {
            sema.acquire();
        }catch(Exception e){

        }
        mCurrentLocation = null;
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        createLocationRequest();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        h = new Handler();
        android.util.Log.e(getClass().toString(),"oncreate::step1");
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.util.Log.e(getClass().toString(),"oncreate::step2-1");
                try{
                    sema.acquire();
                }
                catch(Exception e){ }
                android.util.Log.e(getClass().toString(),"oncreate::step2-2");
                DownloadJson.URLgetQueryBuilder param = new DownloadJson.URLgetQueryBuilder();
                //param.addParameter("page",1+"");
                param.addParameter("lat",mCurrentLocation.getLatitude()+"");
                param.addParameter("lng",mCurrentLocation.getLongitude()+"");
                param.addParameter("m",5000+"");
                DownloadJson<StoreSaleResult> download = new DownloadJson<StoreSaleResult>();
                android.util.Log.e(getClass().toString(),"start download");
                download.execute(StoreSaleResult.class,DownloadJson.URL_STORES_BY_GPS,param.build(false));
                try {
                    StoreSaleResult store = download.get();
                    android.util.Log.e(getClass().toString(),"finish download");
                    android.util.Log.e(MapsActivity.this.getClass().toString(), store.toString());
                    ArrayList<StoreSale> stores = store.getStores();
                    addMarkersByStoreSale(stores);
                }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    android.util.Log.e(e.toString(),sw.toString());
                }
                sema.release();
            }
        },1000);
        android.util.Log.e(getClass().toString(),"oncreate::step3");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    android.util.Log.e(getClass().toString(),"oncreate::step4-1");
                    List<Address> addrs = geo_coder.getFromLocation(37.395303, 127.291804, 3);
                    for(Address addr:addrs){
                        final StringBuffer sb = new StringBuffer();
                        //sb.append("data:"+addr.toString()+"\n");
                        sb.append(String.format("Country Name:%s\n", addr.getCountryName()));
                        sb.append(String.format("Country Code:%s\n",addr.getCountryCode()));
                        sb.append(String.format("admin area:%s\n",addr.getAdminArea()));
                        sb.append(String.format("locality:%s\n",addr.getLocality()));
                        sb.append(String.format("getThoroughfare:%s\n",addr.getThoroughfare()));
                        sb.append(String.format("getFeatureName:%s\n",addr.getFeatureName()));
                        sb.append(String.format("sub locality:%s\n",addr.getSubLocality()));
                        sb.append(String.format("getSubAdminArea:%s\n",addr.getSubAdminArea()));
                        sb.append(String.format("getSubThoroughfare:%s\n",addr.getSubThoroughfare()));

                        sb.append("address line:"+addr.getMaxAddressLineIndex());
                        for(int i=0;i<=addr.getMaxAddressLineIndex();i++){
                            sb.append(addr.getAddressLine(i)+"\n");
                        }
                        android.util.Log.e(getClass().toString(),"oncreate::step4-2");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                android.util.Log.e(getClass().toString(),"oncreate::step4-3-1");
                                android.util.Log.i(getClass().toString(),sb.toString());
                                Toast.makeText(MapsActivity.this.getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }catch(final Exception e){
                    android.util.Log.e(getClass().toString(),"oncreate::step5");
                    final StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            android.util.Log.i(MapsActivity.this.getClass().toString(), sw.toString());
                            Toast.makeText(MapsActivity.this.getApplicationContext(),sw.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
        android.util.Log.e(getClass().toString(),"oncreate::step6");
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
        if(!mLocationPermissionGranted){
            getLocationPermission();
        }
        /*
        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));*/
    }
}
