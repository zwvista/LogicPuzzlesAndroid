package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data.BoxItAroundDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class BoxItAroundGameActivity extends GameGameActivity<BoxItAroundGame, BoxItAroundDocument, BoxItAroundGameMove, BoxItAroundGameState> {
    public BoxItAroundDocument doc() {return app.boxitaroundDocument;}

    protected BoxItAroundGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new BoxItAroundGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new BoxItAroundGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                BoxItAroundGameMove move = doc().loadMove(rec);
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
}
