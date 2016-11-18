package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpObject;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_lightenup_game)
public class LightenUpGameActivity extends GameActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState> {
    public LightenUpDocument doc() {return app.lightenupDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new LightenUpGame(layout, this);
        try {
            // restore game state
            for (LightenUpMoveProgress rec : doc().moveProgress()) {
                LightenUpGameMove move = new LightenUpGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = LightenUpObject.objTypeFromString(rec.objTypeAsString);
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
