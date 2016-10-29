package br.com.friendlydonations.views.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import br.com.friendlydonations.R;
import br.com.friendlydonations.managers.BaseActivity;
import br.com.friendlydonations.views.adapters.DynamicTabViewPageAdapter;
import br.com.friendlydonations.views.fragments.DonateFragment;
import br.com.friendlydonations.views.fragments.HomeFragment;
import br.com.friendlydonations.views.fragments.NotificationsFragment;
import br.com.friendlydonations.views.fragments.ProfileFragment;
import br.com.friendlydonations.views.widgets.BadgeViewLayout;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by brunogabriel on 8/27/16.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabs;
    @BindView(R.id.coordinatorLayout) protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appBar) protected AppBarLayout appBar;
    @BindArray(R.array.array_tab_main) protected String []tabArrayNames;

    BadgeViewLayout notificationBadge;

    private int[]tabIcons = {
            R.drawable.ic_home_tab,
            R.drawable.ic_profile_tab,
            R.drawable.ic_profile_tab,
            R.drawable.ic_notifications_tab
    };

    private DynamicTabViewPageAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
    }

    @Override
    public void initUI() {
        setupToolbar(toolbar, "", null, false, false);
        setupTabs(viewPager);
        applyTypefaceToolbar(toolbar, TypefaceUtils.load(getAssets(), "fonts/Montserrat-Regular.ttf"));
    }

    private void setupTabs(final ViewPager viewPager) {
        viewPagerAdapter = new DynamicTabViewPageAdapter(getSupportFragmentManager());
        if(viewPager!=null) {
            toolbar.setTitle(tabArrayNames[0]);
            try {
                // Adding Fragments
                viewPagerAdapter.addFragment(new HomeFragment(), tabArrayNames[0] );
                // viewPagerAdapter.addFragment(new MapLocationFragment(), tabArrayNames[1]);
                viewPagerAdapter.addFragment(new DonateFragment(), tabArrayNames[1]);
                viewPagerAdapter.addFragment(new ProfileFragment(), tabArrayNames[2]);
                viewPagerAdapter.addFragment(new NotificationsFragment(), tabArrayNames[3]);

                viewPager.setAdapter(viewPagerAdapter);
                tabs.setupWithViewPager(viewPager);
                tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.getCustomView().setAlpha(1.0f);
                        changeTab(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.getCustomView().setAlpha(0.5f);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        tab.getCustomView().setAlpha(1.0f);
                    }
                });

                // Custom View
                for (int i = 0; i < tabIcons.length; ++i) {

                    RelativeLayout mTabLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tabbadge, null, false);
                    BadgeViewLayout mBadge = new BadgeViewLayout(mTabLayout);
                    mBadge.initUi(R.id.badgeImage, R.id.rlBadge, R.id.textBadge);
                    Picasso.with(this).load(tabIcons[i]).into(mBadge.getBadgeImage());

                    // Save notification tab
                    if (i == tabIcons.length-1) {
                        notificationBadge = mBadge;
                        mBadge.changeVisibility();
                    }

                    tabs.getTabAt(i).setCustomView(mTabLayout);

                    // Apply alpha in inactive tabs
                    if ( i != 0) {
                        tabs.getTabAt(i).getCustomView().setAlpha(0.5f);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void changeTab(TabLayout.Tab tab) {
        if (tab != null) {
            viewPager.setCurrentItem(tab.getPosition());
            toolbar.setTitle(tabArrayNames[tab.getPosition()]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }
}