package com.capillasmemoriales.informatica.ventasapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.capillasmemoriales.informatica.ventasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.ventasapp.controllers.Network;
import com.capillasmemoriales.informatica.ventasapp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactDetail extends AppCompatActivity {

    public static Boolean btnStatus = false;

    private TextView txtId, txtFirstName, txtLastName, txtPhone, txtMail;
    private Button btnEdit, btnSave;

    private JSONParser jsonParser = new JSONParser();

    private int response_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        int Id = Objects.requireNonNull(getIntent().getExtras()).getInt("Id");
        String fName = getIntent().getExtras().getString("fName");
        String lName = getIntent().getExtras().getString("lName");
        String Phone = getIntent().getExtras().getString("Phone");
        String Mail = getIntent().getExtras().getString("Mail");

        txtId = findViewById(R.id.txtID);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtPhone = findViewById(R.id.txtPhone);
        txtMail = findViewById(R.id.txtMail);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        txtId.setText(String.valueOf(Id));
        txtFirstName.setText(fName);
        txtLastName.setText(lName);
        txtPhone.setText(Phone);
        txtMail.setText(Mail);

        txtId.setEnabled(false);
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtPhone.setEnabled(false);
        txtMail.setEnabled(false);
        btnSave.setEnabled(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new editContact().execute();
            }
        });
    }

    private void edit() {
        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtPhone.setEnabled(true);
        txtMail.setEnabled(true);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
    }

    public boolean isValidMail(String mail) {
        boolean isValid = false;
        if (mail.contains("@") && mail.contains(".com"))
            isValid = true;
        return isValid;
    }

    @SuppressLint("StaticFieldLeak")
    class editContact extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String Id = txtId.getText().toString();
            String fName = txtFirstName.getText().toString();
            String lName = txtLastName.getText().toString();
            String Phone = txtPhone.getText().toString();
            String Mail = txtMail.getText().toString();
            if (!isValidMail(txtMail.getText().toString())) {
                return getString(R.string.eMail);
            }
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("codprospecto", Id));
                params.add(new BasicNameValuePair("fName", fName));
                params.add(new BasicNameValuePair("lName", lName));
                params.add(new BasicNameValuePair("phone", Phone));
                params.add(new BasicNameValuePair("mail", Mail));

                JSONObject jsonObject = jsonParser.requestObject(Network.EDIT_CONTACT, params);

                response_id = jsonObject.getInt("response_id");

                return jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (file_url != null)
                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            intentMain();
        }
    }

    private void intentMain() {
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
