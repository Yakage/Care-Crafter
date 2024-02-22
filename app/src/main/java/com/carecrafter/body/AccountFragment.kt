package com.carecrafter.body

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.carecrafter.databinding.BodyAccountBinding
import com.carecrafter.registration.SignIn
import com.carecrafter.sqlitedatabase.CareCrafterDatabaseHelper

class AccountFragment : Fragment() {
    private lateinit var binding: BodyAccountBinding
    private lateinit var dbHelper: CareCrafterDatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BodyAccountBinding.inflate(inflater, container, false)

        binding.layoutAccount.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToUpdateAccount())
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = CareCrafterDatabaseHelper(requireContext())
        // Assuming you have a button in your fragment layout to trigger the action
        binding.layoutLogOut.setOnClickListener {
            dbHelper.deleteAllUserData()
            showToast("User logged out successfully")
            val intent = Intent(activity, SignIn::class.java)
            startActivity(intent)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}