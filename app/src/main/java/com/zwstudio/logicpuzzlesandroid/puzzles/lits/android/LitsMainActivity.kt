package com.zwstudio.logicpuzzlesandroid.puzzles.lits.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.data.LitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LitsMainActivity : GameMainActivity<LitsGame?, LitsDocument?, LitsGameMove?, LitsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LitsDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        LitsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        LitsGameActivity_.intent(this).start()
    }
}