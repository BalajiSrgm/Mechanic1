package mechanic.com.mechanic;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.DateUtil;
import mechanic.com.mechanic.BusinessHelper.EditTextUtil;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.LoginBO;

public class settings extends AppCompatActivity {

    private TextView aboutTextView;
    private TextView showAboutTextView;
    private TextView sendUsAnEmailTextView;
    private TextView changePasswordTextView;
    private ListView hiddenListView;
    private TextInputLayout passwordWithToggle;
    private TextInputLayout conformPasswordWithToggle;
    boolean passswordToggleBoolean = true;
    boolean conformPassswordToggleBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        aboutTextView = (TextView) findViewById(R.id.about);
        showAboutTextView = (TextView) findViewById(R.id.showAbout);

        sendUsAnEmailTextView = (TextView) findViewById(R.id.sendusemail);
        changePasswordTextView = (TextView) findViewById(R.id.changePassword);
        setSupportActionBar(toolbar);

        Intent in = getIntent();
        final LoginBO dbloginBO = (LoginBO) in.getSerializableExtra("dbLoginBO");


        String s = "This Application was developed by Balaji shanmugam ";
        showAboutTextView.setText(s);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("common/login");

        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(settings.this);
                dialog.setContentView(R.layout.change_password_layout);
                dialog.setTitle("Update Password");
                dialog.setCancelable(true);
                final EditText newPassword = (EditText) dialog.findViewById(R.id.newPassword);
                final EditText newConformPassword = (EditText) dialog.findViewById(R.id.newconformPassword);
                final Button updatePasswordButton  = (Button) dialog.findViewById(R.id.updatepasswordButton);
                passwordWithToggle = (TextInputLayout) dialog.findViewById(R.id.newPasswordwithToggle);
                conformPasswordWithToggle = (TextInputLayout) dialog.findViewById(R.id.newConformPasswordwithToggle);

                newPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(TextUtils.isEmpty(newPassword.getText().toString())){
                            passwordWithToggle.setPasswordVisibilityToggleEnabled(false);
                            passswordToggleBoolean = true;
                        }else if(passswordToggleBoolean) {
                            passwordWithToggle.setPasswordVisibilityToggleEnabled(true);
                            passswordToggleBoolean = false;
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                newConformPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(TextUtils.isEmpty(newConformPassword.getText().toString())){
                            conformPasswordWithToggle.setPasswordVisibilityToggleEnabled(false);
                            conformPassswordToggleBoolean = true;
                        }else if(conformPassswordToggleBoolean) {
                            conformPasswordWithToggle.setPasswordVisibilityToggleEnabled(true);
                            conformPassswordToggleBoolean = false;
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });




                updatePasswordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(newPassword.getText().toString().equals(newConformPassword.getText().toString()))){
                            newConformPassword.setError(getString(R.string.error_check_password));
                            newConformPassword.requestFocus();
                        }

                        if(dbloginBO != null && StringUtil.isNotNullOrEmpty(dbloginBO.getIdLogin())){
                            myRef.child(dbloginBO.getIdLogin()).child("password").setValue(EditTextUtil.getString(newPassword));
                            myRef.child(dbloginBO.getIdLogin()).child("UpdatedTime").setValue(DateUtil.getDateAndTime(new Date()));
                            dialog.dismiss();
                            Toast.makeText(settings.this, "Password updated successfully", Toast.LENGTH_LONG).show();
                        }

                    }
                });


                dialog.show();

            }
        });
        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = "This Application was developed by Balaji shanmugam";
                showAboutTextView.setText(s);
               /* DataShow<String> stringList  = new ArrayList<String>();
                stringList
                ArrayAdapter arrayAdapter = new ArrayAdapter(settings.class,stringList);*/
            }
        });

        sendUsAnEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","balaje.srgm@gmail.com",null));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for BS Enterprise");
                startActivity(Intent.createChooser(intent,"Choose an Email client: "));
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

}
