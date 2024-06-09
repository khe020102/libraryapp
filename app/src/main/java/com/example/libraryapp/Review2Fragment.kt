package com.example.libraryapp

import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Review2Fragment : Fragment() {

    // 리뷰 작성에 필요한 정보를 저장하는 전역 변수들
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var isbnNo: String
    private lateinit var score: String

    private val IP_ADDRESS = "http://52.78.146.166:8080/api/"
    private val TAG = "phpreviewdownload"

    private lateinit var mTextViewResult: TextView
    private lateinit var mArrayList: ArrayList<ReviewData>
    private lateinit var mAdapter: ReviewAdapter
    private lateinit var mRecyclerView: RecyclerView
    private var mJsonString: String? = null

    private var name: String? = null
    private var loginID: String? = null
    private var loginSort: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rreview, container, false)

        arguments?.let {
            name = it.getString("name")
            loginID = it.getString("loginID")
            loginSort = it.getString("loginSort")
        }

        mTextViewResult = view.findViewById(R.id.textView_main_result)
        mRecyclerView = view.findViewById(R.id.listView_main_list)

        mRecyclerView.addItemDecoration(DividerItemDecoration(mRecyclerView.context, 1))
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mTextViewResult.movementMethod = ScrollingMovementMethod()

        mArrayList = ArrayList()
        mAdapter = ReviewAdapter(requireContext(), mArrayList)
        mRecyclerView.adapter = mAdapter

        GetData().execute("http://$IP_ADDRESS/ReviewDownload.php", name)

        val btn1: Button = view.findViewById(R.id.reviewwirteButton)
        btn1.setOnClickListener {
            // 리뷰 작성 페이지로 이동
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, ReviewFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private inner class GetData : AsyncTask<String, Void, String>() {
        private lateinit var progressDialog: ProgressDialog
        private var errorString: String? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(requireContext(), "Please Wait", null, true, true)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            mTextViewResult.text = result
            Log.d(TAG, "response - $result")
            if (result == null) {
                mTextViewResult.text = errorString
            } else {
                mJsonString = result
                showResult()
            }
        }

        override fun doInBackground(vararg params: String): String? {
            val serverURL = params[0]
            val name = params[1]
            val postParameters = "name=$name"

            return try {
                val url = URL(serverURL)
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.doInput = true
                httpURLConnection.connect()

                val outputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode = httpURLConnection.responseCode
                Log.d(TAG, "response code - $responseStatusCode")

                val inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    sb.append(line)
                }

                bufferedReader.close()
                sb.toString().trim()

            } catch (e: Exception) {
                Log.d(TAG, "GetData : Error ", e)
                errorString = e.toString()
                null
            }
        }
    }

    private fun showResult() {
        val TAG_JSON = "user"
        val TAG_rate = "rate"
        val TAG_userID = "userID"
        val TAG_title = "title"
        val TAG_review = "review"

        try {
            val jsonObject = JSONObject(mJsonString)
            val jsonArray: JSONArray = jsonObject.getJSONArray(TAG_JSON)

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val rate = item.getString(TAG_rate)
                val userID = item.getString(TAG_userID)
                val title = item.getString(TAG_title)
                val review = item.getString(TAG_review)

                val reviewData = ReviewData()
                reviewData.review_rate = rate
                reviewData.review_userID = userID
                reviewData.review_title = title
                reviewData.review_review = review

                mArrayList.add(reviewData)
                mAdapter.notifyDataSetChanged()
            }

        } catch (e: JSONException) {
            Log.d(TAG, "showResult : ", e)
        }
    }
}
