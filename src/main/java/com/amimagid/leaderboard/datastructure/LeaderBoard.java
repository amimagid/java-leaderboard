package com.amimagid.leaderboard.datastructure;

import java.util.Set;

/**
 * Created by ami on 18/01/17.
 */
public interface LeaderBoard<ID, V> {

    void add(V v);
    V getById(ID id);
    Set<V> getRange(int from, int to);
    V remove(ID id);
    int size();
    Set<V> getTop(int numberOfResults);
}
