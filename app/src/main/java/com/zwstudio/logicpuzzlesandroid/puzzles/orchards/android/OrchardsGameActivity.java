package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data.OrchardsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class OrchardsGameActivity extends GameGameActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState> {
    @Bean
    protected OrchardsDocument document;
    public OrchardsDocument doc() {return document;}

    protected OrchardsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new OrchardsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new OrchardsGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                OrchardsGameMove move = doc().loadMove(rec);
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
        OrchardsHelpActivity_.intent(this).start();
    }
}
