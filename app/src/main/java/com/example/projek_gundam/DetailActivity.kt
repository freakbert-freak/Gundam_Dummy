package com.example.projek_gundam

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class DetailActivity :
    AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(
            R.layout.activity_detail
        )

        ViewCompat
            .setOnApplyWindowInsetsListener(
                findViewById(
                    R.id.detailMain
                )
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

        val btnBack =
            findViewById<Button>(
                R.id.btnBack
            )

        val tvId =
            findViewById<TextView>(
                R.id.tvDetailId
            )

        val imgGundam =
            findViewById<ImageView>(
                R.id.imgDetailGundam
            )

        val tvName =
            findViewById<TextView>(
                R.id.tvDetailName
            )

        val tvPilot =
            findViewById<TextView>(
                R.id.tvDetailPilot
            )

        val tvOverview =
            findViewById<TextView>(
                R.id.tvOverview
            )

        val btnWiki =
            findViewById<Button>(
                R.id.btnWiki
            )

        val id =
            intent.getIntExtra(
                "id",
                0
            )

        val name =
            intent
                .getStringExtra("name")
                .orEmpty()

        val imgUrl =
            intent
                .getStringExtra("imgUrl")
                .orEmpty()

        val pilots =
            intent
                .getStringExtra("pilots")
                .orEmpty()

        val header =
            intent
                .getStringExtra("header")
                .orEmpty()

        val details =
            intent
                .getStringExtra("details")

        val wikiUrl =
            intent
                .getStringExtra("wikiUrl")
                .orEmpty()

        tvId.text =
            "#${id.toString().padStart(3, '0')}"

        tvName.text =
            name.uppercase()

        tvPilot.text =
            if (pilots.isBlank())
                "Pilot data unavailable"
            else
                pilots

        val overviewText =
            when {

                !details.isNullOrBlank() ->
                    details

                header.isNotBlank() ->
                    header

                else ->
                    "Overview data unavailable"
            }

        tvOverview.text =
            decodeHtml(
                overviewText
            )

        Glide.with(this)
            .load(imgUrl)
            .placeholder(
                android.R.drawable
                    .ic_menu_gallery
            )
            .error(
                android.R.drawable
                    .ic_menu_report_image
            )
            .fitCenter()
            .into(imgGundam)

        btnBack.setOnClickListener {

            finish()
        }

        btnWiki.setOnClickListener {

            if (wikiUrl.isBlank()) {

                Toast.makeText(
                    this,
                    "Wiki URL unavailable",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            try {

                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(wikiUrl)
                    )

                startActivity(
                    browserIntent
                )

            } catch (
                e: ActivityNotFoundException
            ) {

                Toast.makeText(
                    this,
                    "No browser app found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun decodeHtml(
        text: String
    ): String {

        return if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.N
        ) {

            Html.fromHtml(
                text,
                Html.FROM_HTML_MODE_LEGACY
            ).toString()

        } else {

            Html.fromHtml(
                text
            ).toString()
        }
    }
}