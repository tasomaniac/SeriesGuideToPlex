package com.tasomaniac.seriesguide.plex;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import com.battlelancer.seriesguide.api.Action;
import com.battlelancer.seriesguide.api.Episode;
import com.battlelancer.seriesguide.api.Movie;
import com.battlelancer.seriesguide.api.SeriesGuideExtension;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class PlexExtension extends SeriesGuideExtension {

  @Inject Analytics analytics;

  public PlexExtension() {
    super("PlexExtension");
  }

  @Override
  public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override
  protected void onRequest(int episodeIdentifier, Episode episode) {
    publishActionWithTitleSearch(episodeIdentifier, episode.getTitle());
  }

  @Override
  protected void onRequest(int movieIdentifier, Movie movie) {
    publishActionWithTitleSearch(movieIdentifier, movie.getTitle());
  }

  private void publishActionWithTitleSearch(int identifier, String title) {
    Intent plexSearchIntent = new Intent(Intent.ACTION_SEARCH)
        .setComponent(new ComponentName("com.plexapp.android", "com.plexapp.plex.activities.SearchActivity"))
        .putExtra(SearchManager.QUERY, title);

    analytics.sendScreenView("Extension");
    analytics.sendEvent("Extension", "Search Plex", title);

    publishAction(new Action.Builder(getString(R.string.action_extension), identifier)
        .viewIntent(plexSearchIntent)
        .build());
  }
}
