package com.example.sample.usertribe.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.usertribe.Common.Common;
import com.example.sample.usertribe.Helper.CustomDialog;
import com.example.sample.usertribe.Interface.IFCMService;
import com.example.sample.usertribe.Model.FCMResponse;
import com.example.sample.usertribe.Model.Notification;
import com.example.sample.usertribe.Model.Provider;
import com.example.sample.usertribe.Model.Sender;
import com.example.sample.usertribe.Model.Token;
import com.example.sample.usertribe.Model.UserAccount;
import com.example.sample.usertribe.R;
import com.example.sample.usertribe.utils.NetworkUtils;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Hide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class ServiceProMapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private static final int My_PERMISSION_REQUEST_CODE = 7000;
    private LocationRequest locationRequest;
    private Location location;
    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 5000;
    DatabaseReference drivers,mdrivers, databaseReference,driverAvailable,user_account,filterdata;
    GeoFire geoFire;
    Marker marker;
    IFCMService mservice;
    SupportMapFragment mapFragment;
    private static final int MAX_GPS_SEARCH_SECONDS = 30;
    private CountDownTimer mCountDownTimer;
    private boolean mLocationFound;
    boolean isDriverFound = false;
    String driverId = "";
    int radius = 1;
    int distance = 1;
    private static final int LIMIT = 5;
    LinearLayout no_provider;
    LinearLayout provider_layout;
    Button request;
    ImageView provider_img;
    TextView provider_name;
    FusedLocationProviderClient fusedLocationProviderClient;
    com.google.android.gms.location.LocationCallback locationCallback;
    String category_name;
    LinearLayout maplayout;
    String fname,lname,phone,photo,uid;
    String value;
    String title;
    String provider;
    TextView service_name;
    String job_description;
    Marker mDriverMarker;
    List<Marker> markers = new ArrayList<Marker>();
    String img;
    Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_pro_map);
        if(getIntent()!=null)
        {
            title=getIntent().getStringExtra("subcategory_name");
            value=getIntent().getStringExtra("detail_name");
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Service Providers");
        job_description=title+"-"+value;
        provider_img = (ImageView) findViewById(R.id.provider_img);
        provider_name = (TextView) findViewById(R.id.provider_name);
        no_provider = (LinearLayout) findViewById(R.id.no_provider);
        provider_layout = (LinearLayout) findViewById(R.id.provider_layout);
        request = (Button) findViewById(R.id.request);
        maplayout=(LinearLayout)findViewById(R.id.maplayout);
        mservice = Common.getFCMService();
        service_name=(TextView)findViewById(R.id.service_name);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDriver(driverId);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.providermap);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        category_name=getIntent().getStringExtra("category_name");
        drivers = FirebaseDatabase.getInstance().getReference("User");
        user_account=FirebaseDatabase.getInstance().getReference("Users");
        user_account.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                     fname=dataSnapshot1.child("fname").getValue().toString();
                     lname=dataSnapshot1.child("lname").getValue().toString();
                     phone=dataSnapshot1.child("mobile").getValue().toString();
                     photo=dataSnapshot1.child("picture").getValue().toString();
                     uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        geoFire = new GeoFire(drivers);
        setUpCountDown();
        SetUpLocation();
        updateFirebaseToken();
    }

    private void updateFirebaseToken() {
        DatabaseReference  firebaseDatabase=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(FirebaseInstanceId.getInstance().getToken());
        firebaseDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
    private void RequestDriver(final String driverId) {
        final UserAccount userAccount=new UserAccount();
        userAccount.setFname(fname);
        userAccount.setLname(lname);
        userAccount.setMobile(phone);
        userAccount.setLatitude(location.getLatitude());
        userAccount.setLongitude(location.getLongitude());
        userAccount.setPicture(photo);
        userAccount.setUid(uid);
        userAccount.setEmail(job_description);
        final DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        tokens.orderByKey().equalTo(driverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Token token=dataSnapshot1.getValue(Token.class);
                            String json_lat_lng = new Gson().toJson(userAccount);
                            String userToken=FirebaseInstanceId.getInstance().getToken();
                            Notification data = new Notification(userToken, json_lat_lng);
                            Sender content = new Sender(token.getToken(), data);
                            mservice.sendMessage(content)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<FCMResponse> call, @NonNull Response<FCMResponse> response) {
                                            if (response.body().success == 1) {
                                                provider_layout.setVisibility(View.INVISIBLE);
                                                Toast.makeText(ServiceProMapActivity.this, "Request Send Successfully  You will be notified with in 30 seconds with the response of service provider", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ServiceProMapActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setUpCountDown() {
        mCountDownTimer = new CountDownTimer(MAX_GPS_SEARCH_SECONDS * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (mLocationFound) {
                    mCountDownTimer.cancel();
                }

            }

            public void onFinish() {
                if (!mLocationFound) {
                    removeLocationUpdates();
                }
            }
        };
    }

    private void removeLocationUpdates() {
        Log.d("Location Listener", "Removed");
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case My_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        createLocationRequest();
                        DisplayLocation();
                    }
                }
        }
    private void SetUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, My_PERMISSION_REQUEST_CODE);
        } else {
                buildLocationCallback();
                createLocationRequest();
                DisplayLocation();
            }
        }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                DisplayLocation();
            }
        };
    }
    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    private void StopUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
      fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    private void DisplayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location mlocation) {
                location = mlocation;
                if (location != null) {
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();
                    userLocation(new LatLng(location.getLatitude(),location.getLongitude()));
//                    loadAllDriver();
//                    getClosestDriver();
                }
            }
        });
    }

    private void userLocation(final LatLng location) {
        driverAvailable=FirebaseDatabase.getInstance().getReference("Drivers");
        driverAvailable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findDriver();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(location.latitude,location.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (marker != null)
                    marker.remove();
                marker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title("You"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude),12));
                findDriver();
            }
        });
    }
//    private void loadAllDriver() {
//        mdrivers = FirebaseDatabase.getInstance().getReference("Drivers");
//        GeoFire geoFire = new GeoFire(mdrivers);
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), distance);
//        geoQuery.removeAllListeners();
//        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                                              @Override
//                                              public void onKeyEntered(String key, final GeoLocation location) {
//                                                  mMap.addMarker(new MarkerOptions()
//                                                          .position(new LatLng(location.latitude, location.longitude))
//                                                          .flat(true)
//                                                          .title("Driver")
//                                                          .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_driver_loc_marker)));
//                                                  mMap.setOnMarkerClickListener(ServiceProMapActivity.this);
//            }
//            @Override
//            public void onKeyExited(String key) {
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//                                              }
//
//            @Override
//            public void onGeoQueryReady() {
//                if (distance <= LIMIT) {
//                    distance++;
//                    loadAllDriver();
//                }
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }

    private void findDriver() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position( new LatLng(location.getLatitude(),location.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
                .title("you"));

        mdrivers = FirebaseDatabase.getInstance().getReference("Drivers");
        GeoFire geoFire = new GeoFire(mdrivers);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                filterdata=FirebaseDatabase.getInstance().getReference("Driver");
                filterdata.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            provider = dataSnapshot1.child("serviceId").getValue(String.class);
                            String title = dataSnapshot1.child("firstname").getValue(String.class);
                            img = dataSnapshot1.child("img").getValue(String.class);
                            driverId = key;
                            if (provider.equals(category_name)) {
                                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
//                                Bitmap bitmap = createBitmap();
//                                if(bitmap!=null) {
                                mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation)
                                        .title(title)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_driver_loc_marker)));
                                mDriverMarker.setTag(key);
                                markers.add(mDriverMarker);
                                mMap.setOnMarkerClickListener(ServiceProMapActivity.this);
                            } else if (!provider.equals(category_name)) {
                                no_provider.setVisibility(View.VISIBLE);
                            } else
                            {
                                    provider_layout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                if (!isDriverFound) {
                    radius++;
                    if(radius<5) {
                        findDriver();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Bitmap createBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.inq);
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            if(img == null) {
             bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dummy_user);
            }
            else {
                URL url = new URL(img);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
                if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    private int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
    @Override
    public boolean onMarkerClick(Marker mymarker) {
        String markerId = (String) mymarker.getTag();
            provider_layout.setVisibility(View.VISIBLE);
            databaseReference=FirebaseDatabase.getInstance().getReference("Driver").child(markerId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Provider provider = dataSnapshot1.getValue(Provider.class);
                        if(provider.getImg()!=null) {
                            Picasso.get()
                                    .load(provider.getImg())
                                    .placeholder(R.drawable.ic_dummy_user)
                                    .error(R.drawable.ic_dummy_user)
                                    .into(provider_img);
                        }else
                        {
                            Picasso.get()
                                    .load(R.drawable.ic_dummy_user)
                                    .placeholder(R.drawable.ic_dummy_user)
                                    .error(R.drawable.ic_dummy_user)
                                    .into(provider_img);
                        }
                        service_name.setText(provider.getServiceId());
                        provider_name.setText(provider.getFirstname() + "" + provider.getLastname());
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
