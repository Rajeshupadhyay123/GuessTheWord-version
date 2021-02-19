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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    /**
     * for more about factory method:
     * https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
     *
     * The main reason of using the ViewModelFactory is for creating the parameterized
     * constructor for ViewModel
     *
     * Actually the ViewModelProvider.Factory is an interface than is used for create a
     * ViewModel object
     * When we didn't pass anything in the constructor of the viewModel then we needn't
     * use to call this method, it automatically call while the creation of ViewModel
     * But when we have to pass the data over the ViewModel Constructor then we have to
     * call this method memually and pass the parameter in the Factor constructor, it simply
     * return the object (reference) of ViewModel that we want and store that object as an ViewModel
     */
    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )
        //it is used for getting the data from the safe-Args
        val scoreArgs = ScoreFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = ScoreViewModelFactory(scoreArgs.score)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        //for init the layout variable
        binding.scoreViewModel = viewModel

        //Specify the fragment view as the lifecycle owner of the binding.
        //This is used so that the binding can observe the LiveData updates.
        binding.lifecycleOwner=viewLifecycleOwner


        //Navigate back to game when button is pressed
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { playAgain ->
            if (playAgain) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })
        return binding.root
    }
}
