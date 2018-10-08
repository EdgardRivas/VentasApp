package com.capillasmemoriales.informatica.ventasapp.activities.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.capillasmemoriales.informatica.ventasapp.activities.Login;
import com.capillasmemoriales.informatica.ventasapp.activities.Main;
import com.capillasmemoriales.informatica.ventasapp.adapters.ContactsAdapter;
import com.capillasmemoriales.informatica.ventasapp.models.Contact;
import com.capillasmemoriales.informatica.ventasapp.models.DataBase;
import com.capillasmemoriales.informatica.ventasapp.controllers.JSONParser;
import com.capillasmemoriales.informatica.ventasapp.controllers.Network;
import com.capillasmemoriales.informatica.ventasapp.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ContactsFragment extends Fragment {

    public static Boolean btnStatus = false;

    private List<Contact> listContact;
    private RecyclerView rvContacts;

    //private ListView listContacts; //check
    private static final int CALL = 0;

    private ProgressDialog dialog;

    private JSONParser jsonParser = new JSONParser();

    private int response_id;

    private String addFName, addLName, addPhone, addMail;
    private String deleteId;

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listContact = new ArrayList<>();
        View view = inflater.inflate(R.layout.floating_contacts, container, false);
        //listContacts = view.findViewById(R.id.listContacts); //check
        rvContacts = view.findViewById(R.id.rvContacts);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(llm);

        if (Network.isOnline(Objects.requireNonNull(getContext()))) {
            new getContacts().execute();
        } else {
            getLocalContacts();
        }

        final FloatingActionsMenu options = view.findViewById(R.id.options);
        //FloatingActionButton search = view.findViewById(R.id.search);
        FloatingActionButton add = view.findViewById(R.id.add);
        FloatingActionButton delete = view.findViewById(R.id.delete);
        /*
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //search();
                options.collapse();
            }
        });
        */
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                add();
                options.collapse();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
                options.collapse();
            }
        });
        /*
        listContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                return false;
            }
        });
        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Single click
                //Object obj = adapterView.getItemAtPosition(position);
                //phoneNumber = obj.toString();
                //Toast.makeText(getContext(), phoneNumber, Toast.LENGTH_LONG).show();
            }
        });
        registerForContextMenu(listContacts);
        */
        return view;
    }

    public ContactsAdapter adapter;
    private void initAdapter() {
        adapter = new ContactsAdapter(getContext(), listContact);
        rvContacts.setAdapter(adapter);
    }

    public boolean isValidMail(String mail) {
        boolean isValid = false;
        if (mail.contains("@") && mail.contains(".com"))
            isValid = true;
        return isValid;
    }

    private void search() {
        builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = layoutInflater.inflate(R.layout.modal_search, null);
        //Vars
        final TextView txtSearch = dialogView.findViewById(R.id.txtSearch);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.search_contact));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (Network.isOnline(getContext())) {

                } else {

                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertDialog = this.builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void add() {
        builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = layoutInflater.inflate(R.layout.modal_add_contact, null);
        //Vars
        final TextView txtFirstName = dialogView.findViewById(R.id.txtFirstName);
        final TextView txtLastName = dialogView.findViewById(R.id.txtLastName);
        final TextView txtPhone = dialogView.findViewById(R.id.txtPhone);
        final TextView txtMail = dialogView.findViewById(R.id.txtMail);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.add_contact));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addFName = txtFirstName.getText().toString().trim();
                addLName = txtLastName.getText().toString().trim();
                addPhone = txtPhone.getText().toString().trim();
                addMail = txtMail.getText().toString().trim();
                if (Network.isOnline(getContext()))
                    if (!addFName.equals("") && !addLName.equals("") && !addPhone.equals("")) {
                        if (!isValidMail(addMail)){
                            Toast.makeText(getContext(), getString(R.string.eMail), Toast.LENGTH_LONG).show();
                        } else {
                            new addContact().execute();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.empty), Toast.LENGTH_LONG).show();
                    }
                else {
                    DataBase DB = new DataBase(getContext());
                    SQLiteDatabase sqlDB = DB.getWritableDatabase();
                    //....//
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertDialog = this.builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void delete() {
        builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = layoutInflater.inflate(R.layout.modal_delete_contact, null);
        //Vars
        final TextView txtID = dialogView.findViewById(R.id.txtId);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.delete_contact));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteId = txtID.getText().toString().trim();
                if (Network.isOnline(getContext()))
                    if (!deleteId.equals("")) {
                        new deleteContact().execute();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.empty), Toast.LENGTH_LONG).show();
                    }
                else {
                    //Delete Local...
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertDialog = this.builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    class getContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle("Please Wait");
            dialog.setMessage("Searching for Contacts...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("user_id", String.valueOf(Login.user_id).trim()));

                JSONArray jsonArrayContacts = jsonParser.requestArray(Network.GET_CONTACTS, params);

                if (jsonArrayContacts.length() == 0) {
                    dialog.setTitle("No Contacts here");
                    dialog.setMessage("Try to add a contact...");
                    dialog.show();
                } else {
                    DataBase DB = new DataBase(getContext());
                    SQLiteDatabase sqlDB = DB.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    JSONObject jsonObject = null;
                    //Delete records from table to "update"...
                    sqlDB.delete("crm_prospectos_local", null, null);
                    for (int i = 0; i < jsonArrayContacts.length(); i++) {
                        jsonObject = jsonArrayContacts.getJSONObject(i);
                        Contact contact = new Contact();
                        contact.setId(jsonObject.getInt("codprospecto"));
                        contact.setfName(jsonObject.getString("nombre"));
                        contact.setlName(jsonObject.getString("apellido"));
                        contact.setPhone(jsonObject.getString("telefono"));
                        contact.setMail(jsonObject.getString("email"));
                        listContact.add(contact);
                        //Save to local DB
                        try {
                            cv.put("codprospecto", contact.getId());
                            cv.put("nombre", contact.getfName());
                            cv.put("apellido", contact.getlName());
                            cv.put("telefono", contact.getPhone());
                            cv.put("email", contact.getMail());
                            cv.put("estado", false);
                            cv.put("codasesorasignado", Login.user_id);
                            sqlDB.insert("crm_prospectos_local", null, cv);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                    sqlDB.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (listContact != null)
                initAdapter();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class addContact extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle(getString(R.string.wait));
            dialog.setMessage(getString(R.string.adding_contact));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("codempresa", String.valueOf(Login.cod_emp)));
                params.add(new BasicNameValuePair("fName", addFName));
                params.add(new BasicNameValuePair("lName", addLName));
                params.add(new BasicNameValuePair("phone", addPhone));
                params.add(new BasicNameValuePair("mail", addMail));
                params.add(new BasicNameValuePair("codasesor", String.valueOf(Login.user_id)));

                JSONObject jsonObjectAddContact = jsonParser.requestObject(Network.ADD_CONTACT, params);

                response_id = jsonObjectAddContact.getInt("response_id");

                return jsonObjectAddContact.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if (response_id != 1)
                Toast.makeText(getContext(), getString(R.string.add_error), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), getString(R.string.add_ok), Toast.LENGTH_LONG).show();
            reloadData();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class deleteContact extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle("Please Wait");
            dialog.setMessage("Deleting contact...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("codprospecto", deleteId));

                JSONObject jsonObjectDelete = jsonParser.requestObject(Network.DELETE_CONTACT, params);

                response_id = jsonObjectDelete.getInt("response_id");
                return jsonObjectDelete.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            //new getContacts().execute();
            if (response_id == 0)
                Toast.makeText(getContext(),  getString(R.string.delete_error), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), getString(R.string.delete_ok), Toast.LENGTH_LONG).show();
            reloadData();
        }
    }

    private void reloadData() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                //finish();
                if (!btnStatus) {
                    Intent i = new Intent(getContext(), Main.class);
                    startActivity(i);
                }
            }
        }, Network.DURATION);
    }


    private void getLocalContacts() {
        DataBase DB = new DataBase(Objects.requireNonNull(getActivity()).getApplicationContext());
        SQLiteDatabase sqlDB = DB.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursorContacts = sqlDB.rawQuery("SELECT * FROM crm_prospectos_local WHERE codasesorasignado = ?", new String[] {String.valueOf(Login.user_id)});
        if (cursorContacts.moveToFirst()) {
            while(!cursorContacts.isAfterLast()) {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursorContacts.getString(0)));
                contact.setfName(cursorContacts.getString(1));
                contact.setlName(cursorContacts.getString(2));
                contact.setPhone(cursorContacts.getString(3));
                contact.setMail(cursorContacts.getString(4));
                listContact.add(contact);
                cursorContacts.moveToNext();
                initAdapter();
            }
        } else
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
        cursorContacts.close();
        sqlDB.close();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }
/*
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.options_alt);
        menu.setHeaderTitle(getString(R.string.options));
        menu.add(Menu.NONE, CALL, menu.NONE, getString(R.string.openDial));
    }

    public boolean onContextItemSelected(MenuItem item) {
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;

        switch (item.getItemId()) {
            case CALL:
                String [] text = listContacts.getItemAtPosition(position).toString().split(":");
                String phoneNumber = text[1].trim();
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber));
                startActivity(dial);
                break;
        }
        return super.onContextItemSelected(item);
    }
    */
}