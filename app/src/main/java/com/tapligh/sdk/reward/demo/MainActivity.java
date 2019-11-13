package com.tapligh.sdk.reward.demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tapligh.sdk.adview.Tapligh;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout tabLayout;

    private Tapligh tapligh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tapligh = Tapligh.newInstance(this);
        tapligh.setToken("a1448e3f-963a-4ccf-8919-eceeb054e4f8", false);

        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.getTabAt(1).select();
        navigateTo(MainFragment.AD_TYPE_REWARD);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {    // banner
                    navigateTo(MainFragment.AD_TYPE_BANNER);
                } else {    // reward
                    navigateTo(MainFragment.AD_TYPE_REWARD);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_version_name:
                showMessage(tapligh.getTaplighVersion());
                return true;

            case R.id.item_is_initialize:
                showMessage(tapligh.isInitializeDone() ? "initialized" : "not initialized");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String text) {
        if(!isFinishing())
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void navigateTo(int type) {

        String tag = MainFragment.class.getSimpleName() + "," + type;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = MainFragment.getInstance(type);
            transaction.replace(R.id.container, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .commit();
    }


}
