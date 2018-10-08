package com.capillasmemoriales.informatica.ventasapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.capillasmemoriales.informatica.ventasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.ventasapp.models.DataBase;
import com.capillasmemoriales.informatica.ventasapp.controllers.Network;
import com.capillasmemoriales.informatica.ventasapp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    public static Boolean btnStatus = false;
    //public static int red, green;

    private EditText txtMail, txtPass;
    private CheckBox chbRem;
    private Button btnLogin;
    private ProgressDialog dialog;

    private int response_id;
    public static int user_id, cod_emp, company_id;
    public static String user_name;

    private String mail, pass, passEncode;

    private JSONParser jsonParser = new JSONParser();

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtMail = findViewById(R.id.txtMail);
        txtPass = findViewById(R.id.txtPass);
        chbRem = findViewById(R.id.chbRem);
        btnLogin = findViewById(R.id.btnLogin);

        //red = getResources().getColor(R.color.cError);
        //green = getResources().getColor(R.color.cOk);

        //txtMail.setTextColor(green);
        //txtPass.setTextColor(green);

        getPreferences();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mail = txtMail.getText().toString();
                pass = txtPass.getText().toString();
                if (Network.isOnline(getApplicationContext())) {
                    if (isValidMail(mail)) {
                        //txtMail.setTextColor(green);
                        if (pass.length() > 5) /*7*/ {
                            //txtPass.setTextColor(green);
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(txtPass.getWindowToken(), 0);
                            new login().execute();
                        } else {
                            //txtPass.setTextColor(red);
                            txtPass.setError(getString(R.string.ePass));
                        }
                    } else {
                        //txtMail.setTextColor(red);
                        txtMail.setError(getString(R.string.eMail));
                    }
                } else {
                    loginLocal();
                    //openNetworkSettings(view);
                }
            }
        });
    }

    private void getPreferences() {
        loginPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        if (loginPreferences.contains("user_mail"))
            txtMail.setText(loginPreferences.getString("user_mail", ""));
        else
            txtMail.requestFocus();
        if (loginPreferences.contains("user_pass")) {
            byte[] data = Base64.decode(loginPreferences.getString("user_pass", ""), Base64.DEFAULT);
            String passDecode = new String(data, StandardCharsets.UTF_8);
            txtPass.setText(passDecode);
        } else
            txtPass.requestFocus();
    }

    private void deletePreferences() {
        loginPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        loginPreferences.edit().clear().apply();
    }

    public boolean isValidMail(String mail) {
        boolean isValid = false;
        if (mail.contains("@") && mail.contains(".com"))
            isValid = true;
        return isValid;
    }

    private void openNetworkSettings(View view) {
        Snackbar.make(view, getString(R.string.no_network), Snackbar.LENGTH_LONG).setAction(getString(R.string.open_network_settings), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        }).setDuration(Snackbar.LENGTH_INDEFINITE).show();
    }

    private void loginLocal() {
        DataBase DB = new DataBase(getApplicationContext());
        SQLiteDatabase sqlDB = DB.getReadableDatabase();
        byte[] data = pass.getBytes(StandardCharsets.UTF_8);
        passEncode = Base64.encodeToString(data, Base64.DEFAULT);
        @SuppressLint("Recycle")
        Cursor cursorLogin = sqlDB.rawQuery("SELECT * FROM seg_usuarios_local WHERE email = ? and cl_user = ?", new String [] {String.valueOf(mail),String.valueOf(passEncode)});
        if (cursorLogin.moveToFirst()) {
            user_id = cursorLogin.getInt(0);
            user_name = String.valueOf(cursorLogin.getString(1));
            intentMain();
        } else
            Toast.makeText(getApplication(), getString(R.string.error), Toast.LENGTH_LONG).show();
        sqlDB.close();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login.this);
            dialog.setTitle(getString(R.string.login));
            dialog.setMessage(getString(R.string.loading));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params;
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("user_mail", mail));
                params.add(new BasicNameValuePair("user_pass", pass));

                JSONObject jsonObjectLogin = jsonParser.requestObject(Network.LOGIN, params);

                user_id = jsonObjectLogin.getInt("user_id");
                cod_emp = jsonObjectLogin.getInt("cod_emp");
                user_name = jsonObjectLogin.getString("user_name");
                response_id = jsonObjectLogin.getInt("response_id");

                return jsonObjectLogin.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null; //Check
        }

        @SuppressLint("ApplySharedPref")
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (response_id != 1) /*Error Login*/ {
                dialog.setTitle(getString(R.string.alert));
                dialog.setMessage(getString(R.string.error_login));
                dialog.setCancelable(true);
            } else /*OK Login*/ {
                if (chbRem.isChecked()) {
                    //Save preferences
                    loginPreferences = getApplicationContext().getSharedPreferences("loginPreferences", 0);
                    editor = loginPreferences.edit();
                    editor.putString("user_mail", mail);
                    byte[] data = pass.getBytes(StandardCharsets.UTF_8);
                    passEncode = Base64.encodeToString(data, Base64.DEFAULT);
                    //editor.putString("user_pass", passEncode);
                    editor.putInt("company_id", company_id);
                    editor.commit();
                    //Save to local DB
                    DataBase DB = new DataBase(getApplicationContext());
                    SQLiteDatabase sqlDB = DB.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("codempleado", user_id);
                    cv.put("usuario", user_name);
                    cv.put("email", mail);
                    cv.put("cl_user", passEncode);
                    cv.put("codempresa", cod_emp);
                    sqlDB.insert("seg_usuarios_local", null, cv);
                    sqlDB.close();
                } else {
                    deletePreferences();
                }
                if (file_url != null) /*URL OK*/ {
                    dialog.setTitle(getString(R.string.welcome));
                    dialog.setMessage(user_name);
                    dialog.setCancelable(true);
                    intentMain();
                }
            }
            dialog.show();
        }
    }

    public void onBackPressed() {
        btnStatus = true;
        super.onBackPressed();
    }
}