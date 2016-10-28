package com.zwstudio.logicgamesandroid.lightup.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpDocument;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpMoveProgress;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGame;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGameMove;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpGameState;
import com.zwstudio.logicgamesandroid.lightup.domain.LightUpObject;
import com.zwstudio.logicgamesandroid.logicgames.android.GameActivity;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_lightup_game)
public class LightUpGameActivity extends GameActivity<LightUpGame, LightUpDocument, LightUpGameMove, LightUpGameState> {
    public LightUpDocument doc() {return app().lightUpDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new LightUpGame(layout, this);
        try {
            // restore game state
            for (LightUpMoveProgress rec : doc().moveProgress()) {
                LightUpGameMove move = new LightUpGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = LightUpObject.objTypeFromString(rec.objTypeAsString);
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
