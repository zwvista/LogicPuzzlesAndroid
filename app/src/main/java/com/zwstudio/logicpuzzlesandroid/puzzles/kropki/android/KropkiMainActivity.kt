package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class KropkiMainActivity : GameMainActivity<KropkiGame?, KropkiDocument?, KropkiGameMove?, KropkiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KropkiDocument? = null
    override fun doc(): KropkiDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        KropkiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        KropkiGameActivity_.intent(this).start()
    }
}