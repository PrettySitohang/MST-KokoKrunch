package com.example.mst_kokokrunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mst_kokokrunch.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Daftar pilihan drawable Avatar yang tersedia
    private val avatarList = arrayOf(
        R.drawable.logobpn, // Default
        android.R.drawable.ic_menu_camera,
        android.R.drawable.ic_menu_gallery,
        android.R.drawable.ic_menu_compass
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load & tampilkan data profil saat fragment dibuka
        loadProfileData()

        // 1. Event Klik Tombol Edit Profil
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        // 2. Event Klik Tombol Logout
        binding.btnLogOut.setOnClickListener {
            performLogout()
        }
    }

    private fun loadProfileData() {
        val sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val sessionPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)

        // Ambil Nama & Email dari SharedPreferences
        val savedName = sharedPref.getString("user_name", "Pretty Sitohang")
        val savedEmail = sessionPref.getString("logged_in_user", sharedPref.getString("email", "pretty_sthg@gmail.com"))
        val savedAvatarIndex = sharedPref.getInt("avatar_index", 0)

        // Tampilkan ke UI
        binding.tvDevName.text = savedName
        binding.tvEmail.text = savedEmail

        // Set Gambar Avatar
        if (savedAvatarIndex in avatarList.indices) {
            binding.imgProfile.setImageResource(avatarList[savedAvatarIndex])
        }
    }

    private fun showEditProfileDialog() {
        val context = requireContext()
        val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val currentName = binding.tvDevName.text.toString()
        val greenColor = android.graphics.Color.parseColor("#E2D62F")
        val darkTextColor = android.graphics.Color.parseColor("#2D3142")

        // Buat View Input Nama secara dinamis
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 30, 60, 10)
        }

        val etInputName = EditText(context).apply {
            hint = "Masukkan Nama Baru"
            setText(currentName)
            setTextColor(darkTextColor)
            setHintTextColor(android.graphics.Color.GRAY)
            backgroundTintList = android.content.res.ColorStateList.valueOf(greenColor)
        }
        container.addView(etInputName)

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Edit Profil")
            .setView(container)
            .setNeutralButton("Ganti Avatar") { _, _ ->
                showAvatarPickerDialog(etInputName.text.toString().trim())
            }
            .setPositiveButton("Simpan") { dialogInterface, _ ->
                val newName = etInputName.text.toString().trim()
                if (newName.isNotEmpty()) {
                    sharedPref.edit().putString("user_name", newName).apply()
                    binding.tvDevName.text = newName
                    Toast.makeText(context, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(greenColor)
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(greenColor)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(android.graphics.Color.GRAY)
        }

        dialog.show()
    }

    private fun showAvatarPickerDialog(updatedName: String) {
        val context = requireContext()
        val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val avatarNames = arrayOf("Default (Logo BPN)", "Kamera", "Galeri", "Kompas")

        MaterialAlertDialogBuilder(context)
            .setTitle("Pilih Foto Avatar")
            .setItems(avatarNames) { _, which ->
                sharedPref.edit().apply {
                    putInt("avatar_index", which)
                    if (updatedName.isNotEmpty()) putString("user_name", updatedName)
                    apply()
                }

                binding.imgProfile.setImageResource(avatarList[which])
                if (updatedName.isNotEmpty()) binding.tvDevName.text = updatedName

                Toast.makeText(context, "Avatar berhasil diubah!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun performLogout() {
        val sessionPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sessionPref.edit().clear().apply()

        val userDataPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        userDataPref.edit().clear().apply()

        Toast.makeText(requireContext(), "Berhasil Logout!", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}