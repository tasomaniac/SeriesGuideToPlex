package com.tasomaniac.seriesguide.plex;

import com.tasomaniac.seriesguide.plex.ui.SettingsFragment;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class
})
public interface AppComponent extends AndroidInjector<App> {

  void inject(SettingsFragment fragment);

  void inject(PlexExtension extension);

  @Component.Builder
  abstract class Builder extends AndroidInjector.Builder<App> {
  }
}
