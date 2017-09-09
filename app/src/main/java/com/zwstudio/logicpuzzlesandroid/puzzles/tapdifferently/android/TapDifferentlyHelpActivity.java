package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data.TapDifferentlyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class TapDifferentlyHelpActivity extends GameHelpActivity<TapDifferentlyGame, TapDifferentlyDocument, TapDifferentlyGameMove, TapDifferentlyGameState> {
    @Bean
    protected TapDifferentlyDocument document;
    public TapDifferentlyDocument doc() {return document;}
}
