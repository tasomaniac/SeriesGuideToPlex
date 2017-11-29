package com.tasomaniac.seriesguide.plex;

import com.battlelancer.seriesguide.api.SeriesGuideExtension;
import com.battlelancer.seriesguide.api.SeriesGuideExtensionReceiver;

public class PlexReceiver extends SeriesGuideExtensionReceiver {

  @Override
  protected int getJobId() {
    return 1;
  }

  @Override
  protected Class<? extends SeriesGuideExtension> getExtensionClass() {
    return PlexExtension.class;
  }
}
