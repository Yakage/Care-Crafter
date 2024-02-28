package com.carecrafter.body

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carecrafter.databinding.BodyAccountBinding
import com.carecrafter.models.SharedPrefsViewModel
import com.carecrafter.models.User
import com.carecrafter.models.UserViewModel
import com.carecrafter.registration.SignIn
import com.carecrafter.retrofit_database.ApiClient
import com.carecrafter.sqlitedatabase.CareCrafterDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {
    private lateinit var binding: BodyAccountBinding
    private lateinit var viewModel: SharedPrefsViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BodyAccountBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedPrefsViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)

        val authToken = (requireActivity() as BodyActivity).getAuthToken()
        authToken?.let { getUserInfo(it) }

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToUpdateAccount())
        }
        binding.layoutLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            showToast("User logged out successfully")
            val intent = Intent(activity, SignIn::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getUserInfo(authToken: String) {
        ApiClient.instance.getUser("Bearer$authToken").enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        updateInfo(userData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateInfo(userData: User){
        if (userData != null) {
            binding.textViewName.text = userData.name
            binding.textViewEmail.text = userData.email
        }
    }
}