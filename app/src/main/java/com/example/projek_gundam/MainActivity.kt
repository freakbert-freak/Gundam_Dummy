package com.example.projek_gundam

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter:
            GundamAdapter

    private lateinit var etSearch:
            EditText

    private lateinit var tvCount:
            TextView

    private lateinit var tvPage:
            TextView

    private lateinit var tvStatus:
            TextView

    private lateinit var progressBar:
            ProgressBar

    private lateinit var btnPrev:
            Button

    private lateinit var btnNext:
            Button

    private val currentPageGundams =
        mutableListOf<Gundam>()

    private val allGundams =
        mutableListOf<Gundam>()

    private var allGundamsLoaded = false

    private var currentPage = 1
    private var totalPages = 1
    private var totalCount = 0

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(
            R.layout.activity_main
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat
                        .Type
                        .systemBars()
                )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        etSearch =
            findViewById(R.id.etSearch)

        tvCount =
            findViewById(R.id.tvCount)

        tvPage =
            findViewById(R.id.tvPage)

        tvStatus =
            findViewById(R.id.tvStatus)

        progressBar =
            findViewById(R.id.progressBar)

        btnPrev =
            findViewById(R.id.btnPrev)

        btnNext =
            findViewById(R.id.btnNext)

        val recyclerView =
            findViewById<RecyclerView>(
                R.id.rvGundam
            )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter =
            GundamAdapter(
                mutableListOf()
            ) { gundam ->

                openDetail(gundam)
            }

        recyclerView.adapter =
            adapter

        etSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    filterGundams(
                        s?.toString()
                            .orEmpty()
                    )
                }

                override fun afterTextChanged(
                    s: Editable?
                ) = Unit
            }
        )

        btnPrev.setOnClickListener {

            if (currentPage > 1) {

                currentPage--

                loadGundams(
                    currentPage
                )
            }
        }

        btnNext.setOnClickListener {

            if (
                currentPage < totalPages
            ) {

                currentPage++

                loadGundams(
                    currentPage
                )
            }
        }

        loadGundams(currentPage)
        loadAllGundams()
    }

    private fun loadGundams(
        page: Int
    ) {

        showLoading(true)

        tvStatus.visibility =
            View.GONE

        etSearch.setText("")

        Thread {

            try {

                val response =
                    ApiClient
                        .getGundams(page)

                runOnUiThread {

                    currentPageGundams
                        .clear()

                    currentPageGundams
                        .addAll(
                            response.results
                        )

                    totalCount =
                        response.info.count

                    totalPages =
                        response.info.pages

                    adapter.updateData(
                        currentPageGundams
                    )

                    updateHeaderAndPagination()

                    tvStatus.visibility =
                        if (
                            currentPageGundams
                                .isEmpty()
                        )
                            View.VISIBLE
                        else
                            View.GONE

                    if (
                        currentPageGundams
                            .isEmpty()
                    ) {
                        tvStatus.text =
                            "No mobile suits available"
                    }

                    showLoading(false)
                }

            } catch (e: Exception) {

                runOnUiThread {

                    adapter.updateData(
                        emptyList()
                    )

                    tvStatus.text =
                        "Unable to load mobile suits. Tap here to retry."

                    tvStatus.visibility =
                        View.VISIBLE

                    tvStatus
                        .setOnClickListener {

                            loadGundams(
                                currentPage
                            )
                        }

                    showLoading(false)
                }
            }

        }.start()
    }

    private fun loadAllGundams() {

        Thread {

            try {

                // Ambil halaman pertama untuk mengetahui jumlah page
                val firstResponse =
                    ApiClient.getGundams(1)

                val pages =
                    firstResponse.info.pages

                val tempList =
                    mutableListOf<Gundam>()

                tempList.addAll(
                    firstResponse.results
                )

                // Halaman 1 sudah diambil,
                // jadi mulai dari halaman 2
                for (page in 2..pages) {

                    val response =
                        ApiClient.getGundams(page)

                    tempList.addAll(
                        response.results
                    )
                }

                runOnUiThread {

                    allGundams.clear()
                    allGundams.addAll(tempList)

                    allGundamsLoaded = true

                    println(
                        "Total Gundam loaded: ${allGundams.size}"
                    )
                }

            } catch (e: Exception) {

                e.printStackTrace()

                runOnUiThread {
                    allGundamsLoaded = false
                }
            }

        }.start()
    }

    private fun filterGundams(
        keyword: String
    ) {

        val cleanKeyword =
            keyword.trim()

        // Kalau SearchBar kosong,
        // tampilkan kembali page yang sedang dibuka
        if (cleanKeyword.isEmpty()) {

            adapter.updateData(
                currentPageGundams
            )

            tvStatus.visibility =
                View.GONE

            updateHeaderAndPagination()

            return
        }

        // Semua data belum selesai didownload
        if (!allGundamsLoaded) {

            tvStatus.text =
                "Loading all Gundam data..."

            tvStatus.visibility =
                View.VISIBLE

            return
        }

        val result =
            allGundams.filter { gundam ->

                gundam.name.contains(
                    cleanKeyword,
                    ignoreCase = true
                ) ||

                        gundam.pilots.contains(
                            cleanKeyword,
                            ignoreCase = true
                        ) ||

                        gundam.header.contains(
                            cleanKeyword,
                            ignoreCase = true
                        )
            }

        adapter.updateData(result)

        if (result.isEmpty()) {

            tvStatus.text =
                "No Gundam found"

            tvStatus.visibility =
                View.VISIBLE

        } else {

            tvStatus.visibility =
                View.GONE
        }

        tvCount.text =
            "${result.size} RESULTS"

        tvPage.text =
            "SEARCH"

        btnPrev.isEnabled =
            false

        btnNext.isEnabled =
            false
    }

    private fun updateHeaderAndPagination() {

        tvCount.text =
            "$totalCount MOBILE SUITS"

        tvPage.text =
            "$currentPage / $totalPages"

        btnPrev.isEnabled =
            currentPage > 1

        btnNext.isEnabled =
            currentPage < totalPages
    }

    private fun showLoading(
        isLoading: Boolean
    ) {

        progressBar.visibility =
            if (isLoading)
                View.VISIBLE
            else
                View.GONE

        btnPrev.isEnabled =
            !isLoading &&
                    currentPage > 1

        btnNext.isEnabled =
            !isLoading &&
                    currentPage < totalPages
    }

    private fun openDetail(
        gundam: Gundam
    ) {

        val intent =
            Intent(
                this,
                DetailActivity::class.java
            ).apply {

                putExtra(
                    "id",
                    gundam.id
                )

                putExtra(
                    "name",
                    gundam.name
                )

                putExtra(
                    "imgUrl",
                    gundam.imgUrl
                )

                putExtra(
                    "pilots",
                    gundam.pilots
                )

                putExtra(
                    "header",
                    gundam.header
                )

                putExtra(
                    "details",
                    gundam.details
                )

                putExtra(
                    "wikiUrl",
                    gundam.wikiUrl
                )
            }

        startActivity(intent)
    }
}