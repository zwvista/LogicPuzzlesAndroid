package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MagnetsOptionsActivity extends GameOptionsActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState> {
    @Bean
    protected MagnetsDocument document;
    public MagnetsDocument doc() {return document;}
}
