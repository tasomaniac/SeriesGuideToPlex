package com.tasomaniac.seriesguide.plex;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;

import com.battlelancer.seriesguide.api.Action;
import com.battlelancer.seriesguide.api.Episode;
import com.battlelancer.seriesguide.api.SeriesGuideExtension;

public class PlexExtension extends SeriesGuideExtension {

    public PlexExtension() {
        super("PlexExtension");
    }

    @Override
    protected void onRequest(int episodeIdentifier, Episode episode) {

        Intent plexSearchIntent = new Intent(Intent.ACTION_SEARCH)
                .setComponent(new ComponentName("com.plexapp.android", "com.plexapp.plex.activities.SearchActivity"))
                .putExtra(SearchManager.QUERY, episode.getTitle());

        publishAction(new Action.Builder("Google search", episodeIdentifier)
                .viewIntent(plexSearchIntent)
                .build());
    }
}
