package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class KropkiMainActivity : GameMainActivity<KropkiGame?, KropkiDocument?, KropkiGameMove?, KropkiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KropkiDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        KropkiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        KropkiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class KropkiOptionsActivity : GameOptionsActivity<KropkiGame?, KropkiDocument?, KropkiGameMove?, KropkiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KropkiDocument? = null
    override fun doc() = document!!
}

@EActivity(R.layout.activity_game_help)
class KropkiHelpActivity : GameHelpActivity<KropkiGame?, KropkiDocument?, KropkiGameMove?, KropkiGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: KropkiDocument? = null
    override fun doc() = document!!
}