package mechanic.com.mechanic;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.DateUtil;
import mechanic.com.mechanic.BusinessHelper.EditTextUtil;
import mechanic.com.mechanic.BusinessHelper.ElementConstants;
import mechanic.com.mechanic.BusinessHelper.MasterHelper;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.MechanicBO;

import static mechanic.com.mechanic.BusinessHelper.MasterHelper.clearEditTextVariables;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView userName;
    private TextView userFirstLastName;
    private Spinner materialsSpinner;
    private EditText carbonEditText;
    private EditText siliconEditText;
    private EditText manganesEditText;
    private EditText temperatureEditText;
    private EditText poringTemperatureEditText;
    private EditText boxWeightEditText;
    private EditText materialNameEditText;
    private Button save;
    private Button reset;
    public  LoginBO dbloginBO;
    private String materialType;
    SharedPreferences sharedpreferences;
    List<String> materialsList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    Bundle defaultBundle = new Bundle();

    static {
        System.out.println("static works");
    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("on start works");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("on start works");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on start works");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("on start works");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on start works");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on start works");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialsSpinner = (Spinner) findViewById(R.id.materials);
        carbonEditText = (EditText) findViewById(R.id.carbon);
        siliconEditText = (EditText) findViewById(R.id.silicon);
        manganesEditText = (EditText) findViewById(R.id.manganes);
        temperatureEditText = (EditText) findViewById(R.id.temperature);
        poringTemperatureEditText = (EditText) findViewById(R.id.poringTemperature);
        boxWeightEditText = (EditText) findViewById(R.id.boxWeight);
        materialNameEditText = (EditText) findViewById(R.id.materialName);
        save = (Button) findViewById(R.id.save);
        reset = (Button) findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllVariables();

            }
        });
        if (!(isNetworkAvailable())) {
            Toast.makeText(MainActivity.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }


        /*
        final Drawable search = getResources().getDrawable(R.drawable.ic_search_black_24dp);
        search.setColorFilter(getResou rces().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(search);*/

        materialsList.add("");
        materialsList.add("Ferrous");
        materialsList.add("Non Ferrous");

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,materialsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialsSpinner.setAdapter(arrayAdapter);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        defaultBundle = b;

        final MechanicBO mechanicBO = (MechanicBO) in.getSerializableExtra("MechanicBO");
        dbloginBO = (LoginBO) in.getSerializableExtra("dbLoginBO");
        if(mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())){
            carbonEditText.setText(mechanicBO.getCarbon());
            siliconEditText.setText(mechanicBO.getSilicon());
            manganesEditText.setText(mechanicBO.getManganes());
            temperatureEditText.setText(mechanicBO.getTemperature());
            poringTemperatureEditText.setText(mechanicBO.getPoringTemperature());
            boxWeightEditText.setText(mechanicBO.getBoxWeight());
            materialNameEditText.setText(mechanicBO.getMaterialName());
            int count = 0;
            for(String materialType: materialsList){
                if(StringUtil.isEqual(materialType,mechanicBO.getMaterialType())){
                    materialsSpinner.setSelection(count);
                    break;
                }else{
                    count++;
                }
            }


        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(materialsSpinner.getSelectedItem().toString())){
                    TextView textView = (TextView) materialsSpinner.getSelectedView();
                    textView.setError("Please Select Material Type");
                }
                if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(carbonEditText)))){
                    carbonEditText.setError(getString(R.string.error_field_required));
                    carbonEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(siliconEditText)))){
                    siliconEditText.setError(getString(R.string.error_field_required));
                    siliconEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(manganesEditText)))){
                    manganesEditText.setError(getString(R.string.error_field_required));
                    manganesEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(temperatureEditText)))){
                    temperatureEditText.setError(getString(R.string.error_field_required));
                    temperatureEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(poringTemperatureEditText)))){
                    poringTemperatureEditText.setError(getString(R.string.error_field_required));
                    poringTemperatureEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(boxWeightEditText)))){
                    boxWeightEditText.setError(getString(R.string.error_field_required));
                    boxWeightEditText.requestFocus();
                }if(!(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(materialNameEditText)))){
                    materialNameEditText.setError(getString(R.string.error_field_required));
                    materialNameEditText.requestFocus();
                }else if(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(carbonEditText)) &&
                        StringUtil.isNotNullOrEmpty(EditTextUtil.getString(siliconEditText)) &&
                                StringUtil.isNotNullOrEmpty(EditTextUtil.getString(manganesEditText)) &&
                                        StringUtil.isNotNullOrEmpty(EditTextUtil.getString(temperatureEditText)) &&
                                                StringUtil.isNotNullOrEmpty(EditTextUtil.getString(poringTemperatureEditText)) &&
                                                        StringUtil.isNotNullOrEmpty(EditTextUtil.getString(boxWeightEditText)) &&
                                                                StringUtil.isNotNullOrEmpty(materialsSpinner.getSelectedItem().toString()) &&
                                                                    StringUtil.isNotNullOrEmpty(EditTextUtil.getString(materialNameEditText))){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("common/mechanic");

                    if(mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())){
                        myRef.child(mechanicBO.getIdMechanic()).child("carbon").setValue(EditTextUtil.getString(carbonEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("silicon").setValue(EditTextUtil.getString(siliconEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("manganes").setValue(EditTextUtil.getString(manganesEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("temperature").setValue(EditTextUtil.getString(temperatureEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("poringTemperature").setValue(EditTextUtil.getString(poringTemperatureEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("boxWeight").setValue(EditTextUtil.getString(boxWeightEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("materialType").setValue(materialsSpinner.getSelectedItem().toString());
                        myRef.child(mechanicBO.getIdMechanic()).child("materialName").setValue(EditTextUtil.getString(materialNameEditText));
                        myRef.child(mechanicBO.getIdMechanic()).child("UpdatedTime").setValue(DateUtil.getDateAndTime(new Date()));

                    }else {
                        String id = myRef.push().getKey();
                        MechanicBO mechanicBO = new MechanicBO();

                        mechanicBO.setIdMechanic(id);
                        mechanicBO.setCarbon(EditTextUtil.getString(carbonEditText));
                        mechanicBO.setSilicon(EditTextUtil.getString(siliconEditText));
                        mechanicBO.setManganes(EditTextUtil.getString(manganesEditText));
                        mechanicBO.setTemperature(EditTextUtil.getString(temperatureEditText));
                        mechanicBO.setPoringTemperature(EditTextUtil.getString(poringTemperatureEditText));
                        mechanicBO.setBoxWeight(EditTextUtil.getString(boxWeightEditText));
                        mechanicBO.setMaterialName(EditTextUtil.getString(materialNameEditText));
                        mechanicBO.setUpdatedTime(DateUtil.getDateAndTime(new Date()));
                        materialType = materialsSpinner.getSelectedItem().toString();
                        mechanicBO.setMaterialType(materialType);
                        myRef.child(id).setValue(mechanicBO);

                    }
                    clearAllVariables();
                    materialNameEditText.requestFocus();


                }
            }
        });
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(ElementConstants.MyPREFERENCES, Context.MODE_PRIVATE);




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.userName);
        userFirstLastName = (TextView) header.findViewById(R.id.userFirstName);
        String userFullName = "";
        if(dbloginBO != null) {
            if (StringUtil.isNotNullOrEmpty(dbloginBO.getFirstName()) && StringUtil.isNotNullOrEmpty(dbloginBO.getLastName())) {
                userFullName = dbloginBO.getFirstName() + " " + dbloginBO.getLastName();
            } else {
                userFullName = dbloginBO.getFirstName();
            }
            if(StringUtil.isNotNullOrEmpty(dbloginBO.getUserName())) {
                userName.setText(dbloginBO.getUserName());
            }

            userFirstLastName.setText(userFullName);
        }else{
            userFullName = b.getString(ElementConstants.userFirstLastName);
            String username = b.getString(ElementConstants.Name);
            userFirstLastName.setText(userFullName);


            if(StringUtil.isNotNullOrEmpty(username)) {
                userName.setText(username);
            }
        }


    }

    private void clearAllVariables() {
        Intent in = getIntent();
        clearEditTextVariables(carbonEditText);
        clearEditTextVariables(siliconEditText);
        clearEditTextVariables(manganesEditText);
        clearEditTextVariables(temperatureEditText);
        clearEditTextVariables(poringTemperatureEditText);
        clearEditTextVariables(boxWeightEditText);
        clearEditTextVariables(materialNameEditText);
        materialNameEditText.requestFocus();
        materialsSpinner.setSelection(0);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        AutoCompleteTextView  autoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*try {
            Field mcursor = TextView.class.getDeclaredField("mcursor");
            mcursor.setAccessible(true);
            mcursor.set(autoCompleteTextView,R.drawable.white_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_search){

        }
        if (id == R.id.action_settings) {
            Intent i  = new Intent(getApplicationContext(),settings.class);

            i.putExtra("dbLoginBO",dbloginBO);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_operation) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.putExtras(defaultBundle);
            startActivity(i);
        } else if (id == R.id.nav_List) {
            Intent i = new Intent(getApplicationContext(),DataShow.class);
            i.putExtras(defaultBundle);
            startActivity(i);
        }else if (id == R.id.nav_Search) {
            Intent i = new Intent(getApplicationContext(),filter.class);
            i.putExtras(defaultBundle);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
            String filePath = applicationInfo.sourceDir;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(intent.createChooser(intent,"Share app via"));
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences = getSharedPreferences(ElementConstants.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
