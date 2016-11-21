package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_hitori_game)
public class HitoriGameActivity extends GameActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    public HitoriDocument doc() {return app.hitoriDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new HitoriGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                HitoriGameMove move = doc().loadMove(rec);
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
