package id.ac.polbeng.erikaardini.sqliteexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.ac.polbeng.erikaardini.sqliteexample.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var studentDBHelper: StudentDBHelper
    private var currentNim: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.etNama.isEnabled = false
        binding.etUmur.isEnabled = false
        binding.btnUpdate.isEnabled = false
        binding.btnHapus.isEnabled = false

        studentDBHelper = StudentDBHelper(this)

        binding.btnCari.setOnClickListener {
            val nim = binding.etNIM.text.toString().trim()
            if (nim.isEmpty()) {
                Toast.makeText(this, "Silahkan masukan nim terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val students = studentDBHelper.searchStudentByNIM(nim)
            if (students.isNotEmpty()) {
                currentNim = students[0].nim
                binding.etNIM.setText(students[0].nim)
                binding.etNama.setText(students[0].name)
                binding.etUmur.setText(students[0].age)
                setUpdateState(true)
                Toast.makeText(this, "Mahasiswa ditemukan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Mahasiswa tidak ditemukan!", Toast.LENGTH_SHORT).show()
                setUpdateState(false)
            }
        }

        binding.btnCari.setOnLongClickListener {
            binding.etNIM.isEnabled = true
            true
        }

        binding.btnUpdate.setOnClickListener {
            val nim = binding.etNIM.text.toString().trim()
            val name = binding.etNama.text.toString().trim()
            val age = binding.etUmur.text.toString().trim()
            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silahkan masukan data nim, nama, dan umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val newUpdateStudent = StudentModel(nim = nim, name = name, age = age)
                val updateCount = studentDBHelper.updateStudent(
                    newUpdateStudent,
                    oldNim = if (currentNim.isNotEmpty()) currentNim else nim
                )
                if (updateCount > 0) {
                    Toast.makeText(this, "Mahasiswa yang terupdate : $updateCount", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Tidak ada data mahasiswa yang diupdate silahkan coba lagi!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal mengupdate data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHapus.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("Konfirmasi Hapus Data")
                .setMessage("Apakah anda yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    val nim = if (currentNim.isNotEmpty()) currentNim else binding.etNIM.text.toString().trim()
                    val deleteCount = studentDBHelper.deleteStudent(nim)
                    if (deleteCount > 0) {
                        Toast.makeText(this, "Mahasiswa yang terhapus : $deleteCount", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Tidak ada data mahasiswa yang dihapus silahkan coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun setUpdateState(state: Boolean) {
        binding.etNIM.isEnabled = true
        binding.etNama.isEnabled = state
        binding.etUmur.isEnabled = state

        binding.btnUpdate.isEnabled = state
        binding.btnHapus.isEnabled = state
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
