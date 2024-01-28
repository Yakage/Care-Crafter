package com.carecrafter.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.R
import com.carecrafter.databinding.BodyHomeBinding
import com.carecrafter.databinding.BodyProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: BodyProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = BodyProfileBinding.inflate(inflater, container, false)


        return binding.root
    }



}