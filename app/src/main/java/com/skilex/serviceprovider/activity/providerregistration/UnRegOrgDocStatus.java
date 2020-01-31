package com.skilex.serviceprovider.activity.providerregistration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skilex.serviceprovider.R;
import com.skilex.serviceprovider.bean.support.StoreMasterId;
import com.skilex.serviceprovider.helper.AlertDialogHelper;
import com.skilex.serviceprovider.helper.ProgressDialogHelper;
import com.skilex.serviceprovider.interfaces.DialogClickListener;
import com.skilex.serviceprovider.languagesupport.BaseActivity;
import com.skilex.serviceprovider.servicehelpers.ServiceHelper;
import com.skilex.serviceprovider.serviceinterfaces.IServiceListener;
import com.skilex.serviceprovider.utils.CommonUtils;
import com.skilex.serviceprovider.utils.FilePath;
import com.skilex.serviceprovider.utils.PreferenceStorage;
import com.skilex.serviceprovider.utils.SkilExConstants;
import com.skilex.serviceprovider.utils.SkilExValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class UnRegOrgDocStatus extends BaseActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = UnRegOrgDocStatus.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private Button btnClose;
    private TextView docOne, docTwo, docThree, aadharReject, addressReject, bankReject, spnIdProofType1dsp;
    private EditText aadharCard, AddressProof, bankName, bankAcc, bankIFSC, bankBranck;
    private Spinner spnIdProofType1;
    String aadharId, bankID, addressID;
    String doc_id_send = "";
    private String storeDocumentNumber = "";
    private String storeDocumentMasterId = "";
    private String checkValue = "";
    private int flag = 1;
    private String spinnerValue1 = "";
    private String spinnerValue2 = "";
    private TextView txtAlsoServicePerson, txtTerms;
    private RadioGroup rdgIndividualType, rdgAlsoServicePerson;
    private RadioButton rdbIndividual, rdbUnRegOrg, rdbYes, rdbNo;
    private String anyPoliceCaseRecord = "N";

    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath;
    File sizeCge;
    ProgressDialog dialog;

    boolean doubleBackToExitPressedOnce = false;
    ArrayList<StoreMasterId> docNumberList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_reg_doc_stauts);

        docOne = findViewById(R.id.txtUploadPanCard);
        docOne.setOnClickListener(this);
        docTwo = findViewById(R.id.txtUploadProof1);
        docTwo.setOnClickListener(this);
        docThree = findViewById(R.id.txtUploadPassBook);
        docThree.setOnClickListener(this);

        spnIdProofType1dsp = findViewById(R.id.spnIdProofType1dsp);
        spnIdProofType1dsp.setVisibility(View.GONE);

        spnIdProofType1 = findViewById(R.id.spnIdProofType1);
        spnIdProofType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StoreMasterId classList = (StoreMasterId) parent.getSelectedItem();
                spinnerValue1 = classList.getDocId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        aadharCard = findViewById(R.id.edtPanCardNo);
        AddressProof = findViewById(R.id.edtProof1);
        bankName = findViewById(R.id.edtBankName);
        bankAcc = findViewById(R.id.edtBankAccNo);
        bankIFSC = findViewById(R.id.edtBankIFSC);
        bankBranck = findViewById(R.id.edtBankBranch);
        aadharReject = findViewById(R.id.aadhar_reject);
        aadharReject.setOnClickListener(this);
        addressReject = findViewById(R.id.address_reject);
        addressReject.setOnClickListener(this);
        bankReject = findViewById(R.id.bank_reject);
        bankReject.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        getIdProofType();


    }

    private void checkDocumentStatus() {
        checkValue = "doc_status";
        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SkilExConstants.USER_MASTER_ID, PreferenceStorage.getUserMasterId(getApplicationContext()));
//                jsonObject.put(SkilExConstants.USER_MASTER_ID, "60");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SkilExConstants.BUILD_URL + SkilExConstants.API_PROVIDER_DOCUMENT_STATUS;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        }
    }

    private void getRejectData(String idData) {
        checkValue = "reject";

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SkilExConstants.DOC_ID, idData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SkilExConstants.BUILD_URL + SkilExConstants.API_REJECT_REASON;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        }
    }

    private void getIdProofType() {

        checkValue = "IdProof1";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SkilExConstants.KEY_COMPANY_TYPE, "Individual");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SkilExConstants.BUILD_URL + SkilExConstants.ID_PROOF_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == docOne) {
            if (validateFields()) {
                doc_id_send = aadharId;
                storeDocumentNumber = aadharCard.getText().toString();
                storeDocumentMasterId = "3";
                showFileChooser();
            }
        }
        if (v == docTwo) {
            if (validateFields()) {
                doc_id_send = addressID;
                storeDocumentMasterId = spinnerValue1;
                storeDocumentNumber = AddressProof.getText().toString();
                showFileChooser();
            }
        }
        if (v == docThree) {
            if (validateFields()) {
                doc_id_send = bankID;
                storeDocumentNumber = "PassBook";
                storeDocumentMasterId = "22";
                showFileChooser();
            }
        }
        if (v == aadharReject) {
            getRejectData(aadharId);
        }
        if (v == addressReject) {
            getRejectData(addressID);
        }
        if (v == bankReject) {
            getRejectData(bankID);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose file to upload.."), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    sizeCge = new File(selectedFilePath);
                    if (sizeCge.length() >= 12000000) {
                        AlertDialogHelper.showSimpleAlertDialog(this, "File size too large. File should be at least 12MB");
                        selectedFilePath = null;
                    } else {
                        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
                        dialog = ProgressDialog.show(UnRegOrgDocStatus.this, "", "Uploading File...", true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
//                        uploadFile(selectedFilePath);
                                new UnRegOrgDocStatus.PostDataAsyncTask().execute();
                            }
                        }).start();
                    }
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class PostDataAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        //android upload file to server
        private String uploadFile() {

            int serverResponseCode = 0;
            String serverResponseMessage = null;
            HttpURLConnection connection;
            DataOutputStream dataOutputStream;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 3 * 1024 * 1024;
            File selectedFile = new File(selectedFilePath);
            double len = selectedFile.length();

            String[] parts = selectedFilePath.split("/");
            final String fileName = parts[parts.length - 1];

            if (!selectedFile.isFile()) {
                dialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    }
                });
                return "";
            } else {
                try {
                    String id = PreferenceStorage.getUserMasterId(getApplicationContext());
//                    String id = "118";
                    String document_master_id = storeDocumentMasterId;
                    String document_proof_number = storeDocumentNumber;

                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    String SERVER_URL = SkilExConstants.BUILD_URL + SkilExConstants.REUPLOAD_DOCUMENT + "" + id + "/" + document_master_id + "/" + document_proof_number + "/" + doc_id_send + "/";
                    URI uri = new URI(SERVER_URL.replace(" ", "%20"));
                    String baseURL = uri.toString();
                    URL url = new URL(baseURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);//Don't use a cached Copy
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("doc_file", selectedFilePath);
//                    connection.setRequestProperty("user_id", id);
//                    connection.setRequestProperty("doc_name", title);
//                    connection.setRequestProperty("doc_month_year", start);

                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    //writing bytes to data outputstream
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document_file\";filename=\""
                            + selectedFilePath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    serverResponseMessage = connection.getResponseMessage();

                    Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
//                                tvFileName.setText("File Upload completed.\n\n"+ fileName);
                            }
                        });
                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "URL error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                return serverResponseMessage;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            progressDialogHelper.hideProgressDialog();

            super.onPostExecute(result);
            if ((result.contains("OK"))) {
                Toast.makeText(getApplicationContext(), "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
//                if (flag == 1) {
//                    aadharCard.setEnabled(false);
//                    aadharCard.setFocusable(false);
//                    docOne.setText("");
//                    docOne.setEnabled(false);
//                    docOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uploded, 0);
//                    flag = 2;
//                } else if (flag == 2) {
//                    AddressProof.setEnabled(false);
//                    AddressProof.setFocusable(false);
//                    spnIdProofType1.setEnabled(false);
//                    docTwo.setText("");
//                    docTwo.setEnabled(false);
//                    docTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uploded, 0);
//                    flag = 3;
//                }
////                else if (flag == 3) {
////                    edtProofNo2.setEnabled(false);
////                    edtProofNo2.setFocusable(false);
////                    spnIdProofType2.setEnabled(false);
////                    txtUploadProof2.setText("");
////                    txtUploadProof2.setEnabled(false);
////                    txtUploadProof2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uploded, 0);
////                    flag = 4;
////                }
//                else if (flag == 3) {
//                    bankName.setEnabled(false);
//                    bankBranck.setEnabled(false);
//                    bankAcc.setEnabled(false);
//                    bankIFSC.setEnabled(false);
//                    docThree.setText("");
//                    docThree.setEnabled(false);
//                    docThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uploded, 0);
//                    flag = 4;
//                }
            } else {
                Toast.makeText(getApplicationContext(), "Unable to upload file", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private boolean validateFields() {

        if (flag == 1) {
            if (!SkilExValidator.checkNullString(this.aadharCard.getText().toString().trim())) {
                aadharCard.setError(getString(R.string.empty_entry));
                requestFocus(aadharCard);
                return false;
            } else if (!SkilExValidator.checkAadhaarCardLength(this.aadharCard.getText().toString().trim())) {
                aadharCard.setError(getString(R.string.error_aadhaar));
                requestFocus(aadharCard);
                return false;
            }
        }
        if (flag == 2) {
            if (!SkilExValidator.checkNullString(this.AddressProof.getText().toString().trim())) {
                AddressProof.setError(getString(R.string.empty_entry));
                requestFocus(AddressProof);
                return false;
            }
        }
        /*if (flag == 3) {
            if (!SkilExValidator.checkNullString(this.edtProofNo2.getText().toString().trim())) {
                edtProofNo2.setError(getString(R.string.empty_entry));
                requestFocus(edtProofNo2);
                return false;
            }
        }*/
        if (flag == 3) {
            if (!SkilExValidator.checkNullString(this.bankName.getText().toString().trim())) {
                bankName.setError(getString(R.string.empty_entry));
                requestFocus(bankName);
                return false;
            } else if (!SkilExValidator.checkNullString(this.bankAcc.getText().toString().trim())) {
                bankAcc.setError(getString(R.string.empty_entry));
                requestFocus(bankAcc);
                return false;
            }
//            else if (!SkilExValidator.checkNullString(this.edtIFSC.getText().toString().trim())) {
//                edtIFSC.setError(getString(R.string.empty_entry));
//                requestFocus(edtIFSC);
//                return false;
//            }
//            else if (!SkilExValidator.checkNullString(this.edtBranchName.getText().toString().trim())) {
//                edtBranchName.setError(getString(R.string.empty_entry));
//                requestFocus(edtBranchName);
//                return false;
//            }
        } else {
            return true;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SkilExConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
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
        if (validateResponse(response)) {
            try {
                if (checkValue.equalsIgnoreCase("IdProof1")) {

                    JSONArray getData = response.getJSONArray("proof_list");
                    JSONObject userData = getData.getJSONObject(0);
                    int getLength = getData.length();
                    String subjectName = null;
                    Log.d(TAG, "userData dictionary" + userData.toString());

                    String docId = "";
                    String docName = "";

                    for (int i = 0; i < getLength; i++) {

                        docId = getData.getJSONObject(i).getString("id");
                        docName = getData.getJSONObject(i).getString("doc_name");

                        docNumberList.add(new StoreMasterId(docId, docName));
                    }

                    //fill data in spinner
                    ArrayAdapter<StoreMasterId> adapter = new ArrayAdapter<StoreMasterId>(getApplicationContext(), R.layout.spinner_item_ns, docNumberList);
                    spnIdProofType1.setAdapter(adapter);
                    checkDocumentStatus();

                } else if (checkValue.equalsIgnoreCase("doc_status")) {
                    JSONArray docData = response.getJSONArray("document_result");
                    JSONArray bankData = response.getJSONArray("bank_details");

                    bankName.setText(bankData.getJSONObject(0).getString("bank_name"));
                    bankAcc.setText(bankData.getJSONObject(0).getString("bank_acc_no"));
                    bankIFSC.setText(bankData.getJSONObject(0).getString("bank_ifsc_code"));
                    bankBranck.setText(bankData.getJSONObject(0).getString("bank_branch_name"));

                    for (int i = 0; i < docData.length(); i++) {
                        if (docData.getJSONObject(i).getString("doc_name").equalsIgnoreCase("Aadhaar Card")) {
                            String doc_stat = "";
                            String doc_name = "";
                            doc_stat = docData.getJSONObject(i).getString("status");
                            doc_name = docData.getJSONObject(i).getString("doc_proof_number");
                            aadharCard.setText(doc_name);
                            if (doc_stat.equalsIgnoreCase("Approved")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_successful);
                                docOne.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docOne.setText("Approved");
                                docOne.setClickable(false);
                                aadharCard.setClickable(false);
                                aadharCard.setFocusable(false);
//                            docOne.setCompoundDrawables(null, getDrawable(R.drawable.ic_upload_successful),null,null);
                            } else if (doc_stat.equalsIgnoreCase("Rejected")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_falied);
                                docOne.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docOne.setText("Rejected");
                                docOne.setClickable(true);
                                aadharCard.setClickable(true);
                                aadharCard.setFocusable(true);
                                aadharReject.setVisibility(View.VISIBLE);
                                aadharId = docData.getJSONObject(i).getString("id");
                            } else {
                                Drawable top = getResources().getDrawable(R.drawable.ic_uploded);
                                docOne.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docOne.setText("Uploaded");
                                docOne.setClickable(false);
                                aadharCard.setClickable(false);
                                aadharCard.setFocusable(false);
                            }
                        } else if (docData.getJSONObject(i).getString("doc_name").equalsIgnoreCase("Bank Pass Book")) {

                            String doc_stat = "";
                            String doc_name = "";
                            doc_stat = docData.getJSONObject(i).getString("status");
                            doc_name = docData.getJSONObject(i).getString("doc_proof_number");
                            if (doc_stat.equalsIgnoreCase("Approved")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_successful);
                                docThree.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docThree.setText("Approved");
                                docThree.setClickable(false);
                                bankName.setClickable(false);
                                bankAcc.setClickable(false);
                                bankIFSC.setClickable(false);
                                bankBranck.setClickable(false);
                                bankName.setFocusable(false);
                                bankAcc.setFocusable(false);
                                bankIFSC.setFocusable(false);
                                bankBranck.setFocusable(false);
//                            docOne.setCompoundDrawables(null, getDrawable(R.drawable.ic_upload_successful),null,null);
                            } else if (doc_stat.equalsIgnoreCase("Rejected")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_falied);
                                docThree.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docThree.setText("Rejected");
                                docThree.setClickable(true);
                                bankName.setClickable(true);
                                bankAcc.setClickable(true);
                                bankIFSC.setClickable(true);
                                bankBranck.setClickable(true);
                                bankName.setFocusable(true);
                                bankAcc.setFocusable(true);
                                bankIFSC.setFocusable(true);
                                bankBranck.setFocusable(true);
                                bankReject.setVisibility(View.VISIBLE);
                                bankID = docData.getJSONObject(i).getString("id");
                            } else {
                                Drawable top = getResources().getDrawable(R.drawable.ic_uploded);
                                docThree.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docThree.setText("Uploaded");
                                docThree.setClickable(false);
                                bankName.setClickable(false);
                                bankAcc.setClickable(false);
                                bankIFSC.setClickable(false);
                                bankBranck.setClickable(false);
                                bankName.setFocusable(false);
                                bankAcc.setFocusable(false);
                                bankIFSC.setFocusable(false);
                                bankBranck.setFocusable(false);
                            }
                        } else {
                            String doc_stat = "";
                            String doc_name = "";
                            String doc_type = "";
                            doc_stat = docData.getJSONObject(i).getString("status");
                            doc_name = docData.getJSONObject(i).getString("doc_proof_number");
                            doc_type = docData.getJSONObject(i).getString("doc_name");
                            AddressProof.setText(doc_name);
                            if (doc_stat.equalsIgnoreCase("Approved")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_successful);
                                docTwo.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docTwo.setText("Approved");
//                            docOne.setCompoundDrawables(null, getDrawable(R.drawable.ic_upload_successful),null,null);
                                docTwo.setClickable(false);
                                AddressProof.setClickable(false);
                                AddressProof.setFocusable(false);
                                spnIdProofType1.setFocusable(false);
                                spnIdProofType1.setClickable(false);
                                spnIdProofType1.setVisibility(View.GONE);
                                spnIdProofType1dsp.setVisibility(View.VISIBLE);
                                spnIdProofType1dsp.setText(doc_type);

                            } else if (doc_stat.equalsIgnoreCase("Rejected")) {
                                Drawable top = getResources().getDrawable(R.drawable.ic_upload_falied);
                                docTwo.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docTwo.setText("Rejected");
                                docTwo.setClickable(true);
                                AddressProof.setClickable(true);
                                AddressProof.setFocusable(true);
                                
                                spnIdProofType1.setFocusable(true);
                                spnIdProofType1.setClickable(true);
                                addressReject.setVisibility(View.VISIBLE);
                                addressID = docData.getJSONObject(i).getString("id");

                                spnIdProofType1.setVisibility(View.VISIBLE);
                                spnIdProofType1dsp.setVisibility(View.GONE);
                                spnIdProofType1dsp.setText(doc_type);
                            } else {
                                Drawable top = getResources().getDrawable(R.drawable.ic_uploded);
                                docTwo.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                                docTwo.setText("Uploaded");
                                docTwo.setClickable(false);
                                AddressProof.setClickable(false);
                                AddressProof.setFocusable(false);
                                spnIdProofType1.setFocusable(false);
                                spnIdProofType1.setClickable(false);
                                spnIdProofType1.setVisibility(View.GONE);
                                spnIdProofType1dsp.setVisibility(View.VISIBLE);
                                spnIdProofType1dsp.setText(doc_type);
                                
                            }

                        }
                    }
                } else if (checkValue.equalsIgnoreCase("reject")) {
                    String msg = response.getString(SkilExConstants.PARAM_MESSAGE);
                    AlertDialogHelper.showSimpleAlertDialog(this, msg);
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
}
