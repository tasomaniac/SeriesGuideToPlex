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
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.tasomaniac.android.widget.IntegrationPreference;
import com.tasomaniac.seriesguide.plex.App;
import com.tasomaniac.seriesguide.plex.R;
import com.tasomaniac.seriesguide.plex.data.prefs.StringPreference;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String LAUNCHER_ACTIVITY_NAME = "com.tasomaniac.seriesguide.plex.ui.MainActivity";

    private IntegrationPreference seriesGuidePref;
    private IntegrationPreference plexPref;


    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(boolean forceShowPermission) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putBoolean(SettingsActivity.EXTRA_FROM_BACKGROUND, forceShowPermission);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        App.get(getActivity()).component().inject(this);
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

    /**
     * A preference value change listener that updates the preference's summary to reflect its new
     * value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? (listPreference.getEntries()[index])
                                .toString().replaceAll("%", "%%")
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the preference's value is
     * changed, its summary (line of text below the preference title) is updated to reflect the
     * value. The summary is also immediately updated upon calling this method. The exact display
     * format is dependent on the type of preference.
     */
    public static void bindPreferenceSummaryToValue(Preference preference, StringPreference pref) {
        setAndCallPreferenceChangeListener(preference, sBindPreferenceSummaryToValueListener, pref);
    }

    /**
     * When the preference's value is changed, trigger the given listener. The listener is also
     * immediately called with the preference's current value upon calling this method.
     */
    public static void setAndCallPreferenceChangeListener(Preference preference,
                                                          Preference.OnPreferenceChangeListener listener,
                                                          StringPreference pref) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(listener);

        // Trigger the listener immediately with the preference's
        // current value.
        listener.onPreferenceChange(preference, pref.get());
    }
}
