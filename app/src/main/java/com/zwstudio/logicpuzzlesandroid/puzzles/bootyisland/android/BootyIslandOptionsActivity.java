package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data.BootyIslandDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class BootyIslandOptionsActivity extends GameOptionsActivity<BootyIslandGame, BootyIslandDocument, BootyIslandGameMove, BootyIslandGameState> {
    @Bean
    protected BootyIslandDocument document;
    public BootyIslandDocument doc() {return document;}
}
