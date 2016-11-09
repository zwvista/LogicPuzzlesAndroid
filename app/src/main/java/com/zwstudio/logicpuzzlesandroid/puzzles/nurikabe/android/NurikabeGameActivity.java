package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeObject;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_nurikabe_game)
public class NurikabeGameActivity extends GameActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState> {
    public NurikabeDocument doc() {return app.nurikabeDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new NurikabeGame(layout, this);
        try {
            // restore game state
            for (NurikabeMoveProgress rec : doc().moveProgress()) {
                NurikabeGameMove move = new NurikabeGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = NurikabeObject.objTypeFromString(rec.objTypeAsString);
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
