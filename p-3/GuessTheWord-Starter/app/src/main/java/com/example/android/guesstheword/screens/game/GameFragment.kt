/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {


    private lateinit var binding: GameFragmentBinding


    private lateinit var viewModel: GameViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel=viewModel

        //it is for going to the score screen after all the word has been finished and word list is empty
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinish ->
            if (hasFinish)
                gameFinished()
        })

        /**
         * why we uses the viewLifeCycleOwer : https://medium.com/@cs.ibrahimyilmaz/viewlifecycleowner-vs-this-a8259800367b
         *
         * Observer is an interface which call when the LiveData changes
         * here we are using the lambada expression which execute anonymously
         *
         * --it give the data that are being observed and store that data into the provided variable 'newScore'
         * and we can use this data in our code.
         *
         * In place of lambda expression we can create an another method explectly like workdDone(it) and
         * pass an argument 'it' which refer as an value of the data that are being observed and can define this
         * method in anywhere else and perform operation
         */

        /**
         * Here when we call the GameViewModel and pass the score as an constructor
         * then the score field get updated from default value and on that while
         * the observe() method call and the Observer interface get executed.
         * after the execution of Observer interface the 'newScore' got the value of
         * score that are being observed and that (newScore=score) and then set to the
         * scoreText
         */
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })

        return binding.root

    }




    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value ?: 0
        NavHostFragment.findNavController(this).navigate(action)

        viewModel.onGameFinishComplete()
    }
}