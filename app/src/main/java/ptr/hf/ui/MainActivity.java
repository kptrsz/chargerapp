package ptr.hf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ptr.hf.R;
import ptr.hf.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getSupportFragmentManager();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.veryTransparentColorPrimaryDark));

        toolbar.setTitle(R.string.map);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, new ptr.hf.ui.MapFragment())
                .commit();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        View headerLayout = navView.getHeaderView(0);
        ImageView profileImage = (ImageView) headerLayout.findViewById(R.id.profile_picture);
        TextView profileName = (TextView) headerLayout.findViewById(R.id.profile_name);
        TextView profileEmail = (TextView) headerLayout.findViewById(R.id.profile_email);
        if (user.getPhotoUrl() != null)
        Picasso
                .with(this)
                .load(user.getPhotoUrl())
                .into(profileImage);
        if (user.getDisplayName() != null)
            profileName.setText(user.getDisplayName());
        else
            profileName.setText("Vendég");

        if (user.getEmail() != null)
            profileEmail.setText(user.getEmail());
        else
            profileEmail.setText("");



        navView.setNavigationItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = new ptr.hf.ui.MapFragment();

        if (id == R.id.nav_maps) {
            toolbar.setTitle(R.string.map);
            fragment = new ptr.hf.ui.MapFragment();
        } else if (id == R.id.nav_reservation) {
            toolbar.setTitle(R.string.reservation);
            fragment = new ReservationFragment();
        } else if (id == R.id.nav_stats) {
            toolbar.setTitle(R.string.statistics);
//            fragment = new StatisticsFragment();
        } else if (id == R.id.nav_settings) {
            toolbar.setTitle(R.string.settings);
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_info) {
            toolbar.setTitle(R.string.information);
//            fragment = new InformationFragment();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        drawerLayout
                .closeDrawer(GravityCompat.START);
        return true;
    }
}
