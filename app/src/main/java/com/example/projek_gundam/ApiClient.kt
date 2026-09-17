package com.example.projek_gundam

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    fun getGundams(page: Int): GundamResponse {

        val url = URL(
            "https://gundam-api.pages.dev/api/gundams?page=$page"
        )

        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000

            if (connection.responseCode !in 200..299) {
                throw Exception(
                    "HTTP ${connection.responseCode}"
                )
            }

            val reader = BufferedReader(
                InputStreamReader(connection.inputStream)
            )

            val response = StringBuilder()

            var line: String?

            while (true) {
                line = reader.readLine()

                if (line == null) {
                    break
                }

                response.append(line)
            }

            reader.close()

            val jsonObject =
                JSONObject(response.toString())

            val infoObject =
                jsonObject.getJSONObject("info")

            val resultsArray =
                jsonObject.getJSONArray("results")

            val info = ApiInfo(
                count = infoObject.getInt("count"),
                pages = infoObject.getInt("pages"),

                next =
                    if (infoObject.isNull("next"))
                        null
                    else
                        infoObject.getString("next"),

                prev =
                    if (infoObject.isNull("prev"))
                        null
                    else
                        infoObject.getString("prev")
            )

            val gundamList =
                mutableListOf<Gundam>()

            for (i in 0 until resultsArray.length()) {

                val obj =
                    resultsArray.getJSONObject(i)

                val gundam = Gundam(
                    id = obj.getInt("id"),
                    wikiName =
                        obj.optString("wikiName"),
                    wikiUrl =
                        obj.optString("wikiUrl"),
                    name =
                        obj.optString("name"),
                    header =
                        obj.optString("header"),

                    details =
                        if (obj.isNull("details"))
                            null
                        else
                            obj.optString("details"),

                    imgUrl =
                        obj.optString("imgUrl"),

                    pilots =
                        obj.optString("pilots")
                )

                gundamList.add(gundam)
            }

            return GundamResponse(
                info = info,
                results = gundamList
            )

        } finally {
            connection.disconnect()
        }
    }
}