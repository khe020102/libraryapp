
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.libraryapp.Bookinformation2Fragment
import com.example.libraryapp.BookinformationFragment
import com.example.libraryapp.Location2Fragment
import com.example.libraryapp.LocationFragment
import com.example.libraryapp.R
import com.example.libraryapp.Review2Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class Pagelayout2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.page_layout2, container, false)

        // Load image from URL and set it to ImageView
        val imageView = view.findViewById<ImageView>(R.id.img)
        Picasso.get().load("https://img.libbook.co.kr/V2/BookImgK13/9788955593884.gif").into(imageView)

        // ViewPager2에 어댑터 설정
        val adapter = PagerAdapter(requireActivity())
        adapter.addFragment(Bookinformation2Fragment(), "책 정보")
        adapter.addFragment(Location2Fragment(), "위치")
        adapter.addFragment(Review2Fragment(), "리뷰")

        // ViewPager2 참조
        val viewPager = view.findViewById<ViewPager2>(R.id.book_viewPager)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        val tabLayout = view.findViewById<TabLayout>(R.id.myPagetabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()



        return view
    }

    inner class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        fun getPageTitle(position: Int): CharSequence? = fragmentTitleList[position]
    }
}