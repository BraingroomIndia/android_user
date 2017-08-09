package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.utils.BadgeDrawable;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.viewmodel.HomeViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hanks.htextview.base.HTextView;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SupportMapFragment mapFragment;
    RxPermissions rxPermissions;
    RxLocation rxLocation;
    LocationRequest locationRequest;


    private static final String TAG = HomeActivity.class.getSimpleName();
    private HTextView textView;
    private RelativeLayout competitionBanner;
    private MenuItem itemNotification;
    private MenuItem itemMessage;

    private Disposable timerDisposable;

    public interface UiHelper {
        void setCount();
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        if (bundle != null)

        {

            Bundle data = new Bundle();
            String referralCode = getIntentString("referralCode");
            if (referralCode != null && !vm.getLoggedIn()) {
                data.putString("referralCode", referralCode);
                getNavigator().navigateActivityForResult(LoginActivity.class, data, ViewModel.REQ_CODE_LOGIN);
            }


        }
        /*for (String key : bundleReceived.keySet()) {
            Object value = bundleReceived.get(key);
            Log.d(TAG, "extras " +String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
    }*/



       /* try {
            PackageInfo info = getPackageManager().getPackageInfo("com.braingroom.user", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash :  \t", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (
                PackageManager.NameNotFoundException e)

        {
            //e.printStackTrace();
        } catch (
                NoSuchAlgorithmException e)

        {
            //  e.printStackTrace();
        }
*/
        initMap();

        rxLocation = new

                RxLocation(this);

        locationRequest = LocationRequest.create()
                .

                        setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .

                        setNumUpdates(2)
                .

                        setInterval(5000);


        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
                if (granted) {
                    rxLocation.location().updates(locationRequest).subscribe(new Consumer<Location>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Location location) throws Exception {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOption = new MarkerOptions().position(myLocation).title("Your Location").
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_0));
                            ((HomeViewModel) vm).mGoogleMap.addMarker(markerOption).setTag(myLocation.latitude + "," + myLocation.longitude);
                            ((HomeViewModel) vm).refreshMapPinsToNewLocation("" + location.getLatitude(), "" + location.getLongitude());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Log.d("Location", "accept: ");
                        }
                    });
                } else {
                    getMessageHelper().show("Showing default location as chennai");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNavigationDrawer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void initNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        if (vm.getLoggedIn())
            navigationView.inflateMenu(R.menu.activity_home_drawer_loggedin);
        else
            navigationView.inflateMenu(R.menu.activity_home_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        //  hideItem();


    }


    public void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ((HomeViewModel) vm).setGoogleMap(googleMap);
            }
        });

    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new HomeViewModel(getMessageHelper(), getNavigator(), getHelperFactory().createDialogHelper(), new UiHelper() {
            @Override
            public void setCount() {
                setBadgeCount(itemNotification, HomeActivity.this, ViewModel.notificationCount);
                setBadgeCount(itemMessage, HomeActivity.this, ViewModel.messageCount);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        itemNotification = menu.findItem(R.id.action_notifications);
        itemMessage = menu.findItem(R.id.action_messages);
        setBadgeCount(itemNotification, this, ViewModel.notificationCount);
        setBadgeCount(itemMessage, this, ViewModel.messageCount);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {
/*//            ((ActivityHomeBinding) binding).setVmConnect(new ConnectHomeViewModel(getMessageHelper(), getNavigator()));
//            ((ActivityHomeBinding) binding).setVm(null);
//            binding.getRoot().invalidate();
//            initNavigationDrawer();*/
            Intent intent = new Intent(this, ConnectHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getNavigator().navigateActivity(intent);
            return true;
        }
        if (id == R.id.action_messages) {
            if (!vm.getLoggedIn()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", HomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can send a message", data);
                return true;
            }
            ViewModel.messageCount = 0;
            getNavigator().navigateActivity(MessageActivity.class, null);
//            vm.retry();
            return true;
        }
        if (id == R.id.action_notifications) {
            if (!vm.getLoggedIn()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", HomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can see notification", data);
                return true;
            }
            ViewModel.notificationCount = 0;
            getNavigator().navigateActivity(NotificationActivity.class, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            getNavigator().navigateActivity(ProfileActivity.class, null);
        }
        if (id == R.id.nav_wishlist) {
            Bundle data = new Bundle();
            data.putString("listType", "wishlist");
            getNavigator().navigateActivity(ClassSimpleListActivity.class, data);
        }
        if (id == R.id.nav_booking_history) {
            Bundle data = new Bundle();
            data.putString("listType", "bookingHistory");
            getNavigator().navigateActivity(ClassSimpleListActivity.class, data);
        }
        if (id == R.id.nav_logout) {
            getMessageHelper().showAcceptableInfo("Log out?", "Are you sure you want to log out of the app", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    vm.logOut();
                    Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
            });
        }
        if (id == R.id.nav_login) {
            Bundle data = new Bundle();
            data.putString("backStackActivity", HomeActivity.class.getSimpleName());
            getNavigator().navigateActivityForResult(LoginActivity.class, data, ViewModel.REQ_CODE_LOGIN);
        }
        if (id == R.id.nav_register) {
            getNavigator().navigateActivity(SignupActivity.class, null);
//            finish();
        }
        if (id == R.id.nav_faq)
            //Edited By Vikas Godara
            getNavigator().navigateActivity(FAQActivity.class, null);
        if (id == R.id.nav_tnc)
            getNavigator().navigateActivity(TermsConditionActivity.class, null);
        if (id == R.id.nav_contact)
            getNavigator().navigateActivity(ContactUsActivity.class, null);
        if (id == R.id.nav_change_pass)
            getNavigator().navigateActivity(ChangePasswordActivity.class, null);

        if (id == R.id.nav_catalogue) {
            getNavigator().navigateActivity(CatalogueHomeActivity.class, null);
//            finish();
        }
        if (id == R.id.nav_competition)
            getNavigator().navigateActivity(CompetitionWebPage.class, null);
       /* if (id == R.id.nav_giftcard) {
            getNavigator().navigateActivity(GiftcardCouponActivity.class, null);
//            finish();
        }*/

        //Edited By Vikas Godara
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setBadgeCount(MenuItem item, Context context, int count) {
        if (item != null && vm.getLoggedIn()) {
            BadgeDrawable badge;
            LayerDrawable icon = (LayerDrawable) item.getIcon();
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof BadgeDrawable) {
                badge = (BadgeDrawable) reuse;
            } else {
                badge = new BadgeDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((HomeViewModel) vm).profileImage.set(pref.getString(Constants.PROFILE_PIC, null));
        ((HomeViewModel) vm).userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        ((HomeViewModel) vm).userEmail.set(pref.getString(Constants.EMAIL, null));
        if (vm.getLoggedIn())
            navigationView.getMenu().clear();

//            if (vm.getLoggedIn()) navigationView.inflateMenu(R.menu.activity_home_drawer_loggedin);
//            else navigationView.inflateMenu(R.menu.activity_home_drawer);
    }
}
