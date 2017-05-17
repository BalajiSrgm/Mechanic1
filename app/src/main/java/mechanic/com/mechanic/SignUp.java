package mechanic.com.mechanic;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import mechanic.com.mechanic.BusinessHelper.DateUtil;
import mechanic.com.mechanic.BusinessHelper.EditTextUtil;
import mechanic.com.mechanic.BusinessHelper.ListUtil;
import mechanic.com.mechanic.BusinessHelper.LoginHelper;
import mechanic.com.mechanic.BusinessHelper.MasterHelper;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.ErrorListBO;
import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.StatusBO;
import mechanic.com.mechanic.BusinessObject.UserRoleBO;
import mechanic.com.mechanic.BusinessValidation.Validation;

public class SignUp extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText userNameEditText;
    private EditText userPasswordEditText;
    private EditText userConformPasswordEditText;
    private EditText securityAnswerEditText;
    private TextView linkToSignIn;
    private Button submitButton;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout passwordWithToggle;
    List<String> emailDefaultListString = new ArrayList<>();
    private TextInputLayout conformPasswordWithToggle;
    private ListView emailSampleListView;
    boolean passswordToggleBoolean = true;
    boolean conformPassswordToggleBoolean = true;
    ArrayAdapter<String> listViewAdapter;
    Bundle defaultBundle = new Bundle();
    List<String> securityQuestionList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    private Spinner securityQuestionSpinner;


    ArrayList<LoginBO> dbLoginBOs = new ArrayList<LoginBO>();
/*



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtras(defaultBundle);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



/*

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/

        firstNameEditText = (EditText) findViewById(R.id.firstName);
        lastNameEditText = (EditText) findViewById(R.id.LastName);
        userNameEditText = (EditText) findViewById(R.id.signUpEmail);
        userPasswordEditText = (EditText) findViewById(R.id.signUpPassword);
        userConformPasswordEditText = (EditText) findViewById(R.id.signUpconformPassword);
        securityAnswerEditText = (EditText) findViewById(R.id.securityAnswer);
        passwordWithToggle = (TextInputLayout) findViewById(R.id.signUpPasswordwithToggle);
        conformPasswordWithToggle = (TextInputLayout) findViewById(R.id.signUpConformPasswordwithToggle);
        linkToSignIn = (TextView) findViewById(R.id.linktoSignIn);
        emailSampleListView = (ListView) findViewById(R.id.emailSample);
        submitButton = (Button) findViewById(R.id.signUpSubmit);
        securityQuestionSpinner = (Spinner) findViewById(R.id.securityQuestion);

        securityQuestionList.add("");
        securityQuestionList.add("What is your best friend name");
        securityQuestionList.add("What is your life goal");
        securityQuestionList.add("What is your native pincode");

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,securityQuestionList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionSpinner.setAdapter(arrayAdapter);
        
        Intent in = getIntent();
        Bundle b = in.getExtras();
        defaultBundle = b;
        dbLoginBOs = (ArrayList<LoginBO>) in.getSerializableExtra("dbLoginBOs");
            if(!(ListUtil.isNotNullOrEmpty(dbLoginBOs))) {
                getdbLoginBOs();
            }
        linkToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean showornot = true;
                String getUserNameValue = EditTextUtil.getString(userNameEditText);

                if(EditTextUtil.getString(userNameEditText).contains("@")) {
                    getUserNameValue = getUserNameValue.substring(getUserNameValue.indexOf("@")+1);

                    if (StringUtil.isNotNullOrEmpty(getUserNameValue)) {
                        showornot = false;
                    }
                }
                if(showornot && EditTextUtil.getString(userNameEditText).contains("@")){
                    emailDefaultListString = MasterHelper.getSampleEmailDomain(EditTextUtil.getString(userNameEditText));

                    listViewAdapter = new ArrayAdapter<String>(SignUp.this,android.R.layout.simple_list_item_1,emailDefaultListString);
                    listViewAdapter.notifyDataSetChanged();
                    emailSampleListView.setAdapter(listViewAdapter);
                    emailSampleListView.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(userNameEditText.getWindowToken(),0);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailSampleListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        emailSampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (emailSampleListView.getItemAtPosition(position).toString());
                if(StringUtil.isNotNullOrEmpty(value)) {
                    userNameEditText.setText(value);
                   emailSampleListView.setVisibility(View.GONE);
                    userPasswordEditText.requestFocus();
                    getValidateUserName();
}
            }
        });

        userPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(userPasswordEditText.getText().toString())){
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

        userConformPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(userConformPasswordEditText.getText().toString())){
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
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailSampleListView.setVisibility(View.GONE);
                if(!(TextUtils.isEmpty(userNameEditText.getText().toString()))) {
                    getValidateUserName();

                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                if(getValidateUserName()) {

                }else if(!(userPasswordEditText.getText().toString().equals(userConformPasswordEditText.getText().toString()))){
                    userConformPasswordEditText.setError(getString(R.string.error_check_password));
                    userConformPasswordEditText.requestFocus();
                }else if(TextUtils.isEmpty(securityQuestionSpinner.getSelectedItem().toString())){
                    TextView securityQuestionErrorTextview = (TextView) securityQuestionSpinner.getSelectedView();
                    securityQuestionErrorTextview.setError("Please Select Security Question");

                }else if(TextUtils.isEmpty(EditTextUtil.getString(securityAnswerEditText))){
                    securityAnswerEditText.setError(getString(R.string.error_empty_security_answer));
                    securityAnswerEditText.requestFocus();
                }else if(TextUtils.isEmpty(userNameEditText.getText().toString())){
                    userNameEditText.setError(getString(R.string.error_field_required));
                    userNameEditText.requestFocus();
                }else if(TextUtils.isEmpty(userPasswordEditText.getText().toString())){
                    userPasswordEditText.setError(getString(R.string.error_field_required));
                    userPasswordEditText.requestFocus();
                }else if(TextUtils.isEmpty(userConformPasswordEditText.getText().toString())){
                    userConformPasswordEditText.setError(getString(R.string.error_field_required));
                    userConformPasswordEditText.requestFocus();
                }else if(!(userNameEditText.getText().toString().contains("@"))){
                    userNameEditText.setError(getString(R.string.error_invalid_email));
                    userNameEditText.requestFocus();
                }else if(userNameEditText.getText().toString() != null &&
                        userPasswordEditText.getText().toString() != null &&
                        userPasswordEditText.getText().toString().equals(userConformPasswordEditText.getText().toString())){

                    UserRoleBO userRoleBO = new UserRoleBO();
                    userRoleBO.setIdRole("role_002");
                    userRoleBO.setRoleDescription("employee");
                    userRoleBO.setStatusBO(new StatusBO());
                    userRoleBO.setUpdatedTime(DateUtil.getDateAndTime(new Date()));
                    userRoleBO.getStatusBO().setIdStatus("status_002");
                    userRoleBO.getStatusBO().setDescription("userSaveSuccessfully");
                    userRoleBO.getStatusBO().setUpdatedTime(DateUtil.getDateAndTime(new Date()));
                    LoginBO loginBO = new LoginBO();
                    loginBO.setFirstName(EditTextUtil.getString(firstNameEditText));
                    loginBO.setLastName(EditTextUtil.getString(lastNameEditText));
                    loginBO.setUserName(EditTextUtil.getString(userNameEditText));
                    loginBO.setPassword(EditTextUtil.getString(userPasswordEditText));
                    loginBO.setUpdatedTime(DateUtil.getDateAndTime(new Date()));
                    loginBO.setSecurityAnswer(EditTextUtil.getString(securityAnswerEditText));
                    loginBO.setSecurityQuestion(securityQuestionSpinner.getSelectedItem().toString());
                    loginBO.setUserRoleBO(new UserRoleBO());
                    loginBO.setUserRoleBO(userRoleBO);

                   /* FirebaseUser user = firebaseAuth.getCurrentUser();
                    String userId = user.getUid();*/
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("common/sign_up");

                   // System.out.println(userId);
                    String id  = myRef.push().getKey();
                    loginBO.setIdLogin(id);
                    System.out.println(database);
                    System.out.println(myRef);
                    myRef.child(id).setValue(loginBO);
                    myRef = database.getReference("common/login");
                    id  = myRef.push().getKey();
                    loginBO.setIdLogin(id);
                    myRef.child(id).setValue(loginBO);
          //
                    String userIdString = EditTextUtil.getString(userNameEditText);
                    String userPasswordString = EditTextUtil.getString(userPasswordEditText);
//                    firebaseAuth = FirebaseAuth.getInstance();
//             //       final String User_id = firebaseAuth.getCurrentUser().getUid();
//                    firebaseAuth.createUserWithEmailAndPassword(userIdString,userPasswordString).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                           /* startActivity(new Intent(SignUp.this,MainActivity.class));
//                            finish();*/
//                            if(task.isSuccessful()){
//                                Toast.makeText(SignUp.this,"created"+task.getResult(),Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });

                  Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                }

            }
        });
    }

    private boolean getValidateUserName() {
        boolean result = false;
        if(!(ListUtil.isNotNullOrEmpty(dbLoginBOs))) {
            getdbLoginBOs();
        }

        if (ListUtil.isNotNullOrEmpty(dbLoginBOs)) {
            LoginBO loginBO = new LoginBO();
            loginBO.setUserName(userNameEditText.getText().toString());
            loginBO.setPassword(userPasswordEditText.getText().toString());
            loginBO.setFirstName(firstNameEditText.getText().toString());
            loginBO.setLastName(lastNameEditText.getText().toString());
            List<ErrorListBO> errorListBOs = Validation.validateUserName(dbLoginBOs, loginBO);
            if (ListUtil.isNotNullOrEmpty(errorListBOs)) {
                userNameEditText.setError(errorListBOs.get(0).getErrorMessage());
                //userNameEditText.requestFocus();
                result = true;
            }
        }
        return result;
    }

    private void getdbLoginBOs() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference("common/login");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LoginBO loginBO = LoginHelper.databaseToBO(dataSnapshot1);
                    if (loginBO != null && StringUtil.isNotNullOrEmpty(loginBO.getIdLogin())) {
                        dbLoginBOs.add(loginBO);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
