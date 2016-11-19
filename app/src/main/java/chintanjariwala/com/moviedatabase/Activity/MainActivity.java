package chintanjariwala.com.moviedatabase.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import chintanjariwala.com.moviedatabase.fragment.HomeFragment;
import chintanjariwala.com.moviedatabase.fragment.SearchResultsFragment;
import chintanjariwala.com.moviedatabase.fragment.TopRatedFragment;
import chintanjariwala.com.moviedatabase.fragment.TrendingFragment;
import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.fragment.TvAiringTodayFragment;
import chintanjariwala.com.moviedatabase.fragment.TvNowPlayingFragment;
import chintanjariwala.com.moviedatabase.fragment.TvTrendingFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, TrendingFragment.OnFragmentInteractionListener
{
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView tvNavName;
    private TextView tvNavEmail = null;
    private Toolbar toolbar;
    private EditText searchText;
    private Button searchAnything;
    //Base URL to make calls

    //index for current nav_menu item
    private static int navItemIndex = 0;


    private static final String TAG = MainActivity.class.getSimpleName();

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_TRENDING = "photos";
    private static final String TAG_TOP = "movies";
    private static final String TAG_POPULAR_TV = "popularTV";
    private static final String TAG_TRENDING_TV = "TrendingTV";
    private static final String TAG_AIR_TODAY = "AirTodayTV";
    private static final String TAG_SEARCH_RESULT = "SearchResult";

    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the contents
        init();

        setSupportActionBar(toolbar);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        //Setup the navigation bar
        setupNavBar();

        setupNavigationView();

        if (savedInstanceState == null){
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        searchAnything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("searchQuery",searchText.getText().toString().trim());
                Fragment fragment = new SearchResultsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG_SEARCH_RESULT);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_trending:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_TRENDING;
                        break;
                    case R.id.nav_top:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_TOP;
                        break;
                    case R.id.nav_tv_latest:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_POPULAR_TV;
                        break;
                    case R.id.nav_tv_trending:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_TRENDING_TV;
                        break;
                    case R.id.nav_tv_today:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_AIR_TODAY;
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {
        selectNavMenu();
        
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                TrendingFragment trendingFragment= new TrendingFragment();
                return trendingFragment;
            case 2:
                TopRatedFragment topRatedFragment = new TopRatedFragment();
                return topRatedFragment;
            case 3:
                TvTrendingFragment tvrending = new TvTrendingFragment();
                return tvrending;
            case 4:
                TvNowPlayingFragment tvNowPlayingFragment = new TvNowPlayingFragment();
                return tvNowPlayingFragment;
            case 5:
                TvAiringTodayFragment tvAiringTodayFragment = new TvAiringTodayFragment();
                return tvAiringTodayFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        Log.d(TAG, "selectNavMenu: " + navigationView.getMenu().toString());
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }



    private void setupNavBar() {
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string .myPrefs), Context.MODE_PRIVATE);
        String name  = sharedPreferences.getString("name","Test");
        String email = sharedPreferences.getString("email","Test");
        Log.d(TAG, "setupNavBar: "+ name + " " + email);
//        if(name != null && email != null){
//            tvNavEmail.setText(email);
//            tvNavName.setText(name);
//        }

    }



    private void init() {
        //basic items
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //header items
        navHeader = navigationView.getHeaderView(0);
        tvNavName = (TextView) navHeader.findViewById(R.id.drawer_layout);
        tvNavEmail = (TextView) navHeader.findViewById(R.id.tvNavEmail);

        //For the search items
        searchAnything = (Button) findViewById(R.id.searchAnythingButton);
        searchText = (EditText) findViewById(R.id.searchAnything);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
