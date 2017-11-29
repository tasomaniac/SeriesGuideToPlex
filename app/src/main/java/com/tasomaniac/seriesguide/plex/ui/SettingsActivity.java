package com.tasomaniac.seriesguide.plex.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.tasomaniac.seriesguide.plex.R;

public class SettingsActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_action_done);
    toolbar.setNavigationContentDescription(R.string.done);
    setSupportActionBar(toolbar);

    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
    collapsingToolbar.setTitle(getTitle());

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, SettingsFragment.newInstance())
          .commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {

      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      if (prefs.getBoolean(getString(R.string.pref_key_seriesguide_integration), false)) {
        Toast.makeText(this, R.string.error_install_seriesguide, Toast.LENGTH_LONG).show();
      } else {
        finish();
      }
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
