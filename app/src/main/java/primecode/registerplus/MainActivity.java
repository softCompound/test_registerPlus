package primecode.registerplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentButtonClick {
    private boolean onSaveInstanceState = false;

    ArrayList<Token> allTokens = new ArrayList<>();

    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void fragmentButtonClicked(String nhsNumber, String fullName, String address, String selectedSpinner) {
        signInAnonymously();
        //Simple validation completed | create new Activity | populate the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        //create Token object
        Token token = new Token(nhsNumber, fullName, address, selectedSpinner, timeStamp);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("/users/" + nhsNumber + "/");
        database.child(timeStamp).setValue(token);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                allTokens = manipulateFirebaseOutput(dataSnapshot.getValue(t));
                replaceFragments(new TokenDisplayFragment(), true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void replaceFragments(Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_container1, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(addToBackStack) {
            manager.popBackStack();
            ft.addToBackStack(fragment.getTag());
        }
        try{
            ft.commit();
        }catch (IllegalStateException e) {
            FirebaseCrash.log("Exception Caught at ft.commit():" + e.getMessage());
        }
    }

    public ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output) {
        return RegisterPlusSupport.manipulateFirebaseOutput(output);
    }

    @Override
    public boolean validateFirebaseDbInput(String string) {
        if(string.length() > 0) {
            String[] array = new String[] {".", "[", "$", "]", "#"};

            for(String check: array){
                if(string.contains(check)) {
                    Toast.makeText(this, "Name or NHS Number cannot contain character [ " + check + " ]", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

        } else {
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchFirebaseAuthenticationTool();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container1, new RegistrationFragment(), "home").commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void launchFirebaseAuthenticationTool() {
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    signInAnonymously();
                }
                // ...
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        Log.e("On Stop boolState= " , ""+onSaveInstanceState);
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        Log.e("On start boolState= " , ""+onSaveInstanceState);

        super.onStart();
        onSaveInstanceState = false;
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        onSaveInstanceState = true;
        Log.e("onPause boolState= " , ""+onSaveInstanceState);
        super.onPostResume();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("OnsaveInstanceCalled= " , ""+onSaveInstanceState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        Log.e("onrestart bool= " , ""+onSaveInstanceState);
        onSaveInstanceState = false;
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myTokens) {
            Intent intent = new Intent(this, MyTokensActivity.class);
            startActivity(intent);

        } else if (id == R.id.aboutUs) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signInAnonymously() {

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInAnonymously", task.getException());
                        }
                    }
                });
    }

    public void makeToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ArrayList<Token> getAllTokens() {
        return allTokens;
    }



    @Override
    public ArrayList<Token> getNhsQueryArray() {return null;}
    @Override
    public HashMap<String, Object> queryNhsNumber(String nhsNumber) {return null;}
}
