package com.tasomaniac.seriesguide.plex;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;

import com.battlelancer.seriesguide.api.Action;
import com.battlelancer.seriesguide.api.Episode;
import com.battlelancer.seriesguide.api.SeriesGuideExtension;

import javax.inject.Inject;

public class PlexExtension extends SeriesGuideExtension {

    @Inject Analytics analytics;

    public PlexExtension() {
        super("PlexExtension");
    }

    @Override
    protected void onRequest(int episodeIdentifier, Episode episode) {

        Intent plexSearchIntent = new Intent(Intent.ACTION_SEARCH)
                .setComponent(new ComponentName("com.plexapp.android", "com.plexapp.plex.activities.SearchActivity"))
                .putExtra(SearchManager.QUERY, episode.getTitle());

        analytics.sendScreenView("Extension");
        analytics.sendEvent("Extension", "Search Plex", episode.getTitle());

        publishAction(new Action.Builder(getString(R.string.action_extension), episodeIdentifier)
                .viewIntent(plexSearchIntent)
                .build());
    }
}
