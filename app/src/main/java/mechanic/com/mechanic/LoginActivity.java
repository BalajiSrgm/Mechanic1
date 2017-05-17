package mechanic.com.mechanic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.DateUtil;
import mechanic.com.mechanic.BusinessHelper.EditTextUtil;
import mechanic.com.mechanic.BusinessHelper.ElementConstants;
import mechanic.com.mechanic.BusinessHelper.ListUtil;
import mechanic.com.mechanic.BusinessHelper.LoginHelper;
import mechanic.com.mechanic.BusinessHelper.MasterHelper;
import mechanic.com.mechanic.BusinessHelper.NetworkHelper;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.ErrorListBO;
import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.StatusBO;
import mechanic.com.mechanic.BusinessObject.UserRoleBO;
import mechanic.com.mechanic.BusinessValidation.Validation;

import static android.Manifest.permission.READ_CONTACTS;
import static mechanic.com.mechanic.BusinessHelper.MasterHelper.clearEditTextVariables;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView signUpTextView;
    private TextView forgotPasswordTextView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextInputLayout mpasswordWithToggle;
    SharedPreferences sharedpreferences;
    boolean doubleBackToExitPressedOnce = false;
    boolean passswordToggleBoolean = true;
    ArrayList<LoginBO> dbLoginBOs = new ArrayList<>();
    private ListView emailSampleListView;
    ArrayAdapter<String> listViewAdapter;
    List<String> emailDefaultListString = new ArrayList<>();
    List<String> securityQuestionList = new ArrayList<>();
    ArrayAdapter securityQuestionArrayAdapter;
    LoginBO currentloginBO = new LoginBO();


    private LoginBO loginBO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mpasswordWithToggle = (TextInputLayout) findViewById(R.id.passwordWithToggle);
        signUpTextView = (TextView) findViewById(R.id.signUpLink);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordLink);
        emailSampleListView = (ListView) findViewById(R.id.emailloginSample);
        sharedpreferences = getSharedPreferences(ElementConstants.MyPREFERENCES, Context.MODE_PRIVATE);

        forgotPasswordTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.forget_password_layout);
                final EditText userEmailId = (EditText) dialog.findViewById(R.id.forgotEmail);
                final EditText securityAnswer = (EditText) dialog.findViewById(R.id.forgotSecurityAnswer);
                final Spinner securityQuestionSpinner = (Spinner) dialog.findViewById(R.id.forgotSecurityQuestion);
                final Button forgotSubmit = (Button) dialog.findViewById(R.id.forgetSubmit);

                securityQuestionList.clear();
                securityQuestionList.add("");
                securityQuestionList.add("What is your best friend name");
                securityQuestionList.add("What is your life goal");
                securityQuestionList.add("What is your native pincode");

                securityQuestionArrayAdapter = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, securityQuestionList);
                securityQuestionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                securityQuestionSpinner.setAdapter(securityQuestionArrayAdapter);


                userEmailId.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                userEmailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (hasFocus) {
                            if (StringUtil.isNotNullOrEmpty(EditTextUtil.getString(userEmailId))) {
                                userEmailId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                            } else {
                                userEmailId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }
                        } else {
                            userEmailId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                });

                userEmailId.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getRawX() >= userEmailId.getRight() - userEmailId.getTotalPaddingRight()) {
                            clearEditTextVariables(userEmailId);
                            userEmailId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        }
                        return false;
                    }
                });

                forgotSubmit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(isEmailValid(EditTextUtil.getString(userEmailId)))) {
                            userEmailId.setError(getString(R.string.error_invalid_email));
                        }
                        if (TextUtils.isEmpty(EditTextUtil.getString(securityAnswer))) {
                            securityAnswer.setError(getString(R.string.error_empty_security_answer));
                        }
                        if (TextUtils.isEmpty(securityQuestionSpinner.getSelectedItem().toString())) {
                            TextView securityQuestionErrorTextview = (TextView) securityQuestionSpinner.getSelectedView();
                            securityQuestionErrorTextview.setError("Please Select Security Question");
                        }

                        if (ListUtil.isNotNullOrEmpty(dbLoginBOs) && StringUtil.isNotNullOrEmpty(EditTextUtil.getString(userEmailId))) {

                            currentloginBO.setUserName(EditTextUtil.getString(userEmailId));

                            List<ErrorListBO> errorListBOs = Validation.validateUserName(dbLoginBOs, currentloginBO);
                            if (!(ListUtil.isNotNullOrEmpty(errorListBOs))) {
                                userEmailId.setError("Given userName is not registered");
                            }

                            for(LoginBO loginBO : dbLoginBOs){
                                if(StringUtil.isEqual(loginBO.getUserName(),currentloginBO.getUserName())){
                                    currentloginBO = new LoginBO();
                                    currentloginBO = loginBO;
                                    break;
                                }
                            }
                        }

/*
                        if(!(StringUtil.isEqual(securityQuestionSpinner.getSelectedItem().toString(),currentloginBO.getSecurityQuestion()))){
                            TextView securityQuestionErrorTextview = (TextView) securityQuestionSpinner.getSelectedView();
                            securityQuestionErrorTextview.setError("Security Question is wrong");
                        }

                        if(!(StringUtil.isEqual(EditTextUtil.getString(securityAnswer),currentloginBO.getSecurityAnswer()))){
                            securityAnswer.setError("Security Answer is wrong");
                        }
*/

                        /*if(StringUtil.isNotNullOrEmpty(EditTextUtil.getString(userEmailId)) &&
                                StringUtil.isNotNullOrEmpty(securityQuestionSpinner.getSelectedItem().toString()) &&
                                StringUtil.isNotNullOrEmpty(EditTextUtil.getString(securityAnswer)) &&
                                StringUtil.isEqual(securityQuestionSpinner.getSelectedItem().toString(),currentloginBO.getSecurityQuestion()) &&
                                StringUtil.isEqual(EditTextUtil.getString(securityAnswer),currentloginBO.getSecurityAnswer())){

                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Password has been send to your email successfully", Toast.LENGTH_LONG).show();
                        }*/

                        NetworkHelper.password = currentloginBO.getPassword();
                                new  NetworkHelper().execute();

                                dialog.dismiss();


                    }
                });

                dialog.show();

            }
        });

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            //onBackPressed();
        } else if (sharedpreferences.contains(ElementConstants.Name)) {
            String id = sharedpreferences.getString(ElementConstants.Id, "");
            String email = sharedpreferences.getString(ElementConstants.Name, "");
            String password = sharedpreferences.getString(ElementConstants.password, "");
            String userFirstLastName = sharedpreferences.getString(ElementConstants.userFirstLastName, "");
            if (!(TextUtils.isEmpty(id)) && !(TextUtils.isEmpty(email)) && !(TextUtils.isEmpty(password))) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                LoginBO dbloginBO = new LoginBO();
                dbloginBO.setIdLogin(id);
                dbloginBO.setUserName(email);
                dbloginBO.setPassword(password);
                dbloginBO.setFirstName(userFirstLastName);
                i.putExtra("dbLoginBO", dbloginBO);
                Bundle b = MasterHelper.getDefaultBundleValues(id, email, password, userFirstLastName);

                i.putExtras(b);
                // MainActivity.loginBO = login
                startActivity(i);
            }
        }

        if (!(isNetworkAvailable())) {
            Toast.makeText(LoginActivity.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }

        signUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                i.putExtra("dbLoginBOs", dbLoginBOs);
                startActivity(i);
            }
        });


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

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean showornot = true;
                String getUserNameValue = EditTextUtil.getString(mEmailView);

                if (EditTextUtil.getString(mEmailView).contains("@")) {
                    getUserNameValue = getUserNameValue.substring(getUserNameValue.indexOf("@") + 1);

                    if (StringUtil.isNotNullOrEmpty(getUserNameValue)) {
                        showornot = false;
                    }
                }
               /* if(showornot && EditTextUtil.getString(mEmailView).contains("@")){
                    emailDefaultListString = MasterHelper.getSampleEmailDomain(EditTextUtil.getString(mEmailView));

                    listViewAdapter = new ArrayAdapter<String>(LoginActivity.this,android.R.change_password_layout.simple_list_item_1,emailDefaultListString);
                    listViewAdapter.notifyDataSetChanged();
                    emailSampleListView.setAdapter(listViewAdapter);
                    emailSampleListView.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEmailView.getWindowToken(),0);
                }*/


                mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isEmailValid(EditTextUtil.getString(mEmailView))) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                }
            }
        });

        emailSampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (emailSampleListView.getItemAtPosition(position).toString());
                if (StringUtil.isNotNullOrEmpty(value)) {
                    mEmailView.setText(value);
                    emailSampleListView.setVisibility(View.GONE);
                    mPasswordView.requestFocus();
                }
            }
        });
        mEmailView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getRawX() >= mEmailView.getRight() - mEmailView.getTotalPaddingRight()) {
                    clearEditTextVariables(mEmailView);
                    mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }
                return false;
            }
        });

        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (StringUtil.isNotNullOrEmpty(EditTextUtil.getString(mEmailView))) {
                        mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_24dp, 0);
                    } else {
                        mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                } else {
                    emailSampleListView.setVisibility(View.GONE);
                    mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });


        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                    mpasswordWithToggle.setPasswordVisibilityToggleEnabled(false);
                    passswordToggleBoolean = true;
                } else if (passswordToggleBoolean) {
                    mpasswordWithToggle.setPasswordVisibilityToggleEnabled(true);
                    passswordToggleBoolean = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //  attemptLogin();
                //      if (allowSignIn) {
                String email = EditTextUtil.getString(mEmailView);
                String password = EditTextUtil.getString(mPasswordView);

                if (!(isNetworkAvailable())) {
                    Toast.makeText(LoginActivity.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                } else if (mEmailView.getText().toString() != null && !(mEmailView.getText().toString().isEmpty()) &&
                        mPasswordView.getText().toString() != null && !(mPasswordView.getText().toString().isEmpty())) {
                    LoginBO loginBO = new LoginBO();
                    loginBO.setUserName(EditTextUtil.getString(mEmailView));
                    loginBO.setPassword(EditTextUtil.getString(mPasswordView));
                    System.out.println(EditTextUtil.getString(mPasswordView));
                    System.out.println(EditTextUtil.getString(mEmailView));
                    loginBO = LoginHelper.getLoginBOFromLoginBOs(dbLoginBOs, loginBO);

                    if (loginBO != null && StringUtil.isNotNullOrEmpty(loginBO.getIdLogin())) {
                        if (!(loginBO.getPassword().equals(mPasswordView.getText().toString()))) {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            //   Toast.makeText(LoginActivity.this, "Password incorrect", Toast.LENGTH_LONG).show();
                        } else if (StringUtil.isNotNullOrEmpty(loginBO.getIdLogin())) {
                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(ElementConstants.Id, loginBO.getIdLogin());
                            editor.putString(ElementConstants.Name, EditTextUtil.getString(mEmailView));
                            editor.putString(ElementConstants.password, EditTextUtil.getString(mPasswordView));


                            String userFullName = "";
                            if (StringUtil.isNotNullOrEmpty(loginBO.getFirstName()) && StringUtil.isNotNullOrEmpty(loginBO.getLastName())) {
                                userFullName = loginBO.getFirstName() + " " + loginBO.getLastName();
                            } else {
                                userFullName = loginBO.getFirstName();
                            }
                            editor.putString(ElementConstants.userFirstLastName, userFullName);

                            editor.commit();

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {

                                            progressDialog.dismiss();
                                        }
                                    }, 2000);

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("dbLoginBO", loginBO);

                            /*Bundle b = new Bundle();
                            b.putString(ElementConstants.Id, loginBO.getIdLogin());
                            b.putString(ElementConstants.Name, mEmailView.getText().toString());
                            b.putString(ElementConstants.password, mPasswordView.getText().toString());
                            i.putExtras(b);
*/
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Username incorrect", Toast.LENGTH_LONG).show();

                    }
                } else if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    mEmailView.requestFocus();
                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();

                } else if (TextUtils.isEmpty(password)) {
                    mPasswordView.setError(getString(R.string.error_empty_password));
                    mPasswordView.requestFocus();

                }
                // Check for a valid password, if the user entered one.
                else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    mPasswordView.requestFocus();

                }


            }
            // }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public final void onBackPressed() {

        if (sharedpreferences.contains(ElementConstants.Name)) {

        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

