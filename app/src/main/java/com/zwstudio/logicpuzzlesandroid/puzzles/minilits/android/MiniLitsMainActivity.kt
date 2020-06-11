package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android.MiniLitsGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android.MiniLitsOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MiniLitsMainActivity : GameMainActivity<MiniLitsGame?, MiniLitsDocument?, MiniLitsGameMove?, MiniLitsGameState?>() {
    @Bean
    protected var document: MiniLitsDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        MiniLitsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MiniLitsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MiniLitsOptionsActivity : GameOptionsActivity<MiniLitsGame?, MiniLitsDocument?, MiniLitsGameMove?, MiniLitsGameState?>() {
    @Bean
    protected var document: MiniLitsDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class MiniLitsHelpActivity : GameHelpActivity<MiniLitsGame?, MiniLitsDocument?, MiniLitsGameMove?, MiniLitsGameState?>() {
    @Bean
    protected var document: MiniLitsDocument? = null
    override fun doc() = document
}