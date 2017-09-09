package com.zwstudio.logicpuzzlesandroid.puzzles.pata.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.data.PataDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class PataOptionsActivity extends GameOptionsActivity<PataGame, PataDocument, PataGameMove, PataGameState> {
    @Bean
    protected PataDocument document;
    public PataDocument doc() {return document;}
}
