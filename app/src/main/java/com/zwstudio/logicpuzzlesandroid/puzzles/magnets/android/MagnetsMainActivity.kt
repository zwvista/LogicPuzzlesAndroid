package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class MagnetsMainActivity : GameMainActivity<MagnetsGame?, MagnetsDocument?, MagnetsGameMove?, MagnetsGameState?>() {
    @JvmField
    @Bean
    protected var document: MagnetsDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        MagnetsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        MagnetsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
open class MagnetsOptionsActivity : GameOptionsActivity<MagnetsGame?, MagnetsDocument?, MagnetsGameMove?, MagnetsGameState?>() {
    @JvmField
    @Bean
    protected var document: MagnetsDocument? = null
    override fun doc() = document!!
}

@EActivity(R.layout.activity_game_help)
open class MagnetsHelpActivity : GameHelpActivity<MagnetsGame?, MagnetsDocument?, MagnetsGameMove?, MagnetsGameState?>() {
    @JvmField
    @Bean
    protected var document: MagnetsDocument? = null
    override fun doc() = document!!
}