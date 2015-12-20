package com.tasomaniac.seriesguide.plex;


import com.tasomaniac.seriesguide.plex.data.DataModule;
import com.tasomaniac.seriesguide.plex.ui.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(SettingsFragment fragment);

    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        static AppComponent init(App app) {
            return com.tasomaniac.seriesguide.plex.DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }

        private Initializer() {
        } // No instances.
    }
}