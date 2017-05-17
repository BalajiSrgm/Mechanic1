package mechanic.com.mechanic;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.EditTextUtil;
import mechanic.com.mechanic.BusinessHelper.ListUtil;
import mechanic.com.mechanic.BusinessHelper.MechanicHelper;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.MechanicBO;

import static mechanic.com.mechanic.BusinessHelper.MasterHelper.clearEditTextVariables;

public class filter extends AppCompatActivity {

    private Spinner materialsSpinner;
    private EditText carbonEditText;
    private EditText siliconEditText;
    private EditText manganesEditText;
    private EditText temperatureEditText;
    private EditText poringTemperatureEditText;
    private EditText boxWeightEditText;
    private EditText materialNameEditText;
    private Button search;
    private Button reset;
    private String materialType;
    private ListView materialNameListView;
    List<String> materialsList = new ArrayList<>();
    ArrayList<String> materialNameList = new ArrayList<>();
    private List<MechanicBO> mechanicBOs = new ArrayList<>();

    ArrayAdapter arrayAdapter;
    ArrayAdapter<String> listViewAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("common/mechanic");
    Bundle defaultBundle = new Bundle();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtras(defaultBundle);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        defaultBundle = b;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        materialsSpinner = (Spinner) findViewById(R.id.materialsFilter);
        carbonEditText = (EditText) findViewById(R.id.carbonFilter);
        siliconEditText = (EditText) findViewById(R.id.siliconFilter);
        manganesEditText = (EditText) findViewById(R.id.manganesFilter);
        temperatureEditText = (EditText) findViewById(R.id.temperatureFilter);
        poringTemperatureEditText = (EditText) findViewById(R.id.poringTemperatureFilter);
        boxWeightEditText = (EditText) findViewById(R.id.boxWeightFilter);
        materialNameEditText = (EditText) findViewById(R.id.materialNameFilter);
        search = (Button) findViewById(R.id.searchFilter);
        reset = (Button) findViewById(R.id.resetFilter);
        materialNameListView = (ListView) findViewById(R.id.materialNameListView);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllFilterVariables();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MechanicBO> searchedmechanicBOs = new ArrayList<MechanicBO>();
                String materialNameSearched = EditTextUtil.getString(materialNameEditText);
                if(StringUtil.isNotNullOrEmpty(materialNameSearched) && ListUtil.isNotNullOrEmpty(mechanicBOs)){
                    for(MechanicBO mechanicBO : mechanicBOs){
                        if(StringUtil.isEqual(mechanicBO.getMaterialName(),materialNameSearched)){
                            searchedmechanicBOs.add(mechanicBO);

                        }

                    }
                }

                Intent intent = new Intent(getApplicationContext(), DataShow.class);
                intent.putExtra("searchedMechanicBOs",searchedmechanicBOs);
                intent.putExtras(defaultBundle);
                startActivity(intent);

            }
        });


        materialsList.add("");
        materialsList.add("Ferrous");
        materialsList.add("Non Ferrous");



        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,materialsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialsSpinner.setAdapter(arrayAdapter);


        materialNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_clear_black_24dp,0);
                String materialNameSearch = EditTextUtil.getString(materialNameEditText);
                if(StringUtil.isNotNullOrEmpty(materialNameSearch)) {
                    Query query = myRef.orderByChild("materialName").equalTo(materialNameSearch);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mechanicBOs.clear();
                            materialNameList.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                MechanicBO mechanicBO = MechanicHelper.databaseToBO(dataSnapshot1);
                                if (mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())) {
                                    mechanicBOs.add(mechanicBO);
                                }
                                if (StringUtil.isNotNullOrEmpty(mechanicBO.getMaterialName())) {
                                    materialNameList.add(mechanicBO.getMaterialName());
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    populateListView();
                }else{
                    materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if(ListUtil.isNotNullOrEmpty(materialNameList)) {
                    materialNameListView.setVisibility(View.VISIBLE);
                }*/
            }
        });


        materialNameListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });
        materialNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                materialNameListView.setAdapter(null);
                materialNameListView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                String value = (materialNameListView.getItemAtPosition(position).toString());
                if(StringUtil.isNotNullOrEmpty(value)) {
                    materialNameEditText.setText(value);
                    materialNameListView.setVisibility(View.GONE);
                    carbonEditText.requestFocus();
                }


            }
        });
        materialNameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getRawX() >= materialNameEditText.getRight() - materialNameEditText.getTotalPaddingRight()){
                    clearEditTextVariables(materialNameEditText);
                    materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    materialNameListView.setVisibility(View.GONE);

                }
                return false;
            }
        });

        materialNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(materialNameEditText))){
                        materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_clear_black_24dp,0);
                    }else{
                        materialNameListView.setVisibility(View.GONE);
                        materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }else {
                    materialNameListView.setVisibility(View.GONE);
                    materialNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });




       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void populateListView() {
        if(ListUtil.isNotNullOrEmpty(materialNameList)) {
            listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, materialNameList);
            listViewAdapter.notifyDataSetChanged();
            materialNameListView.setAdapter(listViewAdapter);
            materialNameListView.setVisibility(View.VISIBLE);
        }
    }

    private void clearAllFilterVariables() {
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

}
