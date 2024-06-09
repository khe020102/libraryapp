package com.example.libraryapp

import PagelayoutFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libraryapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val listItems = arrayListOf<ListLayout>()   // 리사이클러 뷰 아이템
    private val listAdapter = ListAdapter(listItems)    // 리사이클러 뷰 어댑터
    private var pageNumber = 1      // 검색 페이지 번호
    private var keyword = ""        // 검색 키워드

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러 뷰
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = listAdapter

        // 리스트 아이템 클릭 시 해당 위치로 이동
        listAdapter.setItemClickListener(object : ListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                //책 상세정보 페이지로 이동
                childFragmentManager.beginTransaction().replace(R.id.main_container, PagelayoutFragment()).commit()
            }
        })

        // 검색 버튼
        binding.btnSearch.setOnClickListener {
            keyword = binding.etSearchField.text.toString()
            pageNumber = 1
            searchKeyword(keyword, pageNumber)
        }

        // 이전 페이지 버튼
        binding.btnPrevPage.setOnClickListener {
            pageNumber--
            binding.tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }

        // 다음 페이지 버튼
        binding.btnNextPage.setOnClickListener {
            pageNumber++
            binding.tvPageNumber.text = pageNumber.toString()
            searchKeyword(keyword, pageNumber)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, page: Int) {
//
    }

    // 검색 결과 처리 함수
    //private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
    //    if (!searchResult?.documents.isNullOrEmpty()) {
    //        listItems.clear()
    //        binding.mapView.removeAllPOIItems()
    //        for (document in searchResult!!.documents) {
    //            val item = ListLayout(document.place_name,
    //                document.road_address_name,
    //                document.address_name,
    //                document.x.toDouble(),
    //                document.y.toDouble())
    //            listItems.add(item)

    //            val point = MapPOIItem()
    //            point.apply {
    //                itemName = document.place_name
    //                mapPoint = MapPoint.mapPointWithGeoCoord(document.y.toDouble(), document.x.toDouble())
    //                markerType = MapPOIItem.MarkerType.BluePin
    //                selectedMarkerType = MapPOIItem.MarkerType.RedPin
    //            }
    //            binding.mapView.addPOIItem(point)
    //        }
    //        listAdapter.notifyDataSetChanged()

    //        binding.btnNextPage.isEnabled = !searchResult.meta.is_end
    //        binding.btnPrevPage.isEnabled = pageNumber != 1
    //    } else {
    //        Toast.makeText(requireContext(), "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
    //    }
    //}
}
