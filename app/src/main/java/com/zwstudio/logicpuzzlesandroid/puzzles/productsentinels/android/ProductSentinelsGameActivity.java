package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.data.ProductSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain.ProductSentinelsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class ProductSentinelsGameActivity extends GameGameActivity<ProductSentinelsGame, ProductSentinelsDocument, ProductSentinelsGameMove, ProductSentinelsGameState> {
    @Bean
    protected ProductSentinelsDocument document;
    public ProductSentinelsDocument doc() {return document;}

    protected ProductSentinelsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new ProductSentinelsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o._1().equals(selectedLevelID)).orSome(0))._2();
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new ProductSentinelsGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                ProductSentinelsGameMove move = doc().loadMove(rec);
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
        ProductSentinelsHelpActivity_.intent(this).start();
    }
}
