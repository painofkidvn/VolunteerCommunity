package com.hat_dtu.volunteercommunity.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hat_dtu.volunteercommunity.R;
import com.hat_dtu.volunteercommunity.app.AppConfig;
import com.hat_dtu.volunteercommunity.fragment.MyPlaceFragment;
import com.hat_dtu.volunteercommunity.fragment.HomeFragment;
import com.hat_dtu.volunteercommunity.fragment.PlaceFragment;
import com.hat_dtu.volunteercommunity.helper.SQLiteHandler;
import com.hat_dtu.volunteercommunity.helper.SessionManager;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private SQLiteHandler db;
    private SessionManager session;
    private TextView tvName, tvEmail;
    private Fragment temp;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = getIntent().getBundleExtra("MOVING");
            if(AppConfig.isMyPlace == true){
                fragmentTransaction.replace(R.id.container_body, new MyPlaceFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("My Place");
                AppConfig.isMyPlace = false;

            }else {
                if(AppConfig.isMove == false)
                    fragmentTransaction.replace(R.id.container_body, new HomeFragment());
                else {
                    latLng = new LatLng(Double.parseDouble(bundle.getString("LAT")), Double.parseDouble(bundle.getString("LNG")));
                    fragmentTransaction.replace(R.id.container_body, new HomeFragment(latLng));
                }
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Home");
            }


        }
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }




    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

       /* final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        MenuItem searchItem = menu.findItem(R.id.action_search);*/
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        return true;
    }
    private int choice = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (choice){
                case 1:
                    fragmentTransaction.replace(R.id.container_body, new HomeFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Home");
                    break;
                case 2:
                    fragmentTransaction.replace(R.id.container_body, new MyPlaceFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("MyPlace");
                    break;
                case 3:
                    fragmentTransaction.replace(R.id.container_body, new PlaceFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Place");
                    break;
                default:
                    break;
            }
        }
        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout){

            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);

    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                AppConfig.isMove = false;
                title = getString(R.string.title_home);
                choice = 1;
                break;
            case 1:
                fragment = new MyPlaceFragment();
                title = getString(R.string.title_my_place);
                AppConfig.isMove = false;
                choice = 2;
                break;
            case 2:
                fragment = new PlaceFragment();
                title = getString(R.string.title_places);
                AppConfig.isMove = false;
                choice = 3;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
