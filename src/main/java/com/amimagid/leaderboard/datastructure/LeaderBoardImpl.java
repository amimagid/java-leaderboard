package com.amimagid.leaderboard.datastructure;

import com.amimagid.leaderboard.entity.Rankable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ami on 18/01/17.
 *
 * Thread-safe leader board implementation.
 * <p>
 * Uses @{@link ConcurrentHashMap} for mapping the identifiers to the ranking objects,
 * and @{@link ConcurrentSkipListSet} with a comparator implementation for fast range operations
 * <ul>
 *   <li>ID is the object that identifies the ranking entity (e.g. userId for user ranking)</li>
 *   <li>T is the ranking object, e.g. score (number) for ranking users in a game..</li>
 *   <li>V is the @{@link Rankable} object itself.</li>
 * </ul>
 */
public class LeaderBoardImpl<ID, T extends Comparable, V extends Rankable<ID, T, V>> implements LeaderBoard<ID, V> {

    private volatile ConcurrentSkipListSet<V> set;
    private volatile ConcurrentHashMap<ID, V> concurrentHashMap;
    private volatile AtomicInteger size;

    public LeaderBoardImpl() {
        set = new ConcurrentSkipListSet();
        concurrentHashMap = new ConcurrentHashMap<ID, V>();
        size = new AtomicInteger(0);
    }

    /**
     * Add a @{@link Rankable} object to the leaderboard
     *
     * @param v the param you want to add. BEWARE that it should implement @{@link Comparable}
     */
    @Override
    public synchronized void add(V v) {
        ID id = v.getId();
        if (concurrentHashMap.get(id) == null) {
            size.incrementAndGet();
        }
        set.add(v);
        concurrentHashMap.put(id, v);

    }

    /**
     * Gets the @{@link Rankable} object
     *
     * @param id - the identifier of the rankable object
     */
    @Override
    public V getById(ID id) {
        return concurrentHashMap.get(id);
    }

    /**
     * Gets sorter range of @{@link Rankable} objects.
     *
     * This will create a new @{@link ConcurrentSkipListSet}, so no worries about messing
     * with the original Set :)
     */
    @Override
    public synchronized Set<V> getRange(int from, int to) {
        if (to < from ) throw new IndexOutOfBoundsException();
        final int size = size();
        if (size == 0) return null;
        if (from < 1) from = 1;
        if (from > size) from = size;
        if (to > size) to = size ;
        V elementFrom = getElementInPos(from - 1);
        V elementTo = getElementInPos(to - 1);
        return set.subSet(elementFrom, true, elementTo,true);

    }

    /**
     * Removes the @{@link Rankable} object
     *
     * @param id - the id of the @{@link Rankable} you want to remove
     * @return the removed object, null if there isn't one.
     */
    @Override
    public synchronized V remove(Object id) {
        V removed = concurrentHashMap.remove(id);
        if (removed != null) {
            set.remove(removed);
            size.decrementAndGet();
        }
        return removed;
    }

    /**
     * The size of the leaderboard.
     * Done for o(1) operation instead of  {@link ConcurrentSkipListSet#size()} or @{@link ConcurrentHashMap#size()}
     * that might reflect transient state.
     */
    @Override
    public int size() {
        return size.get();
    }

    /**
     * a simple proxy to the @{@link LeaderBoardImpl#getRange(int, int)} method for getting the top objects
     */
    @Override
    public Set<V> getTop(int numberOfResults) {
        return getRange(1, numberOfResults);
    }

    private V getElementInPos(int elementIndex) {
        if (elementIndex < 0) throw new IndexOutOfBoundsException();
        int counter = 0;
        for (V v : set) {
            if (counter == elementIndex) {
                return v;
            }
            counter++;
        }
        return null;
    }
}
