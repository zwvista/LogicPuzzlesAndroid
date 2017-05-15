package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class SkyscrapersGameActivity extends GameGameActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState> {
    public SkyscrapersDocument doc() {return app.skyscrapersDocument;}

    protected SkyscrapersGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new SkyscrapersGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new SkyscrapersGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                SkyscrapersGameMove move = doc().loadMove(rec);
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
        SkyscrapersHelpActivity_.intent(this).start();
    }
}
