package com.generalmobi.gpaydemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.generalmobi.paysdk.dto.PayDialogAttrs;
import com.generalmobi.paysdk.dto.PayReqParams;
import com.generalmobi.paysdk.dto.PayResParams;
import com.generalmobi.paysdk.utils.PayPGService;
import com.generalmobi.paysdk.utils.PayTransactionCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String partnerCode = ((EditText)findViewById(R.id.partnerCode)).getText().toString();
                final String ptxId = ((EditText)findViewById(R.id.ptxId)).getText().toString();
                final String appName = ((EditText)findViewById(R.id.appName)).getText().toString();
                final String amount = ((EditText)findViewById(R.id.amount)).getText().toString();
                final String imei = ((EditText)findViewById(R.id.imei)).getText().toString();
                final String secretKey = ((EditText)findViewById(R.id.secretKey)).getText().toString();

                // PayReqParams object creation
                PayReqParams payReqParams = new PayReqParams();
                payReqParams.setPartnerCode(partnerCode);           // Partner code provided by GMobi(mandatory)
                payReqParams.setAppName(appName);                   // Application name (mandatory)
                payReqParams.setPtxid(ptxId);                       // Transaction Id (mandatory)
                payReqParams.setAmount(amount);                     // Transaction amount as integer only (mandatory)
                payReqParams.setImeiIp(imei);                       // Device IMEI number (mandatory)
                payReqParams.setSecretKey(secretKey);               // Secret key provided by GMobi (mandatory)
                payReqParams.setAttributes(new PayDialogAttrs());   // Pay dialog attributes (optional)

                PayPGService Service = null;

                // Getting the PayPGService Instance. PayPGService.getStagingService() will
                // return the service pointing to staging environment.
                Service = PayPGService.getStagingService();

                //TODO use either of both as per requirement

                // Getting the PayPGService Instance. PayPGService.getProductionService() will
                // return the service pointing to production environment.
//                Service = PayPGService.getProductionService();

                // Call this method and set PayReqParams object before starting transaction.
                Service.initialize(payReqParams);

                //Start the Payment Transaction. Before starting the transaction ensure that
                // initialize method is called.
                Service.startTransaction(MainActivity.this, new PayTransactionCallback() {
                    @Override
                    public void onTransactionResponse(PayResParams payResParams) {
                        Log.e("MainActivity", "pay_response --> " + payResParams.toString());
                        try {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            mBuilder.setTitle("Response");
                            mBuilder.setMessage(payResParams.toString());
                            mBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            mBuilder.create().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "Back Pressed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage) {
                        Toast.makeText(MainActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
