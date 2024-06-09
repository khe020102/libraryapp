package com.example.libraryapp
import PagelayoutFragment
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
//리뷰작성프래그먼트
class ReviewFragment : Fragment() {
    private lateinit var name: String
    private lateinit var loginID: String
    private lateinit var loginSort: String
    private var rate: Float = 0.0f
    private lateinit var mEditTextreview: EditText
    private lateinit var mEditTexttitle: EditText
    private lateinit var mTextViewResult: TextView
    private lateinit var dialog: AlertDialog
    private var temp: String = ""

    private val GET_GALLERY_IMAGE = 200
    private lateinit var reviewImageview: ImageView

    private val IP_ADDRESS = "서버 Ip 주소"
    private val TAG = "phpreviewup"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        val intent = requireActivity().intent
        name = intent.getStringExtra("name") ?: ""
        loginID = intent.getStringExtra("loginID") ?: ""
        loginSort = intent.getStringExtra("loginSort") ?: ""

        mTextViewResult = view.findViewById(R.id.textView_result)
        mTextViewResult.movementMethod = ScrollingMovementMethod()

        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        val okButton: Button = view.findViewById(R.id.okButton)
        val reviewRating: RatingBar = view.findViewById(R.id.reviewRating)

        mEditTexttitle = view.findViewById(R.id.titleEdit)
        mEditTextreview = view.findViewById(R.id.reviewEdit)


        cancelButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, PagelayoutFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        reviewRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            rate = rating
        }

        okButton.setOnClickListener {
            InsertData().execute(
                "http://$IP_ADDRESS/ImageUpload.php",
                name,
                rate.toString(),
                loginID,
                mEditTexttitle.text.toString(),
                mEditTextreview.text.toString(),
                temp
            )

            val builder = AlertDialog.Builder(requireContext())
            dialog = builder.setMessage("리뷰작성이 완료되었습니다.")
                .setNegativeButton("확인") { dialog, which ->
                    // Review2Fragment로 이동
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_container, PagelayoutFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                .create()
            dialog.show()
        }

        return view
    }

    inner class InsertData : AsyncTask<String, Void, String>() {
        private lateinit var progressDialog: ProgressDialog
        private var error: String? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(
                requireContext(),
                "Please Wait",
                null,
                true,
                true
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            mTextViewResult.text = result ?: error
            Log.d(TAG, "POST response  - $result")
        }

        override fun doInBackground(vararg params: String): String? {
            val name = params[1]
            val rate = params[2]
            val userID = params[3]
            val title = params[4]
            val review = params[5]
            val image = params[6]
            val serverURL = params[0]
            val postParameters =
                "name=$name&rate=$rate&userID=$userID&title=$title&review=$review&image=$image"
            return try {
                val url = URL(serverURL)
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()
                val outputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()
                val responseStatusCode = httpURLConnection.responseCode
                Log.d(TAG, "POST response code - $responseStatusCode")
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
                sb.toString()
            } catch (e: Exception) {
                Log.d(TAG, "InsertData: Error ", e)
                error = "Error: ${e.message}"
                null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            reviewImageview.setImageURI(selectedImageUri)
            try {
                val selPhoto: Bitmap? =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                selPhoto?.let { BitMapToString(it) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun BitMapToString(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val arr = baos.toByteArray()
        val image = Base64.encodeToString(arr, Base64.DEFAULT)
        try {
            temp = URLEncoder.encode(image, "utf-8")
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }
}
