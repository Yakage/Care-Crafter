package com.carecrafter.body

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.profile.UpdateAccountDirections
import com.carecrafter.databinding.BodyAccountBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.SharedPrefsViewModel
import com.carecrafter.models.User
import com.carecrafter.registration.SignIn
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

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
        binding.layoutAchievement.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAchievements())
        }
        binding.layoutLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
//            logoutUser(authToken.toString())
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
        ApiClient.instance.getUser("Bearer $authToken").enqueue(object : Callback<User>{
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
            when (userData.avatar) {
                1 -> binding.profilePicture.setImageResource(R.drawable.avatarone)
                2 -> binding.profilePicture.setImageResource(R.drawable.avatartwo)
                3 -> binding.profilePicture.setImageResource(R.drawable.avatarthree)
                4 -> binding.profilePicture.setImageResource(R.drawable.avatarfour)
                5 -> binding.profilePicture.setImageResource(R.drawable.avatarfive)
                6 -> binding.profilePicture.setImageResource(R.drawable.avatarsix)
                7 -> binding.profilePicture.setImageResource(R.drawable.avatarseven)
                8 -> binding.profilePicture.setImageResource(R.drawable.avataeight)
                else -> {
                    binding.profilePicture.setImageResource(R.drawable.boy_unpressed)
                }
            }
        }
    }

//    private fun logoutUser(authToken: String) {
//        val logoutUser =
//            "{\"authToken\":\"$authToken\"}"
//
//        //correct malformed data
//        try {
//            val reader = JsonReader(StringReader(logoutUser))
//            reader.isLenient = true
//            reader.beginObject()
//            reader.close()
//            ApiClient.instance.logoutUser(
//                "Bearer $authToken",
//            ).enqueue(object : Callback<DefaultResponse> {
//                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
//                }
//                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_LONG).show()
//                        findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
//                    } else {
//                        val errorMessage: String = try {
//                            response.errorBody()?.string() ?: "Failed to get a valid response. Response code: ${response.code()}"
//                        } catch (e: Exception) {
//                            "Failed to get a valid response. Response code: ${response.code()}"
//                        }
//                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
//                        Log.e("API_RESPONSE", errorMessage)
//                    }
//                }
//            })
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show()
//            e.printStackTrace()
//        }
//    }
}