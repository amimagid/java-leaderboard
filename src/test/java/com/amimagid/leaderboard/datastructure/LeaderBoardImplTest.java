package com.amimagid.leaderboard.datastructure;

import static org.junit.Assert.*;

import com.amimagid.leaderboard.entity.StringRank;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Created by ami on 22/01/17.
 */
public class LeaderBoardImplTest {
    private LeaderBoard<Integer, StringRank> leaderBoard;
    @Before
    public void init() throws Exception {
        leaderBoard = new LeaderBoardImpl<Integer, String, StringRank>();
        for (int i = 0; i < 10; i++) {
             leaderBoard.add(new StringRank(i, String.valueOf(i)));
        }
    }
    @Test
    public void simpleInsert() throws Exception {
        StringRank rank = new StringRank(11, "999");
        leaderBoard.add(rank);
        assertEquals(11, leaderBoard.size());
        assertNotNull(leaderBoard.getById(11));
        assertTrue(leaderBoard.getRange(11,1000).contains(rank));
    }
    @Test
    public void insertDuplicateScores() throws Exception {
        StringRank rank1 = new StringRank(11, "0");
        StringRank rank2 = new StringRank(12, "0");
        leaderBoard.add(rank1);
        leaderBoard.add(rank2);

        assertEquals(12, leaderBoard.size());
        Set<StringRank> range = leaderBoard.getRange(1, 3);
        for (StringRank rank : range) {
            assertTrue(rank.getRankParam().equals("0"));
        }
    }
    @Test
    public void insertDuplicateValues() throws Exception {
        StringRank rank1 = new StringRank(0, "190");
        StringRank rank2 = new StringRank(0, "778");
        leaderBoard.add(rank1);
        leaderBoard.add(rank2);

        assertEquals(10, leaderBoard.size());
        StringRank rank = leaderBoard.getById(0);
        assertEquals("778", rank.getRankParam());
    }

    @Test
    public void getById() throws Exception {
        assertEquals(10, leaderBoard.size());
        StringRank rank = leaderBoard.getById(7);
        assertEquals("7", rank.getRankParam());
    }
    @Test
    public void getByIdNoId() throws Exception {
        assertEquals(10, leaderBoard.size());
        StringRank rank = leaderBoard.getById(77);
        assertNull(rank);
    }

    @Test
    public void getRange() throws Exception {
        StringRank rank1 = new StringRank(10, "10");
        StringRank rank2 = new StringRank(11, "11");
        leaderBoard.add(rank1);
        leaderBoard.add(rank2);

        assertEquals(12, leaderBoard.size());
        Set<StringRank> range = leaderBoard.getRange(5, 12);//starting from 2 to 9
        int index = 2;
        for (StringRank rank : range) {
            assertTrue(rank.getRankParam().equals(String.valueOf(index)));
            index++;
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getRangeOutOfRange() throws Exception {
        assertEquals(10, leaderBoard.size());
        leaderBoard.getRange(6, 4);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void getRangeOutOfRangeOfZeroes() throws Exception {
        assertEquals(10, leaderBoard.size());
        leaderBoard.getRange(0, 0);
    }

    @Test
    public void getRangeAfterRemove() throws Exception {
        Set<StringRank> range = leaderBoard.getRange(1, 1);
        assertTrue(range.iterator().next().getRankParam().equals("0"));
        leaderBoard.remove(0);
        leaderBoard.remove(6);
        assertEquals(8, leaderBoard.size());
        range = leaderBoard.getRange(1, 1);
        assertTrue(range.iterator().next().getRankParam().equals("1"));
    }

    @Test
    public void remove() throws Exception {
        leaderBoard.remove(2);
        assertEquals(9, leaderBoard.size());

    }
    @Test
    public void removeDoesNotExist() throws Exception {
        leaderBoard.remove(22);
        assertEquals(10, leaderBoard.size());
    }
    @Test
    public void getTop() throws Exception {
        int numOfResults = 5;
        Set<StringRank> top = leaderBoard.getTop(numOfResults);
        assertEquals(5, top.size());
        int index = 0;
        for (StringRank stringRank : top) {
            assertEquals(String.valueOf(index), stringRank.getRankParam());
            index++;
        }
    }

}