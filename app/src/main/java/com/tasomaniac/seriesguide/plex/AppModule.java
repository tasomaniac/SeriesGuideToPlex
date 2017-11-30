package com.tasomaniac.seriesguide.plex;

import android.content.pm.PackageManager;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * A module for Android-specific dependencies which require a Context to create.
 * <p>
 * Created by Said Tahsin Dane on 17/03/15.
 */
@Module
abstract class AppModule {

  @Provides
  @Singleton
  static Analytics provideAnalytics() {
    if (BuildConfig.DEBUG) {
      return new Analytics.DebugAnalytics();
    }
    return new AnswersAnalytics();
  }

  @Provides
  static PackageManager packageManager(App app) {
    return app.getPackageManager();
  }

  private static class AnswersAnalytics implements Analytics {

    private final Answers answers = Answers.getInstance();

    @Override
    public void sendScreenView(String screenName) {
      answers.logContentView(new ContentViewEvent().putContentName(screenName));
    }

    @Override
    public void sendEvent(String category, String action, String label) {
      answers.logCustom(new CustomEvent(category)
          .putCustomAttribute(action, label));
    }
  }
}
