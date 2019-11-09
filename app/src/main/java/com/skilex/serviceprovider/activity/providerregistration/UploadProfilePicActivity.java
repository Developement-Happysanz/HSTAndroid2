package com.skilex.serviceprovider.activity.providerregistration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.activity.loginmodule.OTPVerificationActivity;
import com.skilex.serviceprovider.activity.loginmodule.RegisterActivity;
import com.skilex.serviceprovider.activity.serviceperson.ServicePersonDetailInfoActivity;
import com.skilex.serviceprovider.customview.CircleImageView;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.AndroidMultiPartEntity;
import com.skilex.serviceprovider.utils.CommonUtils;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadProfilePicActivity extends BaseActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private Button btnSubmit;

    private CircleImageView profilePic;
    private Uri outputFileUri;
    private File file;
    private File sourceFile;
    private File destFile;
    static final int REQUEST_IMAGE_GET = 1;
    static final int CROP_PIC = 2;
    private String mActualFilePath = null;
    private Uri mSelectedImageUri = null;
    private Bitmap mCurrentUserImageBitmap = null;
    private ProgressDialog mProgressDialog = null;
    private String mUpdatedImageUrl = null;
    private SimpleDateFormat dateFormatter;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String IMAGE_DIRECTORY = "ImageScalling";

    private String checkProviderAndPerson = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        profilePic = (CircleImageView) findViewById(R.id.img);
        profilePic.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            checkProviderAndPerson = extras.getString("ProviderPersonCheck");
            //The key argument here must match that used in the other activity
        }

        if (checkProviderAndPerson.equalsIgnoreCase("Provider")) {
            String url = PreferenceStorage.getProfilePicture(this);
            if (((url != null) && !(url.isEmpty()))) {
                Picasso.get().load(url).into(profilePic);
            } else {
                profilePic.setImageResource(R.drawable.ic_profile);
            }
        }

        dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);


    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.haveNetworkConnection(getApplicationContext())) {
            if (v == profilePic) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(UploadProfilePicActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(UploadProfilePicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(UploadProfilePicActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                        openImageIntent();
                    }
                }
            } else if (v == btnSubmit) {
                Intent i = new Intent(UploadProfilePicActivity.this, CategorySelectionActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("ProviderPersonCheck", checkProviderAndPerson);
                startActivity(i);
                finish();
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private void openImageIntent() {

// Determine Uri of camera image to save.
        File pictureFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        final File root = new File(pictureFolder, "SkilExImages");
//        final File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyDir");

        if (!root.exists()) {
            if (!root.mkdirs()) {
                Log.d(TAG, "Failed to create directory for storing images");
                return;
            }
        }
        Calendar newCalendar = Calendar.getInstance();
        int month = newCalendar.get(Calendar.MONTH) + 1;
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int year = newCalendar.get(Calendar.YEAR);
        int hours = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = newCalendar.get(Calendar.MINUTE);
        int seconds = newCalendar.get(Calendar.SECOND);
        final String fname = PreferenceStorage.getUserMasterId(this) + "_" + day + "_" + month + "_" + year + "_" + hours + "_" + minutes + "_" + seconds + ".png";
        final File sdImageMainDirectory = new File(root.getPath() + File.separator + fname);
        destFile = sdImageMainDirectory;
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        Log.d(TAG, "camera output Uri" + outputFileUri);

        // Camera.
        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Profile Photo");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, REQUEST_IMAGE_GET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_GET) {
                Log.d(TAG, "ONActivity Result");
                final boolean isCamera;
                if (data == null) {
                    Log.d(TAG, "camera is true");
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    Log.d(TAG, "camera action is" + action);
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }


                if (isCamera) {
                    Log.d(TAG, "Add to gallery");
                    mSelectedImageUri = outputFileUri;
                    mActualFilePath = outputFileUri.getPath();
                    galleryAddPic(mSelectedImageUri);
                } else {
//                    selectedImageUri = data == null ? null : data.getData();
//                    mActualFilePath = getRealPathFromURI(this, selectedImageUri);
//                    Log.d(TAG, "path to image is" + mActualFilePath);

                    if (data != null && data.getData() != null) {
                        try {
                            mSelectedImageUri = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(mSelectedImageUri,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            mActualFilePath = getRealPathFromURI(this, mSelectedImageUri);
                            cursor.close();
                            File f1 = new File(mActualFilePath);
                            mCurrentUserImageBitmap = decodeFile(f1);
                            //return Image Path to the Main Activity
                            Intent returnFromGalleryIntent = new Intent();
                            returnFromGalleryIntent.putExtra("picturePath", mActualFilePath);

                            setResult(RESULT_OK, returnFromGalleryIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent returnFromGalleryIntent = new Intent();
                            setResult(RESULT_CANCELED, returnFromGalleryIntent);
                            finish();
                        }
                    } else {
                        Log.i(TAG, "RESULT_CANCELED");
                        Intent returnFromGalleryIntent = new Intent();
                        setResult(RESULT_CANCELED, returnFromGalleryIntent);
                        finish();
                    }

                }
                Log.d(TAG, "image Uri is" + mSelectedImageUri);
                if (mSelectedImageUri != null) {
                    Log.d(TAG, "image URI is" + mSelectedImageUri);
//                    performCrop();
//                    setPic(mSelectedImageUri);
                    mUpdatedImageUrl = null;
                    mCurrentUserImageBitmap = decodeFile(destFile);
                    new UploadFileToServer().execute();
                }
            }

        }
    }

    private Bitmap decodeFile(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        destFile = new File(file, "img_"
                + dateFormatter.format(new Date()).toString() + ".png");
        mActualFilePath = destFile.getPath();
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private static final String TAG = "UploadFileToServer";
        private HttpClient httpclient;
        HttpPost httppost;
        public boolean isTaskAborted = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            String userId = "";
            if (checkProviderAndPerson.equalsIgnoreCase("Provider")) {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(String.format(SkilExConstants.BUILD_URL + SkilExConstants.UPLOAD_IMAGE + Integer.parseInt(PreferenceStorage.getUserMasterId(getApplicationContext())) + "/"));
            } else {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(String.format(SkilExConstants.PERSON_BUILD_URL + SkilExConstants.UPLOAD_IMAGE + Integer.parseInt(PreferenceStorage.getServicePersonId(getApplicationContext())) + "/"));
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });
                Log.d(TAG, "actual file path is" + mActualFilePath);
                if (mActualFilePath != null) {

                    File sourceFile = new File(mActualFilePath);

                    // Adding file data to http body
                    //fileToUpload
                    entity.addPart("profile_pic", new FileBody(sourceFile));

                    // Extra parameters if you want to pass to server
                    entity.addPart("user_id", new StringBody(PreferenceStorage.getUserMasterId(UploadProfilePicActivity.this)));
//                    entity.addPart("user_type", new StringBody(PreferenceStorage.getUserType(ProfileActivity.this)));

//                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                        try {
                            JSONObject resp = new JSONObject(responseString);
                            String successVal = resp.getString("status");

                            mUpdatedImageUrl = resp.getString("picture_url");
                            if (mUpdatedImageUrl != null) {
                                if (checkProviderAndPerson.equalsIgnoreCase("Provider")) {
                                    PreferenceStorage.saveProfilePicture(UploadProfilePicActivity.this, mUpdatedImageUrl);
                                }
                            }
                            Log.d(TAG, "updated image url is" + mUpdatedImageUrl);
                            if (successVal.equalsIgnoreCase("success")) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            progressDialogHelper.hideProgressDialog();

            super.onPostExecute(result);
            if ((result == null) || (result.isEmpty()) || (result.contains("Error"))) {
                if (((mUpdatedImageUrl != null) && !(mUpdatedImageUrl.isEmpty()))) {
                    Picasso.get().load(mUpdatedImageUrl).into(profilePic);
                } else {
                    profilePic.setImageResource(R.drawable.ic_profile);
                }
                Toast.makeText(UploadProfilePicActivity.this, "Unable to upload picture", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UploadProfilePicActivity.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                if (checkProviderAndPerson.equalsIgnoreCase("Provider")) {
                    if (((mUpdatedImageUrl != null) && !(mUpdatedImageUrl.isEmpty()))) {
                        Picasso.get().load(mUpdatedImageUrl).into(profilePic);
                    } else {
                        profilePic.setImageResource(R.drawable.ic_profile);
                    }
                    btnSubmit.setVisibility(View.VISIBLE);
                } else {
//                    Intent i = new Intent(UploadProfilePicActivity.this, CategorySelectionActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.putExtra("ProviderPersonCheck", "Person");
//                    startActivity(i);
//                    finish();
                    Intent i = new Intent(getApplicationContext(), ServicePersonDetailInfoActivity.class);
                    startActivity(i);
                    finish();
                }


//                finish();
//                startActivity(getIntent());
            }
//            saveProfileData();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void galleryAddPic(Uri urirequest) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(urirequest.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String result = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);

            Cursor cursor = loader.loadInBackground();
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                cursor.close();
            } else {
                Log.d(TAG, "cursor is null");
            }
        } catch (Exception e) {
            result = null;
            Toast.makeText(this, "Was unable to save  image", Toast.LENGTH_SHORT).show();

        } finally {
            return result;
        }
    }

    private void sendSuccessMessage() {
        Log.d(TAG, "Updated image succesfully");
        Toast.makeText(getApplicationContext(), "Upload succesful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
