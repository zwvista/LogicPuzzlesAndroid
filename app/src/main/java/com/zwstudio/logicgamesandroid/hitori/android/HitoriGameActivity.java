package com.zwstudio.logicgamesandroid.hitori.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.hitori.data.HitoriDocument;
import com.zwstudio.logicgamesandroid.hitori.data.HitoriMoveProgress;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGame;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGameMove;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriGameState;
import com.zwstudio.logicgamesandroid.hitori.domain.HitoriObject;
import com.zwstudio.logicgamesandroid.common.android.GameActivity;
import com.zwstudio.logicgamesandroid.common.domain.Position;

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
