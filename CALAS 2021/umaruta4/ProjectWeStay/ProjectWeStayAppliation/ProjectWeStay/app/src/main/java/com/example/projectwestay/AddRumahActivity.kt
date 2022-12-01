package com.example.projectwestay

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projectwestay.API.APIService
import com.example.projectwestay.API.APIUrl
import com.example.projectwestay.databinding.ActivityAddRumahBinding
import com.example.projectwestay.response.ExampleInsert
import com.example.projectwestay.response.LoginResponse
import com.example.projectwestay.response.Response
import com.example.projectwestay.response.StatusResponse
import com.example.projectwestay.session.Session
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class AddRumahActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddRumahBinding
    private var thumbnail : Uri? = null
    private lateinit var encodedThumbnail : String
    private lateinit var thumbnailName : String
    private lateinit var extraPhotos : List<Uri>

    var gson = GsonBuilder()
        .setLenient()
        .create()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(APIUrl.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service = retrofit.create(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRumahBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeListener()
    }

    fun initializeListener(){
        val startForResultThumbnail =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedImage: Uri = result.data?.data as Uri
                    var encodedImage: String? = null
                    var filePath: String? = null
                    val imageStream = contentResolver.openInputStream(selectedImage)
                    val bitmapStream = BitmapFactory.decodeStream(imageStream)

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)

                    if (cursor!!.moveToFirst()) {
                        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                        filePath = cursor.getString(columnIndex)
                        val extension = filePath.substring(filePath.lastIndexOf("."))
                        encodedImage = encodeImage(bitmapStream, extension)
                    }
                    cursor.close()


                    if (encodedImage == null && filePath == null){
                        Toast.makeText(applicationContext, "Gagal dalam menyimpan foto!", Toast.LENGTH_LONG).show()
                    }else {


                        thumbnail = selectedImage
                        encodedThumbnail = encodedImage as String
                        thumbnailName = filePath as String
                        binding.imageViewThumbnail.setImageURI(selectedImage)
                        binding.imageButtonPlus.visibility = View.INVISIBLE
                        binding.textViewAddImage.visibility = View.INVISIBLE

                        Log.d("BERHASIL FOTO ENCODED: ", encodedThumbnail)
                        Log.d("BERHASIL FOTO NAME: ", thumbnailName)
                    }
                }
            }

        binding.constraintLayoutAddImage.setOnClickListener (object: View.OnClickListener{

            override fun onClick(view: View){
                val intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startForResultThumbnail.launch(intent)
            }
        })

        binding.imageButtonPlus.setOnClickListener (object: View.OnClickListener{

            override fun onClick(view: View){
                val intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startForResultThumbnail.launch(intent)
            }
        })

        binding.buttonDaftar.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val namaHomestay = binding.editTextNamaHomestay.text.toString()
                val fasilitas = binding.editTextFasilitas.text.toString()
                val jenisKamar = binding.editTextJenisKamar.text.toString()
                val lokasi = binding.editTextLokasi.text.toString()
                val harga = binding.editTextHarga.text.toString()
                val userId = Session.read(applicationContext, "user_id", "0")

                if (thumbnail == null){
                    Toast.makeText(applicationContext, "Maaf, thumbnail masih kosong!", Toast.LENGTH_LONG).show()
                    return
                }

                if (namaHomestay.isEmpty()){
                    binding.editTextNamaHomestay.error = "Maaf, nama homestay harus ada isinya!"
                    return
                }

                if (fasilitas.isEmpty()){
                    binding.editTextFasilitas.error = "Maaf, fasilitas harus ada isinya!"
                    return
                }

                if (jenisKamar.isEmpty()){
                    binding.editTextFasilitas.error = "Maaf, jenis kamar harus ada isinya!"
                    return
                }

                if (lokasi.isEmpty()){
                    binding.editTextLokasi.error = "Maaf, lokasi harus ada isinya!"
                    return
                }

                if (harga.isEmpty()){
                    binding.editTextHarga.error = "Maaf, harga harus ada isinya!"
                    return
                }

                var call = service.tambahRumah(userId!!.toInt(), namaHomestay, fasilitas, jenisKamar, lokasi, harga.toInt(), encodedThumbnail, thumbnailName)

                call.enqueue(object: Callback<Response<StatusResponse>> {
                    override fun onResponse(call: Call<Response<StatusResponse>>, response: retrofit2.Response<Response<StatusResponse>>){
                        val tambahRumahResponse = response.body()

                        if (tambahRumahResponse == null){
                            Log.d("Responsenya : ", "NULL!!")
                            Toast.makeText(applicationContext, "Gagal memasukkan data!", Toast.LENGTH_LONG).show()
                            return
                        }

                        var error = response.body()?.error as Boolean
                        if (error) {
                            var message = response.body()?.message as String
                            Log.e("Server Error : ", message)
                            return
                        }
                        Log.d("MASUK : ", error.toString())
                        var responseData = response.body()?.response as StatusResponse
                        var status = responseData.status
                        if (status == 0){
                            Toast.makeText(applicationContext, "Maaf, data gagal dimasukkan!", Toast.LENGTH_LONG).show()
                            return
                        }

                        Toast.makeText(applicationContext, "Data berhasil dimasukkan!", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, HomePenyewaActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<Response<StatusResponse>>, t: Throwable){
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        Log.w("GAGAL API : ", t.message.toString())
                    }
                })
            }
        })

        binding.imageViewHome.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                val intent = Intent(applicationContext, HomePenyewaActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.imageViewAccount.setOnClickListener{v->
            val intent = Intent(applicationContext, AccountDetailPenyewaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun encodeImage(bm: Bitmap, extension: String): String? {
        val baos = ByteArrayOutputStream()
        if (extension == ".jpg" || extension == ".jpeg"){
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            Log.d("BERHASIL JPG/JPEG: ", "DISINI")
        }
        if (extension == ".png"){
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
            Log.d("BERHASIL PNG: ", "DISINI")
        }
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}