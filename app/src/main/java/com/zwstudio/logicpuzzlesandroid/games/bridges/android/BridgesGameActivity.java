package com.zwstudio.logicpuzzlesandroid.games.bridges.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.games.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.games.bridges.data.BridgesMoveProgress;
import com.zwstudio.logicpuzzlesandroid.games.bridges.domain.BridgesGame;
import com.zwstudio.logicpuzzlesandroid.games.bridges.domain.BridgesGameMove;
import com.zwstudio.logicpuzzlesandroid.games.bridges.domain.BridgesGameState;
import com.zwstudio.logicpuzzlesandroid.common.android.GameActivity;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

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
