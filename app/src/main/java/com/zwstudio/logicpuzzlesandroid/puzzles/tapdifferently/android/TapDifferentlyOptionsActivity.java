package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data.TapDifferentlyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TapDifferentlyOptionsActivity extends GameOptionsActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState> {
    @Bean
    protected TapDifferentlyDocument document;
    public TapDifferentlyDocument doc() {return document;}
}
