package primecode.registerplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nagendralimbu on 03/02/2017.
 */

public class MyTokensActivity extends AppCompatActivity implements FragmentButtonClick {

    ArrayList<Token> nhsQuery;

    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;

    public MyTokensActivity() {
        super();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchFirebaseAuthenticationTool();
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container1, new MyTokensFragment(), "mytokensfragment").commit();
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ArrayList<Token> manipulateFirebaseOutput(HashMap<String, Object> output) {
        return RegisterPlusSupport.manipulateFirebaseOutput(output);
    }

    public void replaceFragments(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container1, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(addToBackStack) {
            ft.addToBackStack(fragment.getTag());
        }else {
            //getSupportFragmentManager().popBackStack();
        }


        ft.commit();
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

    @Override
    public HashMap<String, Object> queryNhsNumber(String nhsNumber) {
        nhsQuery = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("/users/" + nhsNumber + "/");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                if(dataSnapshot.hasChildren()){
                    nhsQuery = manipulateFirebaseOutput(dataSnapshot.getValue(t));
                    //makeToast("Database Read is Good. Size => " + nhsQuery.size());
                }
                replaceFragments(new NhsQueryFragment(), true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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



    public ArrayList<Token> getNhsQueryArray(){
        return nhsQuery;
    }
    @Override
    public void fragmentButtonClicked(String fullName, String address, String selectedSpinner, String nhsNumber) {}
    @Override
    public ArrayList<Token> getAllTokens() {return null;}
}
