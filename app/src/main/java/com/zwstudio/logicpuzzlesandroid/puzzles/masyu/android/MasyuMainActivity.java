package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class MasyuMainActivity extends GameMainActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState> {
    @Bean
    protected MasyuDocument document;
    public MasyuDocument doc() {return document;}

    @Click
    void btnOptions() {
        MasyuOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        MasyuGameActivity_.intent(this).start();
    }
}
