package com.calebtrevino.tallystacker.models;

import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.crashlytics.android.Crashlytics;

import java.util.HashMap;

/**
 * @author Ritesh Shakya
 */

public class IntermediateResult {
    private HashMap<String, Integer> resultList = new HashMap<>();
    private GameStatus gameStatus = GameStatus.NEUTRAL;

    public void add(String teamAbbr, String teamScore) {
        try {
            resultList.put(teamAbbr, Integer.valueOf(teamScore));
        } catch (NumberFormatException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return resultList.isEmpty();
    }

    public int getTotal() {
        int totalScore = 0;
        for (Integer score : resultList.values()) {
            totalScore += score;
        }
        return totalScore;
    }

    public int getTeamScore(Team team) {
        if (resultList.containsKey(team.getAcronym()))
            return resultList.get(team.getAcronym());
        else return 0;
    }

    @Override
    public String toString() {
        return "IntermediateResult{" +
                "resultList=" + resultList +
                ", gameStatus=" + gameStatus +
                '}';
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}