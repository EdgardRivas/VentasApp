package com.capillasmemoriales.informatica.ventasapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.capillasmemoriales.informatica.ventasapp.R;
import com.capillasmemoriales.informatica.ventasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.ventasapp.controllers.Network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Company extends AppCompatActivity {

    private Boolean btnStatus = false;
    private RadioButton rdbESA, rdbHON, rdbNIC, rdbPjc, rdbBri, rdbCon, rdbSie, rdbPre;
    private Button btnSelect;
    private ProgressDialog dialog;

    private JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_selection);

        rdbESA = findViewById(R.id.rdbESA);
        rdbHON = findViewById(R.id.rdbHON);
        rdbNIC = findViewById(R.id.rdbNIC);
        rdbPjc = findViewById(R.id.rdbPjc);
        rdbBri = findViewById(R.id.rdbBri);
        rdbCon = findViewById(R.id.rdbCon);
        rdbSie = findViewById(R.id.rdbSie);
        rdbPre = findViewById(R.id.rdbPre);

        btnSelect = findViewById(R.id.btnSelect);

        rdbESA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdbESA.isChecked()) {
                    rdbPjc.setEnabled(true);
                    rdbBri.setEnabled(true);
                    rdbCon.setEnabled(false);
                    rdbSie.setEnabled(false);
                    rdbPre.setEnabled(false);
                    uncheckedCompanies();
                }
            }
        });
        rdbHON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdbHON.isChecked()) {
                    rdbPjc.setEnabled(false);
                    rdbBri.setEnabled(false);
                    rdbCon.setEnabled(true);
                    rdbSie.setEnabled(false);
                    rdbPre.setEnabled(false);
                    uncheckedCompanies();
                }
            }
        });
        rdbNIC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdbNIC.isChecked()) {
                    rdbPjc.setEnabled(false);
                    rdbBri.setEnabled(false);
                    rdbCon.setEnabled(false);
                    rdbSie.setEnabled(true);
                    rdbPre.setEnabled(true);
                    uncheckedCompanies();
                }
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdbPjc.isChecked() || rdbBri.isChecked() || rdbCon.isChecked() || rdbSie.isChecked() || rdbPre.isChecked()) {
                    new conx().execute();
                } else {
                    Snackbar.make(view, getString(R.string.select_company), Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uncheckedCompanies() {
        rdbPjc.setChecked(false);
        rdbBri.setChecked(false);
        rdbCon.setChecked(false);
        rdbSie.setChecked(false);
        rdbPre.setChecked(false);
    }

    class conx extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Company.this);
            dialog.setTitle(getString(R.string.company));
            dialog.setMessage(getString(R.string.loading));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String company = "";
            if (rdbPjc.isChecked()) {
                company = "pjc";
            } else if (rdbBri.isChecked()) {
                company = "bri";
            } else if (rdbCon.isChecked()) {
                company = "con";
            } else if (rdbSie.isChecked()) {
                company = "sie";
            } else if (rdbPre.isChecked()) {
                company = "pre";
            }
            List<NameValuePair> params;
            params = new ArrayList<>();
            params.add(new BasicNameValuePair("company", company));

            JSONObject jsonObjectLogin = jsonParser.requestObject(Network.VALIDATE, params);

            return null;
        }

        @SuppressLint("ApplySharedPref")
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    if (!btnStatus) {
                        Intent i = new Intent(getApplicationContext(), Main.class);
                        startActivity(i);
                    }
                }
            }, Network.DURATION);
        }
    }

    public void onBackPressed(){
        btnStatus = true;
        super.onBackPressed();
    }
}