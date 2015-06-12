package sebastianalarcon.com.astorapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class mapa extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private GoogleMap map;
    private CameraUpdate cameraUpdate;

    private Cursor cursor;

    private final LatLng LOCATION_CITY = new LatLng(6.247899,-75.576239);

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng currentLocation;
    private boolean newLocationReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_CITY, 11);
        map.animateCamera(cameraUpdate);
        cargarRest();
        buildGoogleApiClient();
        createLocationRequest();
    }

    public void onClick(View view){
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_CITY, 11);
        map.animateCamera(cameraUpdate);
    }

    public void onClick1(View view){
        if(newLocationReady){
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation,16);
            map.animateCamera(cameraUpdate);
        }
    }

    public void cargarRest(){
        DataBaseManager Manager = MainActivity.getManager();
        cursor = Manager.cargarCursorContactos();
        if (cursor.moveToFirst()){
            do{
                String dbnombre = cursor.getString(cursor.getColumnIndex(Manager.CN_NAME)).toString();
                String dblatitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LAT)).toString();
                String dblongitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LONG)).toString();
                float lat = Float.parseFloat(dblatitud);
                float longitud = Float.parseFloat(dblongitud);
                final LatLng LOCATION_VAR = new LatLng(lat,longitud);
                map.addMarker(new MarkerOptions()
                        .position(LOCATION_VAR)
                        .title(dbnombre)
                        .snippet(dblatitud+", "+dblongitud)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }while (cursor.moveToNext());

        }
        else{
            Toast.makeText(getApplicationContext(), "No se han ingresado restaurantes", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.formulario) {
            Intent f = new Intent(this,formulario.class);
            startActivity(f);
            return true;
        }
        if (id == R.id.main) {
            Intent m = new Intent(this,MainActivity.class);
            startActivity(m);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation!=null){
            setNewLocation(mLastLocation);
            newLocationReady=true;
        }
        else{
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        setNewLocation(location);
        newLocationReady=true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setNewLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        currentLocation = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Yo estoy aquí")
                .snippet("Esta es tu posición actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }
}