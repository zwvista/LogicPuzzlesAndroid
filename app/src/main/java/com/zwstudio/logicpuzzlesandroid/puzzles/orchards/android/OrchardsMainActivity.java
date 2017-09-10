package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data.OrchardsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class OrchardsMainActivity extends GameMainActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState> {
    @Bean
    protected OrchardsDocument document;
    public OrchardsDocument doc() {return document;}

    @Click
    void btnOptions() {
        OrchardsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        OrchardsGameActivity_.intent(this).start();
    }
}
