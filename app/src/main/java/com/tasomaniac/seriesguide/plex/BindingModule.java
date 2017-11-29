package com.tasomaniac.seriesguide.plex;

import com.tasomaniac.seriesguide.plex.ui.SettingsFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
interface BindingModule {

  @ContributesAndroidInjector
  SettingsFragment settingsFragment();

  @ContributesAndroidInjector
  PlexExtension plexExtension();
}
