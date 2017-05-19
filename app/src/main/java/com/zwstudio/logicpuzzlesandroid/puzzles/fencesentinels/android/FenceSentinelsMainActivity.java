package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data.FenceSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class FenceSentinelsMainActivity extends GameMainActivity<FenceSentinelsGame, FenceSentinelsDocument, FenceSentinelsGameMove, FenceSentinelsGameState> {
    @Bean
    protected FenceSentinelsDocument document;
    public FenceSentinelsDocument doc() {return document;}

    @Click
    void btnOptions() {
        FenceSentinelsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        FenceSentinelsGameActivity_.intent(this).start();
    }
}
