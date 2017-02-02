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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentButtonClick {
    private static final String MESSAGE = "registerPlus";
    ArrayList<Token> allTokens;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void fragmentButtonClicked(String fullName, String address, String selectedSpinner, String nhsNumber) {
            signInAnonymously();
            //Simple validation completed | create new Activity | populate the database
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String timeStamp = dateFormat.format(date);
            Toast.makeText(this, timeStamp , Toast.LENGTH_SHORT).show();
            //create Token object
            Token token = new Token(fullName, address, selectedSpinner, nhsNumber, timeStamp);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("/users/" + nhsNumber + "/");
            database.child(timeStamp).setValue(token);

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    allTokens = manipulateFirebaseOutput(dataSnapshot.getValue(t));
                    createMyTokenFragment();
//                    HashMap<String, Object> s = dataSnapshot.getValue(t);
//                        Toast.makeText(getApplicationContext(), s.keySet().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "dberror " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }

    public void createMyTokenFragment(){
        TokenDisplayFragment tdf = new TokenDisplayFragment();
        replaceFragments(tdf, false);
    }

    public ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output) {
        Set entrySet = output.entrySet();

        if(entrySet.size() > 0) {
            ArrayList<Token> allTokens = new ArrayList<>();
            Iterator it = entrySet.iterator();
            while(it.hasNext()){
                Map.Entry me = (Map.Entry) it.next();

                String tokenText = me.getValue().toString();
                //Toast.makeText(this, tokenText.toString(), Toast.LENGTH_SHORT).show();
                String[] tokenValues = ObjectToClassConverter.filtrStringToClass(tokenText);
                Token token = new Token(tokenValues[4], tokenValues[3],tokenValues[2],tokenValues[1],tokenValues[0]);
                allTokens.add(token);
            }

            return allTokens;
        }

        return null;
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
                    Toast.makeText(getApplicationContext(), "onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_LONG).show();
                } else {
                    // User is signed out
                    signInAnonymously();
                    Toast.makeText(getApplicationContext(), "onAuthStateChanged:signed_out", Toast.LENGTH_LONG).show();
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
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Fragment fragment = fm.findFragmentByTag("home");
            if(fragment != null) {
                //String s = fragment.getClass().getName();
                //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                makeToast("home exixts!");

                replaceFragments(fragment, false);
            } else {
                makeToast("home created!");
                replaceFragments(new RegistrationFragment(), false);
            }


        } else if (id == R.id.myTokens) {
            //start a new Activity here.
            Fragment fragment = fm.findFragmentByTag("myTokens");
            boolean addToBackStack = false;
            if(fm.findFragmentById(R.id.fragment_container1) instanceof RegistrationFragment) {
                addToBackStack = true;
            }
            if(fragment != null) {
                makeToast("mytokenFragment exists");

                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                replaceFragments(fragment, addToBackStack);
            }else {
                makeToast("mytokenFragment created");

                //create new aboutUs Fragment
                Fragment myToken = new MyTokensFragment();
                ft.add(myToken, "myTokens");
                replaceFragments(myToken, addToBackStack);
            }

        } else if (id == R.id.aboutUs) {
            Fragment fragment = fm.findFragmentByTag("aboutUs");
            boolean addToBackStack = false;
            if(fm.findFragmentById(R.id.fragment_container1) instanceof RegistrationFragment) {
                addToBackStack = true;
            }
            if(fragment != null) {
                makeToast("aboutUs exists");

                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                replaceFragments(fragment, addToBackStack);
            }else {
                makeToast("mytokenFragment created");

                //create new aboutUs Fragment
                AboutUsFragment aboutUs = new AboutUsFragment();
                ft.add(aboutUs, "aboutUs");
                replaceFragments(aboutUs, addToBackStack);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        ft.commit();

        return true;
    }

    public void replaceFragments(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container1, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(addToBackStack) {
            ft.addToBackStack(fragment.getTag());
        }


        ft.commit();
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
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
    public HashMap<String, Object> queryNhsNumber(String nhsNumber) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("/users/" + nhsNumber + "/");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
               if(dataSnapshot.hasChildren()){
                   allTokens = manipulateFirebaseOutput(dataSnapshot.getValue(t));
                   if(allTokens.size() > 0) {
                       makeToast("Database Read is Good. Size => " + allTokens.size());
                   }
               }else {
                   makeToast("No result found!");
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return null;
    }
}
