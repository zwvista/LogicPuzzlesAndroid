package com.zwstudio.logicgamesandroid.games.clouds.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.games.clouds.data.CloudsDocument;
import com.zwstudio.logicgamesandroid.games.clouds.data.CloudsMoveProgress;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsGameState;
import com.zwstudio.logicgamesandroid.games.clouds.domain.CloudsObject;
import com.zwstudio.logicgamesandroid.common.android.GameActivity;
import com.zwstudio.logicgamesandroid.common.domain.Position;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_clouds_game)
public class CloudsGameActivity extends GameActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState> {
    public CloudsDocument doc() {return app.cloudsDocument;}

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);

        levelInitilizing = true;
        game = new CloudsGame(layout, this);
        try {
            // restore game state
            for (CloudsMoveProgress rec : doc().moveProgress()) {
                CloudsGameMove move = new CloudsGameMove();
                move.p = new Position(rec.row, rec.col);
                move.obj = CloudsObject.values()[rec.obj];
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
