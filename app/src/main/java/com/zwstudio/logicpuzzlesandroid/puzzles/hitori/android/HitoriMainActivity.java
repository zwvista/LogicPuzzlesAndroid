package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class HitoriMainActivity extends GameMainActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    @Bean
    protected HitoriDocument document;
    public HitoriDocument doc() {return document;}

    @Click
    void btnOptions() {
        HitoriOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        HitoriGameActivity_.intent(this).start();
    }
}
