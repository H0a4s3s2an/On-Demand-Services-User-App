package com.example.sample.usertribe.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Fragments.BookServiceFragment;
import com.example.sample.usertribe.Fragments.JobFragment;
import com.example.sample.usertribe.Fragments.SettinFragment;
import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Helper.PermissionUtils;
import com.example.sample.usertribe.Helper.SharedHelper;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.Dash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;
    private ImageView appbar_image;
    private LocationManager locationmanager;
    public Context context=DashBoardActivity.this;
    private String provider;
    UserAccount userAccount;
    TextView username;
    ImageView profile;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    CustomDialog customDialog;
    TextView enter_location_0;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public int navItemIndex = 0;
    public String CURRENT_TAG = TAG_BOOK;
    private static final String TAG_BOOK = "book service";
    private static final String TAG_SETTING = "setting";
    private static final String TAG_JOB = "job";
    boolean push = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        appbar_image = (ImageView) findViewById(R.id.appbar_image);
//        appbar_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashBoardActivity.this, AutoCompleteSearch.class));
//                finish();
//            }
//        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        username=(TextView)header.findViewById(R.id.usernameTxt);
        profile=(ImageView)header.findViewById(R.id.img_profile);
        username.setText(SharedHelper.getKey(this,"fname")+" "+SharedHelper.getKey(this,"lname"));
        if (!SharedHelper.getKey(this, "photo").equalsIgnoreCase("")
                && !SharedHelper.getKey(this, "photo").equalsIgnoreCase(null)
                && SharedHelper.getKey(this, "photo") != null)
        {
            Picasso.get()
                    .load(SharedHelper.getKey(this, "photo"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile);
        }
        else
        {
            Picasso.get()
                    .load(R.drawable.ic_dummy_user)
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile);
        }
//        header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadMethod();
//            }
//        });
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.book_service);
        setUpFirebaseAuthListening();
//        enter_location_0 = (TextView) findViewById(R.id.enter_location_0);
        addressResultReceiver = new LocationAddressResultReceiver(new Handler());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            };
        };
        startLocationUpdates();

    }

    private void setUpFirebaseAuthListening() {
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Log.d("Auth Listener", "DASHBOARD- onAuthStateChanged");
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user == null){
                    //Cleanup other resources as well
                    Log.d("DASHBOARD- Signed", "Out");
                    user = null;
                    Intent loginActivityIntent = new Intent(DashBoardActivity.this, FlatActivity.class);
                    startActivity(loginActivityIntent);
                    finish();
                }
            }
        };
    }

    private void getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(DashBoardActivity.this,
                    "Can't find current address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        Intent intent = new Intent(this, GetAddressIntentService.class);
//        intent.putExtra("add_receiver", addressResultReceiver);
//        intent.putExtra("add_location", currentLocation);
//        startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission not granted, " +
                                    "restart the app if you want the feature",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(DashBoardActivity.this,
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");

            showResults(currentAdd);
        }
    }

    private void showResults(String currentAdd){
        enter_location_0.setText(currentAdd);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
        }
    }
    public class GetAddressIntentService extends IntentService {

        private static final String IDENTIFIER = "GetAddressIntentService";
        private ResultReceiver addressResultReceiver;

        public GetAddressIntentService() {
            super(IDENTIFIER);
        }

        //handle the address request
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            String msg = "";
            //get result receiver from intent
            addressResultReceiver = intent.getParcelableExtra("add_receiver");

            if (addressResultReceiver == null) {
                Log.e("GetAddressIntentService",
                        "No receiver, not processing the request further");
                return;
            }

            Location location = intent.getParcelableExtra("add_location");

            //send no location error to results receiver
            if (location == null) {
                msg = "No location, can't go further without location";
                sendResultsToReceiver(0, msg);
                return;
            }
            //call GeoCoder getFromLocation to get address
            //returns list of addresses, take first one and send info to result receiver
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1);
            } catch (Exception ioException) {
                Log.e("", "Error in getting address for the location");
            }

            if (addresses == null || addresses.size()  == 0) {
                msg = "No address found for the location";
                sendResultsToReceiver(1, msg);
            } else {
                Address address = addresses.get(0);
                StringBuffer addressDetails = new StringBuffer();

                addressDetails.append(address.getFeatureName());
                addressDetails.append("\n");

                addressDetails.append(address.getThoroughfare());
                addressDetails.append("\n");

                addressDetails.append("Locality: ");
                addressDetails.append(address.getLocality());
                addressDetails.append("\n");

                addressDetails.append("County: ");
                addressDetails.append(address.getSubAdminArea());
                addressDetails.append("\n");

            }
        }
        //to send results to receiver in the source activity
        private void sendResultsToReceiver(int resultCode, String message) {
            Bundle bundle = new Bundle();
            bundle.putString("address_result", message);
            addressResultReceiver.send(resultCode, bundle);
        }
    }


    private void loadMethod() {
        startActivity(new Intent(this,EditProfileActivity.class));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /* Remove the location listener updates when Activity is paused */



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment=null;
       switch (itemId)
       {
           case R.id.book_service:
               navItemIndex = 0;
               CURRENT_TAG = TAG_BOOK;
               fragment = new BookServiceFragment();
               Bundle bundle = new Bundle();
               bundle.putBoolean("push", push);
               fragment.setArguments(bundle);
               break;
           case R.id.jobe:
               navItemIndex = 1;
               CURRENT_TAG = TAG_JOB;
               fragment=new JobFragment();
               break;
           case R.id.pro:
               startActivity(new Intent(DashBoardActivity.this,ProfileVisitActivity.class));
               break;
           case R.id.nav_share:
               navItemIndex = 4;
               CURRENT_TAG = TAG_SETTING;
               fragment=new SettinFragment();

       }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
