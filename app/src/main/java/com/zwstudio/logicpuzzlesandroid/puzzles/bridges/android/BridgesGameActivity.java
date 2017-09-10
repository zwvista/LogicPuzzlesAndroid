package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class BridgesGameActivity extends GameGameActivity<BridgesGame, BridgesDocument, BridgesGameMove, BridgesGameState> {
    @Bean
    protected BridgesDocument document;
    public BridgesDocument doc() {return document;}

    protected BridgesGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new BridgesGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0)).layout;
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new BridgesGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                BridgesGameMove move = doc().loadMove(rec);
                game.switchBridges(move);
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
        BridgesHelpActivity_.intent(this).start();
    }
}
