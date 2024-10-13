package com.trungngo.carshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trungngo.carshareapp.Constants;
import com.trungngo.carshareapp.R;
import com.trungngo.carshareapp.model.User;
import com.trungngo.carshareapp.ui.customer.booking.BookingViewModel;
import com.trungngo.carshareapp.ui.customer.booking.checkout.CheckoutViewModel;
import com.trungngo.carshareapp.ui.customer.booking.dropoff.DropoffViewModel;
import com.trungngo.carshareapp.ui.customer.booking.pickup.PickupViewModel;
import com.trungngo.carshareapp.ui.customer.home.CustomerHomeViewModel;
import com.trungngo.carshareapp.ui.driver.home.DriverHomeViewModel;
import com.trungngo.carshareapp.ui.user_profile.UserProfileViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView navHeaderEmailTextView;
    private TextView navHeaderUsernameTextView;

    //Firebase, FireStore
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    //Current user info
    User currentUserObject = null;

    //View models
    CustomerHomeViewModel customerHomeViewModel;
    DriverHomeViewModel driverHomeViewModel;
    DropoffViewModel dropoffViewModel;
    PickupViewModel pickupViewModel;
    BookingViewModel bookingViewModel;
    CheckoutViewModel checkoutViewModel;
    UserProfileViewModel userProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //setup navigation drawer
        linkViewElements(); //Get view elements
        initAllChildFragmentsViewModel(); //Init all child fragments viewModels
        initFirebaseCurrentUserInfo(); //Get all fireStore instances
    }

    /**
     * Connect view elements of layout to this class variable
     */
    private void linkViewElements() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        LinearLayout navHeaderView = (LinearLayout) navigationView.getHeaderView(0);
        navHeaderUsernameTextView = (TextView) navHeaderView.getChildAt(1);
        navHeaderEmailTextView = (TextView) navHeaderView.getChildAt(2);
    }

    /**
     * Set up navigation drawer activity
     */
    private void navigationDrawerSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_customer_home,
                R.id.nav_driver_home,
                R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigateAndHideAccordingMenuBasedOnRole(navController);
    }

    /**
     * Logout menu item listener (sits in 3-dots collapsing menu)
     */
    private void onLogoutOptionClick() {
        mAuth.signOut();
        Intent i = new Intent(MainActivity.this, StartActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_logout:
                onLogoutOptionClick();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Get instances of Firebase FireStore Auth, db, current user
     */
    private void initFirebaseCurrentUserInfo() {
        //Get instances of Firebase FireStore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        getCurrentUserObject(); //Get current user object info
    }

    /**
     * Get current user object from FireStore
     */
    private void getCurrentUserObject() {
        db.collection(Constants.FSUser.userCollection)
                .whereEqualTo(Constants.FSUser.emailField, currentUser.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            currentUserObject = doc.toObject(User.class);
                            setNavHeaderEmailAndUsername(); //Set nav header username and email
                            setAllChildFragmentsViewModelData();
                            navigationDrawerSetup();
                        }
                    }
                });
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (QueryDocumentSnapshot doc : value) {
//                            currentUserObject = doc.toObject(User.class);
//                            setNavHeaderEmailAndUsername(); //Set nav header username and email
//                            setAllChildFragmentsViewModelData();
//                            navigationDrawerSetup();
//                        }
//                    }
//                });
    }

    /**
     * navigate to the right home and remove according home menu based on user role
     * @param navController navController object of this layout
     */
    private void navigateAndHideAccordingMenuBasedOnRole(NavController navController){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        //Hide according menu and Navigate to the right fragment based on
        if (currentUserObject.getRole().equals("Customer")){
            MenuItem driverHomeMenuItem = menu.getItem(1);
            driverHomeMenuItem.setVisible(false);
            navController.navigate(R.id.nav_customer_home);
        } else {
            MenuItem customerHomeMenuItem = menu.getItem(0);
            customerHomeMenuItem.setVisible(false);
            navController.navigate(R.id.nav_driver_home);
        }
    }

    /**
     * Init all child fragments' view models
     */
    private void initAllChildFragmentsViewModel() {
        customerHomeViewModel = ViewModelProviders.of(this).get(CustomerHomeViewModel.class);
        driverHomeViewModel = ViewModelProviders.of(this).get(DriverHomeViewModel.class);
        dropoffViewModel = ViewModelProviders.of(this).get(DropoffViewModel.class);
        pickupViewModel = ViewModelProviders.of(this).get(PickupViewModel.class);
        bookingViewModel = ViewModelProviders.of(this).get(BookingViewModel.class);
        checkoutViewModel = ViewModelProviders.of(this).get(CheckoutViewModel.class);
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
    }

    /**
     * Set nav header username and email
     */
    private void setNavHeaderEmailAndUsername() {
        navHeaderEmailTextView.setText(currentUser.getEmail());
        navHeaderUsernameTextView.setText(currentUserObject.getUsername());
    }

    /**
     * Send current user data through child fragments' view models
     */
    private void setAllChildFragmentsViewModelData() {
        if (currentUserObject.getRole().equals("Customer")){
            customerHomeViewModel.setCurrentUserObject(currentUserObject);
        } else {
            driverHomeViewModel.setCurrentUserObject(currentUserObject);
        }
        dropoffViewModel.setCurrentUserObject(currentUserObject);
        pickupViewModel.setCurrentUserObject(currentUserObject);
        bookingViewModel.setCurrentUserObject(currentUserObject);
        userProfileViewModel.setCurrentUserObject(currentUserObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}