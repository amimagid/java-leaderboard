package com.amimagid.leaderboard.entity;

/**
 * Created by ami on 18/01/17.
 */
public class StringRank extends Rankable<Integer,String, StringRank> {
    private int userId ;
    private String myRank;

    public StringRank(int userId, String myRank) {
        this.userId = userId;
        this.myRank = myRank;
    }
    public Integer getId() {
        return userId;
    }
    public String getRankParam() {
        return myRank;
    }


    @Override
    public String toString() {
        return "StringRank{" +
                "userId=" + userId +
                ", myRank='" + myRank + '\'' +
                '}';
    }


}
