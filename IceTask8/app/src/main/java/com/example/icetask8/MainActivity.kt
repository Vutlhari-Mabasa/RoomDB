package com.example.icetask8

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icetask8.data.User
import com.example.icetask8.data.UserAdapter
import com.example.icetask8.data.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private var selectedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind UI
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = UserAdapter { user ->
            // Populate selected user info
            selectedUser = user
            etName.setText(user.name)
            etAge.setText(user.age.toString())
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ViewModel
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.allUsers.observe(this) { users ->
            adapter.submitList(users)
        }

        // Add
        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toIntOrNull()
            if (name.isNotBlank() && age != null) {
                userViewModel.insert(User(0, name, age))
                clearFields()
            }
        }

        // Update
        btnUpdate.setOnClickListener {
            val user = selectedUser ?: return@setOnClickListener
            val name = etName.text.toString()
            val age = etAge.text.toString().toIntOrNull()
            if (name.isNotBlank() && age != null) {
                userViewModel.update(User(user.id, name, age))
                clearFields()
            }
        }

        // Delete
        btnDelete.setOnClickListener {
            selectedUser?.let {
                userViewModel.delete(it)
                clearFields()
            }
        }
    }

    private fun clearFields() {
        etName.text.clear()
        etAge.text.clear()
        selectedUser = null
    }
}
