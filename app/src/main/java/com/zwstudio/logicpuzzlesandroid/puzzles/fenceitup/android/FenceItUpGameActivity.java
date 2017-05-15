package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class FenceItUpGameActivity extends GameGameActivity<FenceItUpGame, FenceItUpDocument, FenceItUpGameMove, FenceItUpGameState> {
    public FenceItUpDocument doc() {return app.fenceitupDocument;}

    protected FenceItUpGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new FenceItUpGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new FenceItUpGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                FenceItUpGameMove move = doc().loadMove(rec);
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Click
    protected void btnHelp() {
        FenceItUpHelpActivity_.intent(this).start();
    }
}
