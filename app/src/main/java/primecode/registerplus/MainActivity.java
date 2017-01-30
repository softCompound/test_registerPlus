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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MyActivity";

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
            }

        } else if (id == R.id.myTokens) {
            //start a new Activity here.
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("myTokens");
            if(fragment != null) {
                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().add(fragment, "myTokens").commit();
                replaceFragments(fragment, false);
            }else {
                //create new aboutUs Fragment
                Fragment myToken = new MyTokensFragment();
                replaceFragments(myToken, true);
            }

        } else if (id == R.id.aboutUs) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("aboutUs");
            if(fragment != null) {
                //Toast.makeText(this, "This is not ABOUT US", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().add(fragment, "aboutUs").commit();
                replaceFragments(fragment, false);
            }else {
                //create new aboutUs Fragment
                Fragment aboutUs = new AboutUsFragment();
                replaceFragments(aboutUs, true);
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     */
    public void replaceFragments(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container1, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(addToBackStack) ft.addToBackStack(fragment.getTag());

        ft.commit();
    }
}
