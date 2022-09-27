package com.example.projectwestay

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectwestay.databinding.ActivityFormPemesananBinding
import com.example.projectwestay.response.HomestayResponse
import java.text.SimpleDateFormat
import java.util.*

class FormPemesananActivity : AppCompatActivity() {
    private lateinit var data: HomestayResponse
    private lateinit var  binding: ActivityFormPemesananBinding

    private var _pickup: Calendar? = null

    private var _return: Calendar? = null

    private  var tanggalCheckin: Calendar? = null
    private  var jamCheckin: Calendar? = null
    private  var tanggalCheckout: Calendar? = null
    private  var jamCheckout: Calendar? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
    private val timeFormat = SimpleDateFormat("hh:mm", Locale.CANADA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPemesananBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        data = intent.getParcelableExtra<HomestayResponse>("data") as HomestayResponse

        initializeView()
        initializeListener()
    }

    fun initializeView(){

        _pickup = Calendar.getInstance()
        _return = Calendar.getInstance()

    }

    fun initializeListener(){
        binding.buttonPesan.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, KonfirmasiPenggunaActivity::class.java)
                val catatan = binding.editTextCatatan.text.toString()
                if (tanggalCheckin == null){
                    binding.textViewTanggalCheckin.error = "Tanggal harus dimasukkan!"
                    return
                }

                if (tanggalCheckout == null){
                    binding.textViewTanggalCheckout.error = "Tanggal harus dimasukkan!"
                    return
                }

                if (catatan.isEmpty()){
                    binding.editTextCatatan.error = "Tidak ada catatan kah?"
                    return
                }

                intent.putExtra("data", data)
                intent.putExtra("check_in", tanggalCheckin!!.timeInMillis)
                intent.putExtra("check_out", tanggalCheckout!!.timeInMillis)
                intent.putExtra("catatan", catatan)

                startActivity(intent)
                finish()
            }
        })
        binding.textViewTanggalCheckin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                openCalendarCheckin(_pickup as Calendar, binding.textViewTanggalCheckin)
            }
        })
        binding.textViewTanggalCheckout.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                openCalendarCheckout(_return as Calendar, binding.textViewTanggalCheckout)
            }
        })
        binding.textViewJamCheckin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                openTimePickerCheckin(_pickup as Calendar, binding.textViewJamCheckin)
            }
        })
        binding.textViewJamCheckout.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                openTimePickerCheckout(_return as Calendar, binding.textViewJamCheckout)
            }
        })

    }

    private fun openCalendarCheckin(rentalDate: Calendar , rentalDateText: TextView) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            rentalDate[year, month] = dayOfMonth
            tanggalCheckin = rentalDate
            rentalDateText.setText(dateFormat.format(rentalDate.time))
        }
        datePickerDialog.show()
    }

    private fun openCalendarCheckout(rentalDate: Calendar , rentalDateText: TextView) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            rentalDate[year, month] = dayOfMonth
            tanggalCheckout = rentalDate
            rentalDateText.setText(dateFormat.format(rentalDate.time))
        }
        datePickerDialog.show()
    }

    private fun openTimePickerCheckin(rentalTime: Calendar, rentalTimeText: TextView): Date? {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val min = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this,
            { view, hourOfDay, minute ->
                rentalTime[Calendar.HOUR_OF_DAY] = hourOfDay
                rentalTime[Calendar.MINUTE] = minute
                jamCheckin = rentalTime
                rentalTimeText.setText(timeFormat.format(rentalTime.time))
            }, hour, min, false
        )
        timePickerDialog.show()
        return calendar.time
    }

    private fun openTimePickerCheckout(rentalTime: Calendar, rentalTimeText: TextView): Date? {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val min = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this,
            { view, hourOfDay, minute ->
                rentalTime[Calendar.HOUR_OF_DAY] = hourOfDay
                rentalTime[Calendar.MINUTE] = minute
                jamCheckout = rentalTime
                rentalTimeText.setText(timeFormat.format(rentalTime.time))
            }, hour, min, false
        )
        timePickerDialog.show()
        return calendar.time
    }
}