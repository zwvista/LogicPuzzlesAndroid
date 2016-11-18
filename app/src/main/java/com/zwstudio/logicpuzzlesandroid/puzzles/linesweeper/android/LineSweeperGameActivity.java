package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperGameState;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain.LineSweeperObjectOrientation;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_linesweeper_game)
public class LineSweeperGameActivity extends GameActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState> {
    public LineSweeperDocument doc() {return app.linesweeperDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new LineSweeperGame(layout, this);
        try {
            // restore game state
            for (LineSweeperMoveProgress rec : doc().moveProgress()) {
                LineSweeperGameMove move = new LineSweeperGameMove();
                move.p = new Position(rec.row, rec.col);
                move.objOrientation = LineSweeperObjectOrientation.values()[rec.objOrientation];
                move.obj = LineSweeperObject.values()[rec.obj];
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
