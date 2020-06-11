package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android.ParkLakesGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.android.ParkLakesOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.data.ParkLakesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain.ParkLakesGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class ParkLakesMainActivity : GameMainActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        ParkLakesOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        ParkLakesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ParkLakesOptionsActivity : GameOptionsActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class ParkLakesHelpActivity : GameHelpActivity<ParkLakesGame, ParkLakesDocument, ParkLakesGameMove, ParkLakesGameState>() {
    @Bean
    protected lateinit var document: ParkLakesDocument
    override fun doc() = document
}