package com.example.libraryapp

import PagelayoutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class HomeFragment : Fragment() {

    private lateinit var vFlipper: ViewFlipper
    private lateinit var vFlipper2: ViewFlipper
    private lateinit var vFlipper3: ViewFlipper

    private val images = arrayOf(
        R.drawable.book1,
        R.drawable.book2,
        R.drawable.book3,
        R.drawable.book4,
        R.drawable.book5,
        R.drawable.book6
    )

    private val images2 = arrayOf(
        R.drawable.book6,
        R.drawable.book5,
        R.drawable.book4,
        R.drawable.book3,
        R.drawable.book2,
        R.drawable.book1
    )

    private val images3 = arrayOf(
        R.drawable.book3,
        R.drawable.book4,
        R.drawable.book5,
        R.drawable.book6,
        R.drawable.book2,
        R.drawable.book1
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        vFlipper = view.findViewById(R.id.flipper)
        vFlipper2 = view.findViewById(R.id.flipper2)
        vFlipper3 = view.findViewById(R.id.flipper3)

        setupFlipper(vFlipper, images)
        setupFlipper(vFlipper2, images2)
        setupFlipper(vFlipper3, images3)

        val img01btn: ImageButton = view.findViewById(R.id.img01)
        img01btn.setOnClickListener {
            // img01 상세정보 페이지로 이동
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, PagelayoutFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    // ViewFlipper 설정을 위한 메서드
    private fun setupFlipper(flipper: ViewFlipper, images: Array<Int>) {
        for (image in images) {
            addImageToFlipper(flipper, image)
        }
        flipper.flipInterval = 3000 // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        flipper.isAutoStart = true // 자동 시작 유무 설정
        // animation
        flipper.setInAnimation(requireContext(), android.R.anim.slide_in_left)
        flipper.setOutAnimation(requireContext(), android.R.anim.slide_out_right)
    }

    // 이미지를 ViewFlipper에 추가하는 메서드
    private fun addImageToFlipper(flipper: ViewFlipper, image: Int) {
        val imageView = ImageView(requireContext())
        imageView.setBackgroundResource(image)
        flipper.addView(imageView)
    }
}
