package primecode.registerplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentButtonClick {


    @Override
    public void fragmentButtonClicked() {
        EditText fullname = (EditText) findViewById(R.id.editTextName);
        EditText address = (EditText) findViewById(R.id.editTextAddress);

        Spinner spinner = (Spinner) findViewById(R.id.reg_spinner);
        if (spinner.getSelectedItem().toString().trim().equals("Select from the list") ||
                (fullname.getText().toString().trim().length() < 1 ||
                        address.getText().toString().trim().length() < 1) ) {
            Toast.makeText(this, "Please Complete the Form.", Toast.LENGTH_SHORT).show();
        } else {
            //validation completed | create new Activity | populate the database
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (id == R.id.home) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("home");
            if(fragment != null) {
                //String s = fragment.getClass().getName();
                //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                replaceFragments(fragment, false);
            } else {
                replaceFragments(new RegistrationFragment(), true);
            }


        } else if (id == R.id.myTokens) {
            //start a new Activity here.
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("myTokens");
            if(fragment != null) {
                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().add(fragment, "myTokens").commit();
                replaceFragments(fragment, false);
            }else {
                boolean addToBackStack = true;
                //create new aboutUs Fragment
                Fragment myToken = new MyTokensFragment();
                if(getSupportFragmentManager().findFragmentById(R.id.fragment_container1) instanceof AboutUsFragment) {
                    addToBackStack = false;
                }
                replaceFragments(myToken, addToBackStack);
            }

        } else if (id == R.id.aboutUs) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("aboutUs");
            if(fragment != null) {
                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().add(fragment, "aboutUs").commit();
                replaceFragments(fragment, false);
            }else {
                boolean addToBackStack = true;
                //create new aboutUs Fragment
                Fragment aboutUs = new AboutUsFragment();
                //create new aboutUs Fragment
                if(getSupportFragmentManager().findFragmentById(R.id.fragment_container1) instanceof MyTokensFragment) {
                    addToBackStack = false;
                }
                replaceFragments(aboutUs, addToBackStack);
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragments(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container1, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(addToBackStack) {
            ft.addToBackStack(fragment.getTag());
        } else {
            getSupportFragmentManager().popBackStack();
        }


        ft.commit();
    }
}
