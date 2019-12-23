package com.skilex.serviceprovider.activity.fragmentactivity.ongoing;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.bean.support.OngoingService;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class InitiatedServiceActivity extends FragmentActivity implements OnMapReadyCallback, IServiceListener, DialogClickListener, View.OnClickListener {


    private static final int REQUEST_PHONE_CALL = 1;
    Intent callIntent = new Intent(Intent.ACTION_CALL);

    private static final String TAG = InitiatedServiceActivity.class.getName();
    private MapView mapView;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    String user;
    LatLng livLoc;
    Marker currentLocationMarker;
    private Handler handler = new Handler();
    OngoingService ongoingService;
    private TextView cusName, cusPhone, cusAddress;
    private ImageView imgCall;
    private Button btnTrack;
    private TextView catName, subCatName, custName, servicedate, orderID, serviceProvider, servicePerson, servicePersonPhone,
            serviceStartTime, estimatedCost;
    private RelativeLayout nameLay;

    private String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiated_service_detail);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        ongoingService = (OngoingService) getIntent().getSerializableExtra("serviceObj");

        cusName = findViewById(R.id.txt_customer_name);
        cusPhone = findViewById(R.id.txt_customer_number);
        cusAddress = findViewById(R.id.txt_address);

//        imgCall = findViewById(R.id.img_call_button);
//        imgCall.setOnClickListener(this);

        nameLay = findViewById(R.id.name_layout);
        nameLay.setOnClickListener(this);

        btnTrack = findViewById(R.id.btn_track);
        btnTrack.setOnClickListener(this);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        startTimer();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

//        livLoc = getIntent().getParcelableExtra("dist");


        mapView = findViewById(R.id.map);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
//        checkProviderAssign();
        loadServiceDetails();

    }

    void loadServiceDetails() {
        res = "serviceDetail";
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = PreferenceStorage.getUserMasterId(this);
        try {
            jsonObject.put(SkilExConstants.USER_MASTER_ID, id);
            jsonObject.put(SkilExConstants.SERVICE_ORDER_ID, ongoingService.getServiceOrderId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = SkilExConstants.BUILD_URL + SkilExConstants.API_INITIATED_SERVICE_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
//        String lat = String.valueOf(livLoc.latitude);
//        String lng = String.valueOf(livLoc.longitude);
//        if (!lat.isEmpty() && !lng.isEmpty()) {
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.addMarker(new MarkerOptions()
//                .position(ny)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SkilExConstants.PARAM_MESSAGE);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                JSONArray getData = response.getJSONArray("detail_services_order");
                Gson gson = new Gson();
                JSONObject getServiceData = getData.getJSONObject(0);

                cusName.setText(getServiceData.getString("contact_person_name"));
                cusPhone.setText(getServiceData.getString("contact_person_number"));
                cusAddress.setText(getServiceData.getString("status"));

                String string = getServiceData.getString("service_location");
                if (!string.isEmpty()) {
                    String[] parts = string.split(",");
                    String part1 = parts[0]; // lat
                    String part2 = parts[1]; // long
                    Double lat = Double.valueOf(part1);
                    Double longi = Double.valueOf(part2);
                    LatLng latLng = new LatLng(lat, longi);
                    showMarker(latLng);
                } else {
//                    AlertDialogHelper.showSimpleAlertDialog(this, "Location unavailable");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    private void animateCamera(@NonNull LatLng latLng) {
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    private void showMarker(@NonNull LatLng latLng) {
//        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        int height = 60;
        int width = 30;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.commando);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        if (currentLocationMarker == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            currentLocationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).position(latLng));
            mapView.onResume();
        } else {
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == nameLay) {
            callNumber();
        }
    }

    public void callNumber() {

        callIntent.setData(Uri.parse("tel:" + cusPhone.getText().toString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(InitiatedServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(InitiatedServiceActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else
            {
                startActivity(callIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
                else
                {

                }
                return;
            }
        }
    }

    public static class MarkerAnimation {
        public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
            final LatLng startPosition = marker.getPosition();
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final Interpolator interpolator = new AccelerateDecelerateInterpolator();
            final float durationInMs = 2000;
            handler.post(new Runnable() {
                long elapsed;
                float t;
                float v;

                @Override
                public void run() {
                    // Calculate progress using interpolator
                    elapsed = SystemClock.uptimeMillis() - start;
                    t = elapsed / durationInMs;
                    v = interpolator.getInterpolation(t);
                    marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                    // Repeat till progress is complete.
                    if (t < 1) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
    }

    public interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class Spherical implements LatLngInterpolator {
            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to) {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);
                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6) {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;
                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);
                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }

            private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
                // Haversine's formula
                double dLat = fromLat - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }
    }
}
