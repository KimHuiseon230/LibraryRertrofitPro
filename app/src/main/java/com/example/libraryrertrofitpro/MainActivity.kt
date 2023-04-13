package com.example.libraryrertrofitpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.Library
import com.data.Row
import com.example.libraryrertrofitpro.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Member

class MainActivity : AppCompatActivity() {
    companion object {
        val DB_NAME = "libraryDB01"
        val VERSION = 1
    }

    lateinit var binding: ActivityMainBinding
    lateinit var libraryData: MutableList<LibraryData>
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryData = mutableListOf()
        dbHelper = DBHelper(applicationContext, DB_NAME, VERSION)
        val libraryList = dbHelper.selectAll()
        if (libraryList == null) {
            // 서울 도서관 정보를 가져오는 로딩함수
            loadLibraries()
        } else {
            binding.recyclerView.adapter = LibraryAdapter(libraryList)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun loadLibraries() {
        val retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(SeoulOpenServiec::class.java)

        service.getLibrarys(SeoulOpenApi.API, SeoulOpenApi.LIST_TOTAL_COUNT)
            .enqueue(object : Callback<Library> {
                override fun onResponse(call: Call<Library>, response: Response<Library>) {
                    val library = response.body() // 공공데이터가 다 들어있음
                    showLibrary(library)
                    Toast.makeText(this@MainActivity, "서울 도서관 공공데이터를 가져왔습니다.", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<Library>, t: Throwable) {
                    Log.e("MainActivity", "서울 도서관 공공데이터를 가져올 수 없습니다. ${t.printStackTrace()}")
                    Toast.makeText(
                        this@MainActivity,
                        "서울 도서관 공공데이터를 가져올 수 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }


    private fun showLibrary(library: Library?) {
        val flag = false
        library?.let {
            for (data in it.SeoulPublicLibraryInfo.row) {
                val guCode = data.GU_CODE
                val lbName = data.LBRRY_NAME
                val tel = data.TEL_NO
                val address = data.ADRES
                val latitude = data.XCNTS
                val longitude = data.YDNTS
                val inputData = LibraryData(guCode, lbName, tel, address, latitude, longitude)
                if (dbHelper.insertTBL(inputData)) {
                    Log.e("MainActivity", "insert 성공")
                } else {
                    Log.e("MainActivity", "insert 실패")
                }

            }

        }
    }
}