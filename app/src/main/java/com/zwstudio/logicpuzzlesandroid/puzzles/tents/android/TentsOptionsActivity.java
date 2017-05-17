package com.zwstudio.logicpuzzlesandroid.puzzles.tents.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain.TentsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TentsOptionsActivity extends GameOptionsActivity<TentsGame, TentsDocument, TentsGameMove, TentsGameState> {
    @Bean
    protected TentsDocument document;
    public TentsDocument doc() {return document;}
}
