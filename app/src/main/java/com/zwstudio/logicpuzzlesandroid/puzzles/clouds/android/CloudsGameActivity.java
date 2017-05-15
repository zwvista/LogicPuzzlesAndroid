package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity(R.layout.activity_game_game)
public class CloudsGameActivity extends GameGameActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState> {
    public CloudsDocument doc() {return app.cloudsDocument;}

    protected CloudsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new CloudsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(selectedLevelID);
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new CloudsGame(layout, this);
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                CloudsGameMove move = doc().loadMove(rec);
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

    @Click
    protected void btnHelp() {
        CloudsHelpActivity_.intent(this).start();
    }
}
