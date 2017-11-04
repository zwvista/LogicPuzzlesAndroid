package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.data.RobotFencesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class RobotFencesMainActivity extends GameMainActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState> {
    @Bean
    protected RobotFencesDocument document;
    public RobotFencesDocument doc() {return document;}

    @Click
    void btnOptions() {
        RobotFencesOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        RobotFencesGameActivity_.intent(this).start();
    }
}
