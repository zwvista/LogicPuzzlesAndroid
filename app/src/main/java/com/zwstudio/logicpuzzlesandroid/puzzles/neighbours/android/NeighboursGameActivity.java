package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_neighbours_game)
public class NeighboursGameActivity extends GameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState> {
    public NeighboursDocument doc() {return app.neighboursDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new NeighboursGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                NeighboursGameMove move = doc().loadMove(rec);
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
