package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data.TatamiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TatamiMainActivity extends GameMainActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState> {
    @Bean
    protected TatamiDocument document;
    public TatamiDocument doc() {return document;}

    @Click
    void btnOptions() {
        TatamiOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TatamiGameActivity_.intent(this).start();
    }
}
