package com.fmendez.parsejson;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {
    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();
        new GetContacts().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            //ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            //String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            String jsonStr = "[{\"id\":12305.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"15:01:09\"},{\"id\":12304.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:55:43\"},{\"id\":12303.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:40:40\"},{\"id\":12302.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:37:18\"},{\"id\":12301.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:32:49\"},{\"id\":12300.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:32:06\"},{\"id\":12299.0,\"temperatura\":\"8\",\"humedad\":\"9\",\"presion\":\"10\",\"fecha\":\"2015-03-09\",\"hora\":\"14:31:55\"},{\"id\":12298.0,\"temperatura\":\"5\",\"humedad\":\"6\",\"presion\":\"7\",\"fecha\":\"2015-03-09\",\"hora\":\"14:09:39\"}]";

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    //contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    JSONArray arreglo = new JSONArray(jsonStr);
                    //contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        //String id = c.getString(TAG_ID);
                        //String name = c.getString(TAG_NAME);
                        //String email = c.getString(TAG_EMAIL);
                        //String address = c.getString(TAG_ADDRESS);
                        //String gender = c.getString(TAG_GENDER);


                        String id = c.getString("id");
                        String temperatura = c.getString("temperatura");
                        String humedad = c.getString("humedad");
                        String presion = c.getString("presion");
                        String fecha = c.getString("fecha");
                        String hora = c.getString("hora");

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject(TAG_PHONE);
                        //String mobile = phone.getString(TAG_PHONE_MOBILE);
                        //String home = phone.getString(TAG_PHONE_HOME);
                        //String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("temperatura", temperatura);
                        contact.put("humedad", humedad);
                        contact.put("presion", presion);
                        contact.put("fecha", fecha);
                        contact.put("hora", hora);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item1, new String[] { "id", "temperatura", "humedad", "presion",
                    "fecha", "hora" }, new int[] { R.id.id,
                    R.id.temperatura, R.id.humedad, R.id.presion, R.id.fecha, R.id.hora });

            setListAdapter(adapter);
        }

    }
}
