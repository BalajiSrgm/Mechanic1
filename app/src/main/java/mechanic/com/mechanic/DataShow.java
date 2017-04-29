package mechanic.com.mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.DataShowAdapter;
import mechanic.com.mechanic.BusinessHelper.ListUtil;
import mechanic.com.mechanic.BusinessHelper.LoginHelper;
import mechanic.com.mechanic.BusinessHelper.MechanicHelper;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.MechanicBO;

public class DataShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<MechanicBO> mechanicBOs = new ArrayList<>();
    private DataShowAdapter mAdapter;
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
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        defaultBundle = b;
       //toolbar.setNavigationIcon(getResources().getdr);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



        mAdapter = new DataShowAdapter(mechanicBOs);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        Intent in = getIntent();
        mechanicBOs = (ArrayList<MechanicBO>) in.getSerializableExtra("searchedMechanicBOs");
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
            recyclerView.setAdapter(null);
            recyclerView.setAdapter(new DataShowAdapter(mechanicBOs));
            mAdapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(linearLayoutManager);
        }else {

            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference databaseReference = database.getReference("common/mechanic");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        mechanicBOs.clear();
                    }else{
                        mechanicBOs = new ArrayList<MechanicBO>();
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        MechanicBO mechanicBO = MechanicHelper.databaseToBO(dataSnapshot1);
                        if (mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())) {
                            mechanicBOs.add(mechanicBO);
                        }
                    }
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        Collections.reverse(mechanicBOs);
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new DataShowAdapter(mechanicBOs));
                        mAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        mechanicBOs.clear();
                    }else{
                        mechanicBOs = new ArrayList<MechanicBO>();
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        MechanicBO mechanicBO = MechanicHelper.databaseToBO(dataSnapshot1);
                        if (mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())) {
                            mechanicBOs.add(mechanicBO);
                        }
                    }
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        Collections.reverse(mechanicBOs);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        mechanicBOs.clear();
                    }else{
                        mechanicBOs = new ArrayList<MechanicBO>();
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        MechanicBO mechanicBO = MechanicHelper.databaseToBO(dataSnapshot1);
                        if (mechanicBO != null && StringUtil.isNotNullOrEmpty(mechanicBO.getIdMechanic())) {
                            mechanicBOs.add(mechanicBO);
                        }
                    }
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        Collections.reverse(mechanicBOs);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
