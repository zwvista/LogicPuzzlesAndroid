package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LoopyMainActivity : GameMainActivity<LoopyGame?, LoopyDocument?, LoopyGameMove?, LoopyGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LoopyDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        LoopyOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        LoopyGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class LoopyOptionsActivity : GameOptionsActivity<LoopyGame?, LoopyDocument?, LoopyGameMove?, LoopyGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LoopyDocument? = null
    override fun doc() = document!!

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class LoopyHelpActivity : GameHelpActivity<LoopyGame?, LoopyDocument?, LoopyGameMove?, LoopyGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LoopyDocument? = null
    override fun doc() = document!!
}