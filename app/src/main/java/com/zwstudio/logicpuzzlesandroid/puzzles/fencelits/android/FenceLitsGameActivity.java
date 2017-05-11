package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data.FenceLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameState;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_fencelits_game)
public class FenceLitsGameActivity extends GameActivity<FenceLitsGame, FenceLitsDocument, FenceLitsGameMove, FenceLitsGameState> {
    public FenceLitsDocument doc() {return app.fencelitsDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new FenceLitsGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                FenceLitsGameMove move = doc().loadMove(rec);
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
