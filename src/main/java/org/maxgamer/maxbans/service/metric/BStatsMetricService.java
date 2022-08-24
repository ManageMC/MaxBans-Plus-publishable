package org.maxgamer.maxbans.service.metric;

import org.maxgamer.maxbans.MaxBansPlus;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author netherfoam
 */
public class BStatsMetricService implements MetricService {
  /**
   * bStats Link: https://bstats.org/plugin/bukkit/MaxBansPlus/829
   */
  public static final int MAXBANS_PLUS_PLUGIN_ID = 829;

  private Map<String, Integer> increments = new HashMap<>();

  @Inject
  public BStatsMetricService(MaxBansPlus plugin) {

  }

  protected Callable<Integer> getAndReset(String key) {
    return () -> {
      Integer v = increments.remove(key);
      if (v == null) return 0;

      return v;
    };
  }

  public int get(String key) {
    Integer v = increments.get(key);
    if (v == null) return 0;

    return v;
  }

  @Override
  public void increment(String chartId) {
    int value = get(chartId) + 1;
    increments.put(chartId, value);
  }
}
