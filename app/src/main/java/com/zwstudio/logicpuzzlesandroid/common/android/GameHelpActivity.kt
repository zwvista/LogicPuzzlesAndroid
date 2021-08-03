package com.zwstudio.logicpuzzlesandroid.common.android

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityGameHelpBinding

abstract class GameHelpActivity<G : Game<G, GM, GS>, GD : GameDocument<GM>, GM, GS : GameState<GM>> : AppCompatActivity() {
    abstract val doc: GD

    protected lateinit var binding: ActivityGameHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, doc.help)
        binding.lvHelp.adapter = adapter
        binding.tvGame.text = doc.gameTitle + " Help"
    }
}