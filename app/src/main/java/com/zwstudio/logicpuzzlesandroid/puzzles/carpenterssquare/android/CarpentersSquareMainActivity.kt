package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.data.CarpentersSquareDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class CarpentersSquareMainActivity : GameMainActivity<CarpentersSquareGame?, CarpentersSquareDocument?, CarpentersSquareGameMove?, CarpentersSquareGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CarpentersSquareDocument? = null
    override fun doc(): CarpentersSquareDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        CarpentersSquareOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        CarpentersSquareGameActivity_.intent(this).start()
    }
}