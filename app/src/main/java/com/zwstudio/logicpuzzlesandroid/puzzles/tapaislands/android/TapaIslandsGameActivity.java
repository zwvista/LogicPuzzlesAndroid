package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.data.TapaIslandsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class TapaIslandsGameActivity extends GameGameActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState> {
    @Bean
    protected TapaIslandsDocument document;
    public TapaIslandsDocument doc() {return document;}

    protected TapaIslandsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new TapaIslandsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0)).layout;
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new TapaIslandsGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                TapaIslandsGameMove move = doc().loadMove(rec);
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
        TapaIslandsHelpActivity_.intent(this).start();
    }
}
