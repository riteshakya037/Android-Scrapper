package com.calebtrevino.tallystacker.controllers.sources;

import android.util.Log;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnGameScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.InvalidScoreTypeException;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidResult;

/**
 * @author Ritesh Shakya
 */
public class CalculateResult {
    private static final String TAG = CalculateResult.class.getSimpleName();
    private Game game;
    private EspnGameScoreParser.IntermediateResult currentScore;

    public ResultOut calculateResult(Game game, EspnGameScoreParser.IntermediateResult currentScore) throws InvalidScoreTypeException {
        this.game = game;
        this.currentScore = currentScore;
        Log.i(TAG, "vigBid" + game.getVI_bid());
        Log.i(TAG, "currentScore" + currentScore);
        switch (game.getScoreType()) {
            case SPREAD:
                return calculateForSpread();
            case TOTAL:
                return calculateForTotal();
            case DEFAULT:
            default:
                throw new InvalidScoreTypeException(game.toJSON());
        }
    }

    private ResultOut calculateForTotal() {
        if (currentScore.isCompleted()) {
            switch (game.getVI_bid().getCondition()) {
                case OVER:
                    if (currentScore.getTotal() > game.getVI_bid().getBidAmount())
                        return new ResultOut(true, BidResult.POSITIVE);
                    else
                        return new ResultOut(true, BidResult.NEGATIVE);
                case UNDER:
                    if (currentScore.getTotal() < game.getVI_bid().getBidAmount())
                        return new ResultOut(true, BidResult.POSITIVE);
                    else
                        return new ResultOut(true, BidResult.NEGATIVE);
            }
        } else {
            switch (game.getVI_bid().getCondition()) {
                case OVER:
                    if (currentScore.getTotal() > game.getVI_bid().getBidAmount())
                        return new ResultOut(true, BidResult.POSITIVE);
                case UNDER:
                    if (currentScore.getTotal() > game.getVI_bid().getBidAmount())
                        return new ResultOut(true, BidResult.NEGATIVE);
                    break;
            }
        }
        return new ResultOut(false, BidResult.NEUTRAL);
    }

    private ResultOut calculateForSpread() {
        if (currentScore.isCompleted()) {
            switch (game.getVI_bid().getCondition()) {
                case SPREAD:
                    if (game.getVI_bid().getBidAmount() < 0.0) {
                        if (currentScore.getTeamScore(game.getSecondTeam()) - currentScore.getTeamScore(game.getFirstTeam()) > Math.abs(game.getVI_bid().getBidAmount())) {
                            return new ResultOut(true, BidResult.POSITIVE);
                        } else {
                            return new ResultOut(true, BidResult.NEGATIVE);
                        }
                    } else {
                        if (currentScore.getTeamScore(game.getFirstTeam()) - currentScore.getTeamScore(game.getSecondTeam()) > game.getVI_bid().getBidAmount()) {
                            return new ResultOut(true, BidResult.POSITIVE);
                        } else {
                            return new ResultOut(true, BidResult.NEGATIVE);
                        }
                    }
            }
        }
        return new ResultOut(false, BidResult.NEUTRAL);
    }


    public static void setResult(Game game, EspnGameScoreParser.IntermediateResult intermediateResult, ResultOut resultOut, boolean isComplete) {
        game.setFirstTeamScore(intermediateResult.getTeamScore(game.getFirstTeam()));
        game.setSecondTeamScore(intermediateResult.getTeamScore(game.getSecondTeam()));
        game.setBidResult(resultOut.getBidResult());
        game.setComplete(isComplete);
    }

    public class ResultOut {
        private boolean gameCompleted = false;
        private BidResult bidResult = BidResult.NEUTRAL;

        ResultOut(boolean gameCompleted, BidResult bidResult) {
            this.gameCompleted = gameCompleted;
            this.bidResult = bidResult;
        }

        public boolean isGameCompleted() {
            return gameCompleted;
        }

        BidResult getBidResult() {
            return bidResult;
        }

        @Override
        public String toString() {
            return "ResultOut{" +
                    "gameCompleted=" + gameCompleted +
                    ", bidResult=" + bidResult +
                    '}';
        }
    }
}
