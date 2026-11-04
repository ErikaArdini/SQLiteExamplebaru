package id.ac.polbeng.erikaardini.sqliteexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import id.ac.polbeng.erikaardini.sqliteexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentDBHelper = StudentDBHelper(this)

        binding.btnCari.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            if (nama.isEmpty()) {
                Toast.makeText(this, "Silahkan masukan nama terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val students = studentDBHelper.searchStudentByName(nama)
            binding.llData.removeAllViews()
            students.forEach {
                val tvUser = TextView(this)
                tvUser.textSize = 20F
                tvUser.text = "${it.nim} - ${it.name}(${it.age} Tahun)"
                binding.llData.addView(tvUser)
            }
            binding.tvHasilPencarian.text = "Ditemukan ${students.size} mahasiswa"
        }

        binding.btnTambah.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateActivity::class.java))
        }

        binding.btnUpdate.setOnClickListener {
            startActivity(Intent(this@MainActivity, UpdateActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            loadAllData()
        }
    }

    private fun loadAllData() {
        val students = studentDBHelper.readStudents()
        binding.llData.removeAllViews()
        students.forEach {
            val tvUser = TextView(this)
            tvUser.textSize = 20F
            tvUser.text = "${it.nim} - ${it.name}(${it.age} Tahun)"
            binding.llData.addView(tvUser)
        }
        binding.tvHasilPencarian.text = "Total ${students.size} mahasiswa"
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onResume() {
        loadAllData()
        super.onResume()
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }
}
