package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class BoxItUpMainActivity : GameMainActivity<BoxItUpGame?, BoxItUpDocument?, BoxItUpGameMove?, BoxItUpGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItUpDocument? = null
    override fun doc(): BoxItUpDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        BoxItUpOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BoxItUpGameActivity_.intent(this).start()
    }
}