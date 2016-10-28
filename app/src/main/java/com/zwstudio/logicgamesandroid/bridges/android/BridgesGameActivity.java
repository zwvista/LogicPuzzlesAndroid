package com.zwstudio.logicgamesandroid.bridges.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesDocument;
import com.zwstudio.logicgamesandroid.bridges.data.BridgesMoveProgress;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGame;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameMove;
import com.zwstudio.logicgamesandroid.bridges.domain.BridgesGameState;
import com.zwstudio.logicgamesandroid.logicgames.android.GameActivity;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_bridges_game)
public class BridgesGameActivity extends GameActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState> {
    public BridgesDocument doc() {return app.bridgesDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new BridgesGame(layout, this);
        try {
            // restore game state
            for (BridgesMoveProgress rec : doc().moveProgress())
                game.switchBridges(new Position(rec.rowFrom, rec.colFrom), new Position(rec.rowTo, rec.colTo));
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }
}
