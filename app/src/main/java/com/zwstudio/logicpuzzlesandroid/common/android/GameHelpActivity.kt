package com.zwstudio.logicpuzzlesandroid.common.android

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity
abstract class GameHelpActivity<G : Game<G, GM, GS>, GD : GameDocument<G, GM>, GM, GS : GameState> : BaseActivity() {
    abstract val doc: GD

    @ViewById
    protected lateinit var tvGame: TextView

    @ViewById
    protected lateinit var lvHelp: ListView

    @AfterViews
    protected fun init() {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, doc.help)
        lvHelp.adapter = adapter
        tvGame.text = doc.gameTitle + " Help"
    }
}