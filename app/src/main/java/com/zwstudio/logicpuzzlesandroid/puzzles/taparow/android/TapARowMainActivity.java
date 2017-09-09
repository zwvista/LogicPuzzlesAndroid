package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data.TapARowDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TapARowMainActivity extends GameMainActivity<TapARowGame, TapARowDocument, TapARowGameMove, TapARowGameState> {
    @Bean
    protected TapARowDocument document;
    public TapARowDocument doc() {return document;}

    @Click
    void btnOptions() {
        TapARowOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TapARowGameActivity_.intent(this).start();
    }
}
