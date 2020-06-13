package com.zwstudio.logicpuzzlesandroid.puzzles.kropki

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class KropkiMainActivity : GameMainActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    @Bean
    protected lateinit var document: KropkiDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        KropkiOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        KropkiGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class KropkiOptionsActivity : GameOptionsActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    @Bean
    protected lateinit var document: KropkiDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class KropkiHelpActivity : GameHelpActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState>() {
    @Bean
    protected lateinit var document: KropkiDocument
    override val doc get() = document
}