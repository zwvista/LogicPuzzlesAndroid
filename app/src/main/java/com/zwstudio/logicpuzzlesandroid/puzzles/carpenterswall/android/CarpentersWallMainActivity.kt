package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.data.CarpentersWallDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class CarpentersWallMainActivity : GameMainActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    @Bean
    protected lateinit var document: CarpentersWallDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        CarpentersWallOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        CarpentersWallGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class CarpentersWallOptionsActivity : GameOptionsActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    @Bean
    protected lateinit var document: CarpentersWallDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class CarpentersWallHelpActivity : GameHelpActivity<CarpentersWallGame, CarpentersWallDocument, CarpentersWallGameMove, CarpentersWallGameState>() {
    @Bean
    protected lateinit var document: CarpentersWallDocument
    override val doc get() = document
}