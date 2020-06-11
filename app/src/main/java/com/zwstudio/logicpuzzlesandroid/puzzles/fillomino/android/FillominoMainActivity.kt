package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.data.FillominoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain.FillominoGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FillominoMainActivity : GameMainActivity<FillominoGame?, FillominoDocument?, FillominoGameMove?, FillominoGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FillominoDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        FillominoOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FillominoGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class FillominoOptionsActivity : GameOptionsActivity<FillominoGame?, FillominoDocument?, FillominoGameMove?, FillominoGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FillominoDocument? = null
    override fun doc() = document!!
}

@EActivity(R.layout.activity_game_help)
class FillominoHelpActivity : GameHelpActivity<FillominoGame?, FillominoDocument?, FillominoGameMove?, FillominoGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FillominoDocument? = null
    override fun doc() = document!!
}