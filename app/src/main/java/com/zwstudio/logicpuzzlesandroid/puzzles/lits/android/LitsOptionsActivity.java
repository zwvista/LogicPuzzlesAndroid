package com.zwstudio.logicpuzzlesandroid.puzzles.lits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.data.LitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class LitsOptionsActivity extends GameOptionsActivity<LitsGame, LitsDocument, LitsGameMove, LitsGameState> {
    @Bean
    protected LitsDocument document;
    public LitsDocument doc() {return document;}
}
