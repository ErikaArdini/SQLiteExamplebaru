package id.ac.polbeng.erikaardini.sqliteexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.ac.polbeng.erikaardini.sqliteexample.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        studentDBHelper = StudentDBHelper(this)

        binding.btnSimpan.setOnClickListener {
            val nim = binding.etNIM.text.toString().trim()
            val name = binding.etNama.text.toString().trim()
            val age = binding.etUmur.text.toString().trim()
            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silahkan masukan data nim, nama, dan umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val newStudent = StudentModel(nim = nim, name = name, age = age)
                val result = studentDBHelper.createStudent(newStudent)
                if (result > -1) {
                    Toast.makeText(this, "Sukses menambahkan data dengan primary key: $result", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menambahkan data dengan nim $nim (NIM mungkin sudah ada)", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal menambahkan data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }
}
