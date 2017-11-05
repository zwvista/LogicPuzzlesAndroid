package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data.TennerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class TennerGridGameActivity extends GameGameActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState> {
    @Bean
    protected TennerGridDocument document;
    public TennerGridDocument doc() {return document;}

    protected TennerGridGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new TennerGridGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new TennerGridGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                TennerGridGameMove move = doc().loadMove(rec);
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
        TennerGridHelpActivity_.intent(this).start();
    }
}
