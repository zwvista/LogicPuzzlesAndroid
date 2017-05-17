package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class LightenUpOptionsActivity extends GameOptionsActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState> {
    @Bean
    protected LightenUpDocument document;
    public LightenUpDocument doc() {return document;}
}
