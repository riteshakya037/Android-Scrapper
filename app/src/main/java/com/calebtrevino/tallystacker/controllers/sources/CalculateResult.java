package com.calebtrevino.tallystacker.controllers.sources;

import android.util.Log;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.InvalidScoreTypeException;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.crashlytics.android.Crashlytics;

/**
 * @author Ritesh Shakya
 */
public class CalculateResult {
    private static final String TAG = CalculateResult.class.getSimpleName();
    private Game game;
    private IntermediateResult currentScore;

    public static void setResult(Game game, IntermediateResult intermediateResult, ResultOut resultOut, GameStatus gameStatus) {
        game.setFirstTeamScore(intermediateResult.getTeamScore(game.getFirstTeam()));
        game.setSecondTeamScore(intermediateResult.getTeamScore(game.getSecondTeam()));
        game.setBidResult(resultOut.getBidResult());
        if (game.getFirstTeamScore() == game.getSecondTeamScore() && game.getGameStatus() == GameStatus.COMPLETE) {
            game.setBidResult(BidResult.DRAW);
        }
        game.setGameStatus(gameStatus);
    }

    public ResultOut calculateResult(Game game, IntermediateResult currentScore) throws InvalidScoreTypeException {
        this.game = game;
        this.currentScore = currentScore;
        Log.i(TAG, "vigBid" + game.getVIBid());
        Log.i(TAG, "currentScore" + currentScore);
        switch (game.getScoreType()) {
            case SPREAD:
                return calculateForSpread();
            case TOTAL:
                return calculateForTotal();
            case DEFAULT:
            default:
                Crashlytics.logException(new InvalidScoreTypeException(game.toJSON()));
                throw new InvalidScoreTypeException(game.toJSON());
        }
    }

    private ResultOut calculateForTotal() {
        if (currentScore.getGameStatus() == GameStatus.COMPLETE) {
            switch (game.getVIBid().getCondition()) {
                case OVER:
                    if (currentScore.getTotal() > game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.POSITIVE);
                    else if (currentScore.getTotal() == game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.DRAW);
                    else
                        return new ResultOut(currentScore.getGameStatus(), BidResult.NEGATIVE);
                case UNDER:
                    if (currentScore.getTotal() < game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.POSITIVE);
                    else if (currentScore.getTotal() == game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.DRAW);
                    else
                        return new ResultOut(currentScore.getGameStatus(), BidResult.NEGATIVE);
                default:
                    break;
            }
        } else {
            switch (game.getVIBid().getCondition()) {
                case OVER:
                    if (currentScore.getTotal() > game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.POSITIVE);
                case UNDER:
                    if (currentScore.getTotal() > game.getVIBid().getBidAmount())
                        return new ResultOut(currentScore.getGameStatus(), BidResult.NEGATIVE);
                default:
                    break;
            }
        }
        return new ResultOut(currentScore.getGameStatus(), BidResult.NEUTRAL);
    }

    private ResultOut calculateForSpread() {
        if (currentScore.getGameStatus() == GameStatus.COMPLETE) {
            switch (game.getVIBid().getCondition()) {
                case SPREAD:
                    if (game.getVIBid().getBidAmount() < 0.0) {
                        if (currentScore.getTeamScore(game.getSecondTeam()) - currentScore.getTeamScore(game.getFirstTeam()) > Math.abs(game.getVIBid().getBidAmount())) {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.POSITIVE);
                        } else if (currentScore.getTeamScore(game.getSecondTeam()) - currentScore.getTeamScore(game.getFirstTeam()) == Math.abs(game.getVIBid().getBidAmount())) {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.DRAW);
                        } else {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.NEGATIVE);
                        }
                    } else {
                        if (currentScore.getTeamScore(game.getFirstTeam()) - currentScore.getTeamScore(game.getSecondTeam()) > game.getVIBid().getBidAmount()) {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.POSITIVE);
                        } else if (currentScore.getTeamScore(game.getFirstTeam()) - currentScore.getTeamScore(game.getSecondTeam()) == game.getVIBid().getBidAmount()) {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.DRAW);
                        } else {
                            return new ResultOut(currentScore.getGameStatus(), BidResult.NEGATIVE);
                        }
                    }
                default:
                    break;
            }
        }
        return new ResultOut(currentScore.getGameStatus(), BidResult.NEUTRAL);
    }

    public class ResultOut {
        private GameStatus gameStatus = GameStatus.NEUTRAL;
        private BidResult bidResult = BidResult.NEUTRAL;

        ResultOut(GameStatus gameStatus, BidResult bidResult) {
            this.gameStatus = gameStatus;
            this.bidResult = bidResult;
        }

        public GameStatus getGameStatus() {
            return gameStatus;
        }

        BidResult getBidResult() {
            return bidResult;
        }

        @Override
        public String toString() {
            return "ResultOut{" +
                    "gameStatus=" + gameStatus +
                    ", bidResult=" + bidResult +
                    '}';
        }
    }
}
