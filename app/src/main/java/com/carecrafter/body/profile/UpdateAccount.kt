package com.carecrafter.body.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.AccountUpdateAccountBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.User
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class UpdateAccount : Fragment() {
    private lateinit var binding: AccountUpdateAccountBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AccountUpdateAccountBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getUserInfo(it) }
        binding.backButton.setOnClickListener {
            findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
        }

        setupProfilePictureSpinner()

        binding.btUpdate.setOnClickListener {
            updateUserData(authToken.toString())
        }

        return binding.root
    }

    private fun setupProfilePictureSpinner() {
        val profilePictureSpinner = binding.profilePictureSpinner
        val imageList = listOf(
            R.drawable.avatarone,
            R.drawable.avatartwo,
            R.drawable.avatarthree,
            R.drawable.avatarfour,
            R.drawable.avatarfive,
            R.drawable.avatarsix,
            R.drawable.avatarseven,
            R.drawable.avataeight
        )
        val indicatorTextList = listOf(
            "Avatar 1",
            "Avatar 2",
            "Avatar 3",
            "Avatar 4",
            "Avatar 5",
            "Avatar 6",
            "Avatar 7",
            "Avatar 8"
        )
        val adapter = ProfilePictureAdapter(requireContext(), imageList, indicatorTextList)
        profilePictureSpinner.adapter = adapter

        profilePictureSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Change the profile picture based on the selected item
                binding.profilePictureAvatar.setImageResource(imageList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
    }


    private fun getUserInfo(authToken: String) {
        ApiClient.instance.getUser("Bearer $authToken").enqueue(object : Callback<User> {
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

    private fun updateInfo(userData: User) {
        if (userData != null) {
            binding.FullNameET.text = Editable.Factory.getInstance().newEditable(userData.name)
            binding.BirthdayET.text = Editable.Factory.getInstance().newEditable(userData.birthday)
            binding.GenderET.text = Editable.Factory.getInstance().newEditable(userData.gender)
            binding.HeightET.text = Editable.Factory.getInstance().newEditable(userData.height)
            binding.WeightET.text = Editable.Factory.getInstance().newEditable(userData.weight)
        }
    }

    private fun updateUserData(authToken: String) {
        val name = binding.FullNameET.text.toString().trim()
        val birthday = binding.BirthdayET.text.toString().trim()
        val height = binding.HeightET.text.toString().trim()
        val weight = binding.WeightET.text.toString().trim()
        val gender = binding.GenderET.text.toString().toLowerCase().trim()

        val updateUserDataJson =
            "{\"authToken\":\"$authToken\",\"name\":\"$name\",\"birthday\":\"$birthday\",\"gender\":\"$gender\",\"height\":\"$height\",\"weight\":\"$weight\"}"

        //validation
        if (name.isEmpty()) {
            binding.FullNameET.error = "Full Name required"
            binding.FullNameET.requestFocus()
        }

        if (birthday.toString().isEmpty()) {
            binding.BirthdayET.error = "Age required"
            binding.BirthdayET.requestFocus()
        }

        if (height.isEmpty()) {
            binding.HeightET.error = "Height required"
            binding.HeightET.requestFocus()
        }

        if (weight.isEmpty()) {
            binding.WeightET.error = "Weight required"
            binding.WeightET.requestFocus()
        }

        if (gender.isEmpty()) {
            binding.GenderET.error = "Gender required"
            binding.GenderET.requestFocus()
        }

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(updateUserDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.updateUser(
                "Bearer $authToken",
                name,
                birthday,
                gender,
                height,
                weight,
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_LONG).show()
                        findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
                    } else {
                        val errorMessage: String = try {
                            response.errorBody()?.string() ?: "Failed to get a valid response. Response code: ${response.code()}"
                        } catch (e: Exception) {
                            "Failed to get a valid response. Response code: ${response.code()}"
                        }
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("API_RESPONSE", errorMessage)
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private inner class ProfilePictureAdapter(context: Context, private val imageList: List<Int>, private val indicatorTextList: List<String>) : ArrayAdapter<Int>(context, 0, imageList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val imageView = ImageView(context)
            imageView.setImageResource(imageList[position])
            imageView.layoutParams = ViewGroup.LayoutParams(150, 150)
            return imageView
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_profile_picture_dropdown, parent, false)
            val imageView = view.findViewById<ImageView>(R.id.avatar)
            val textView = view.findViewById<TextView>(R.id.avatarTitle)
            imageView.setImageResource(imageList[position])
            textView.text = indicatorTextList[position]
            return view
        }
    }
}
