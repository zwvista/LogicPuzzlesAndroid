package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class AbcMainActivity : GameMainActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    @Bean
    protected lateinit var document: AbcDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        AbcOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        AbcGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class AbcOptionsActivity : GameOptionsActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    @Bean
    protected lateinit var document: AbcDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class AbcHelpActivity : GameHelpActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState>() {
    @Bean
    protected lateinit var document: AbcDocument
    override val doc get() = document
}
