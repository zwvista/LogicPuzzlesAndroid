package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcObject;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_abc_game)
public class AbcGameActivity extends GameActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState> {
    public AbcDocument doc() {return app.abcDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new AbcGame(layout, this);
        try {
            // restore game state
            for (AbcMoveProgress rec : doc().moveProgress()) {
                AbcGameMove move = new AbcGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = AbcObject.values()[rec.obj];
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
