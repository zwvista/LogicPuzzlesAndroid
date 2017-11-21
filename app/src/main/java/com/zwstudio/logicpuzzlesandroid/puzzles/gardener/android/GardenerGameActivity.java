package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data.GardenerDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class GardenerGameActivity extends GameGameActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState> {
    @Bean
    protected GardenerDocument document;
    public GardenerDocument doc() {return document;}

    protected GardenerGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new GardenerGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new GardenerGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                GardenerGameMove move = doc().loadMove(rec);
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
        GardenerHelpActivity_.intent(this).start();
    }
}
