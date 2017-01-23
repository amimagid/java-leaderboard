package com.amimagid.leaderboard.entity;

/**
 * Created by ami on 18/01/17.
 * <p>
 * This represents a comparable and rankable item.
 * the @{@link Rankable#compareTo(Rankable)} method has a fallback mechanism to a lexicographical
 * sorting in case the rankable values are the same.
 */
public abstract class Rankable<ID, T extends Comparable, V extends Rankable> implements Comparable<V>{
    public abstract ID getId();
    public abstract T getRankParam();

     public int compareTo(V o) {
        int compared = this.getRankParam().compareTo(o.getRankParam());
        if (compared == 0) return this.getId().toString().compareTo(o.getId().toString());
        return compared;
    }
}
