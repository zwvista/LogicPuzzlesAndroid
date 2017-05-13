package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class MiniLitsGameActivity extends GameActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState> {
    public MiniLitsDocument doc() {return app.minilitsDocument;}

    protected MiniLitsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new MiniLitsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new MiniLitsGame(layout, this, doc().isAllowedObjectsOnly());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                MiniLitsGameMove move = doc().loadMove(rec);
                game.setObject(move, doc().isAllowedObjectsOnly());
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
