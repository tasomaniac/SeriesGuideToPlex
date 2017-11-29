package com.tasomaniac.seriesguide.plex;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    BindingModule.class
})
public interface AppComponent extends AndroidInjector<App> {

  void inject(PlexExtension extension);

  @Component.Builder
  abstract class Builder extends AndroidInjector.Builder<App> {
  }
}
