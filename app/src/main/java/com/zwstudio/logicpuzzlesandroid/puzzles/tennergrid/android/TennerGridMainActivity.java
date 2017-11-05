package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data.TennerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TennerGridMainActivity extends GameMainActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState> {
    @Bean
    protected TennerGridDocument document;
    public TennerGridDocument doc() {return document;}

    @Click
    void btnOptions() {
        TennerGridOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TennerGridGameActivity_.intent(this).start();
    }
}
