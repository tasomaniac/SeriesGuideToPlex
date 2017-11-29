/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tasomaniac.seriesguide.plex.ui;

import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import com.tasomaniac.android.widget.IntegrationPreference;
import com.tasomaniac.seriesguide.plex.Analytics;
import com.tasomaniac.seriesguide.plex.R;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

  @Inject Analytics analytics;

  private static final String LAUNCHER_ACTIVITY_NAME = "com.tasomaniac.seriesguide.plex.ui.MainActivity";

  private IntegrationPreference seriesGuidePref;
  private IntegrationPreference plexPref;

  public SettingsFragment() {
  }

  public static SettingsFragment newInstance() {
    SettingsFragment fragment = new SettingsFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      analytics.sendScreenView("Settings");
    }
  }

  @Override
  public void onCreatePreferences(Bundle bundle, String s) {
    AndroidSupportInjection.inject(this);
    addPreferencesFromResource(R.xml.pref_general);

    seriesGuidePref = (IntegrationPreference) findPreference(R.string.pref_key_seriesguide_integration);
    seriesGuidePref.setPersistent(true);
    plexPref = (IntegrationPreference) findPreference(R.string.pref_key_plex_integration);
    plexPref.setPersistent(true);
  }

  public Preference findPreference(@StringRes int keyResource) {
    return findPreference(getString(keyResource));
  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferenceManager().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
    seriesGuidePref.resume();
    plexPref.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    getPreferenceManager().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
    seriesGuidePref.pause();
    plexPref.pause();
  }

  @SuppressWarnings("ConstantConditions")
  @NonNull
  @Override
  public View getView() {
    return super.getView();
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    new BackupManager(getActivity()).dataChanged();

    // Potentially enable/disable the launcher activity if the settings button
    // preference has changed.
    final String launcherIntentKey = getString(R.string.pref_key_launcher_intent);
    if (isAdded() && launcherIntentKey.equals(s)) {

      final boolean hideLauncher = sharedPreferences.getBoolean(launcherIntentKey, false);
      getActivity().getPackageManager().setComponentEnabledSetting(
          new ComponentName(
              getActivity().getPackageName(),
              LAUNCHER_ACTIVITY_NAME),
          hideLauncher
              ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED
              : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
          PackageManager.DONT_KILL_APP);
    }
  }
}
