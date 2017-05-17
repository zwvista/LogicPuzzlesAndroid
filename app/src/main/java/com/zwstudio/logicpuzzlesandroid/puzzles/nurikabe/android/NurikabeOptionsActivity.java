package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class NurikabeOptionsActivity extends GameOptionsActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState> {
    public NurikabeDocument doc() {return app.nurikabeDocument;}
}
