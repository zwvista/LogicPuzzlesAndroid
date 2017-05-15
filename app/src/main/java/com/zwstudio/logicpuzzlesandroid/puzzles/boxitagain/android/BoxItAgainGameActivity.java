package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.data.BoxItAgainDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class BoxItAgainGameActivity extends GameGameActivity<BoxItAgainGame, BoxItAgainDocument, BoxItAgainGameMove, BoxItAgainGameState> {
    public BoxItAgainDocument doc() {return app.boxitagainDocument;}

    protected BoxItAgainGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new BoxItAgainGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new BoxItAgainGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                BoxItAgainGameMove move = doc().loadMove(rec);
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
        BoxItAgainHelpActivity_.intent(this).start();
    }
}
