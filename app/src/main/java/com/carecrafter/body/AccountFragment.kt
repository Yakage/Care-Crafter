package com.carecrafter.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.carecrafter.databinding.BodyAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: BodyAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = BodyAccountBinding.inflate(inflater, container, false)

        binding.layoutAccount.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToUpdateAccount())
        }
        return binding.root
    }

}