package com.zwstudio.logicpuzzlesandroid.games.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.games.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.games.hitori.data.HitoriMoveProgress;
import com.zwstudio.logicpuzzlesandroid.games.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.games.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.games.hitori.domain.HitoriGameState;
import com.zwstudio.logicpuzzlesandroid.games.hitori.domain.HitoriObject;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

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
            for (HitoriMoveProgress rec : doc().moveProgress()) {
                HitoriGameMove move = new HitoriGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = HitoriObject.values()[rec.obj];
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
