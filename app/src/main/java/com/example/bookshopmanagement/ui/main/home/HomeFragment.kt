package com.example.bookshopmanagement.ui.main.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.BookShopApp.ui.profile.changepass.ChangePassFragment
import com.example.BookShopApp.utils.format.FormatDate
import com.example.BookShopApp.utils.format.FormatMoney
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Product
import com.example.bookshopmanagement.data.model.response.category.CategoryBestSeller
import com.example.bookshopmanagement.databinding.FragmentHomeBinding
import com.example.bookshopmanagement.databinding.LayoutAlertBinding
import com.example.bookshopmanagement.ui.main.user.UserViewModel
import com.example.bookshopmanagement.ui.signin.SignInFragment
import com.example.bookshopmanagement.utils.LoadingProgressBar
import com.example.bookshopmanagement.utils.MySharedPreferences
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelUser: UserViewModel
    private var binding: FragmentHomeBinding? = null
    private var bindingAlert: LayoutAlertBinding? = null
    private var dialog: Dialog? = null
    private var formatDate = FormatDate()
    private var formatMoney = FormatMoney()
    private var currentDate = ""
    private var currentYear = 0
    private var currentMonth = 0
    private lateinit var loadingProgressBar: LoadingProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        bindingAlert = LayoutAlertBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setView(bindingAlert?.root)
        dialog = builder.create()
        dialog?.setCancelable(false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        currentDate = formatDate.formatDate(LocalDateTime.now().toString())
        currentMonth = currentDate.split("/")[1].toInt()
        currentYear = currentDate.split("/")[2].toInt()
        loadingProgressBar = LoadingProgressBar(requireContext())
        loadingProgressBar.show()
        viewModelUser.getAllCustomer()
        viewModel.getRevenueYear(currentYear)
        viewModel.getOrderByToday(currentDate)
        viewModel.getRevenueMonthOfYear(currentYear)
        viewModel.getProductBestSeller()
        viewModel.getCategoryBestSeller()
        binding?.apply {
            textviewRevenue.text = resources.getString(R.string.revenue_year) + " " + currentYear
            textRevenueOverview.text =
                resources.getString(R.string.revenue_overview) + " " + currentYear
            imageChangeYear.setOnClickListener { view ->
                showPopupMenu(view)
            }
            imageAccount.setOnClickListener { view ->
                showPopupAccount(view)

            }
        }
    }

    private fun dpToPx(view: View, dp: Int): Int {
        val scale = view.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun initViewModel() {
        viewModel.totalOrder.observe(viewLifecycleOwner) {
            binding?.textNumberOrders?.text = it.toString()
        }
        viewModelUser.customers.observe(viewLifecycleOwner) {
            binding?.textNumberUser?.text = it.size.toString()
        }
        viewModel.revenueYear.observe(viewLifecycleOwner) {
            binding?.textAllRevenue?.text = formatMoney.formatMoney(it)
        }
        viewModel.revenueMonth.observe(viewLifecycleOwner) {
            lineChart(it)
            loadingProgressBar.cancel()
        }
        viewModel.books.observe(viewLifecycleOwner) {
            barChart(it)
        }
        viewModel.categoryBestSeller.observe(viewLifecycleOwner) {
            pieChart(it)
        }
        viewModel.revenueToday.observe(viewLifecycleOwner) {
            binding?.textRevenueToday?.text = formatMoney.formatMoney(it)
        }
    }

    private fun lineChart(listData: List<Long>) {
        val lineChart: LineChart? = binding?.lineChart
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, listData[0].toFloat()))
        entries.add(Entry(1f, listData[1].toFloat()))
        entries.add(Entry(2f, listData[2].toFloat()))
        entries.add(Entry(3f, listData[3].toFloat()))
        entries.add(Entry(4f, listData[4].toFloat()))
        entries.add(Entry(5f, listData[5].toFloat()))
        entries.add(Entry(6f, listData[6].toFloat()))
        entries.add(Entry(7f, listData[7].toFloat()))
        entries.add(Entry(8f, listData[8].toFloat()))
        entries.add(Entry(9f, listData[9].toFloat()))
        entries.add(Entry(10f, listData[10].toFloat()))
        entries.add(Entry(11f, listData[11].toFloat()))

        val dataSet = LineDataSet(entries, null)
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 8f

        val lineData = LineData(dataSet)
        lineChart?.data = lineData
        val xAxis: XAxis? = lineChart?.xAxis
        val xValues = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        xAxis?.valueFormatter = IndexAxisValueFormatter(xValues) // Đặt lại giá trị trục X
        xAxis?.position = XAxis.XAxisPosition.BOTTOM // Đặt vị trí của trục X
        xAxis?.textSize = 12f
        xAxis?.axisMinimum = 0f

        val yAxis: YAxis? = lineChart?.axisLeft
        yAxis?.axisMinimum = 0f // Đặt giá trị tối thiểu cho trục Y
        var max = listData[0]
        for (i in listData) {
            if (i > max)
                max = i
        }
        val countMax = max.toString().length
        val maxNew =
            ((max / (10.0.pow(countMax.toDouble() - 1))) + 1) * 10.0.pow(countMax.toDouble() - 1)
        yAxis?.axisMaximum = maxNew.toFloat()

        val rightYAxis: YAxis? = lineChart?.axisRight
        rightYAxis?.isEnabled = false

        // Tùy chỉnh biểu đồ và hiển thị
        lineChart?.description?.isEnabled = false
        lineChart?.legend?.isEnabled = false
        lineChart?.setScaleEnabled(false)
        lineChart?.setPinchZoom(false)
        lineChart?.invalidate()
    }

    private fun pieChart(category: List<CategoryBestSeller>) {
        val pieChart: PieChart? = binding?.pieChart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(category[0].totalSold.toFloat(), category[0].category.name))
        entries.add(PieEntry(category[1].totalSold.toFloat(), category[1].category.name))
        entries.add(PieEntry(category[2].totalSold.toFloat(), category[2].category.name))

        val dataSet = PieDataSet(entries, null)
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN)
        dataSet.valueTextSize = 12f

        pieChart?.isRotationEnabled = false
        pieChart?.setDrawEntryLabels(false)
        pieChart?.description?.isEnabled = false

        val pieData = PieData(dataSet)
        pieChart?.setEntryLabelColor(Color.BLACK)
        pieChart?.data = pieData
        pieChart?.invalidate()
    }

    private fun barChart(books: List<Product>) {
        val barChart: BarChart? = binding?.barChart
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, books[0].quantitySold.toFloat()))
        entries.add(BarEntry(1f, books[1].quantitySold.toFloat()))
        entries.add(BarEntry(2f, books[2].quantitySold.toFloat()))
        entries.add(BarEntry(3f, books[3].quantitySold.toFloat()))
        entries.add(BarEntry(4f, books[4].quantitySold.toFloat()))

        val bookNames = mutableListOf<String>()
        var max = books[0].quantitySold
        for (book in books) {
            book.name?.let { bookNames.add(it) }
            if (book.quantitySold > max)
                max = book.quantitySold
        }

        val dataSet = BarDataSet(entries, "Dataa")
        dataSet.setColors(
            Color.BLUE,
            Color.YELLOW,
            Color.GREEN,
            Color.RED,
            Color.CYAN,
        )

        val xAxis = barChart?.xAxis
//        xAxis?.valueFormatter = IndexAxisValueFormatter(bookNames)
//        xAxis?.position = XAxis.XAxisPosition.BOTTOM
//        xAxis?.setDrawLabels(true)
//        xAxis?.axisMinimum = 0f
        xAxis?.granularity = 1f
        xAxis?.setDrawLabels(false)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.axisLineWidth = 1f
        xAxis?.isGranularityEnabled = true


        val yAxis: YAxis? = barChart?.axisLeft
        yAxis?.axisMinimum = 0f
        yAxis?.axisLineWidth = 1f
        val countMax = max.toString().length
        val maxNew =
            ((max / (10.0.pow(countMax.toDouble() - 1))) + 1) * 10.0.pow(countMax.toDouble() - 1)
        yAxis?.axisMaximum = maxNew.toFloat()

        barChart?.description?.isEnabled = true
        barChart?.setFitBars(true)

        val dataBar = BarData(dataSet)
        barChart?.data = dataBar

        val rightYAxis: YAxis? = barChart?.axisRight
        rightYAxis?.isEnabled = false
        barChart?.description?.isEnabled = false
        barChart?.setDrawValueAboveBar(true)
        barChart?.setScaleEnabled(false)
        barChart?.setPinchZoom(false)
        barChart?.legend?.isEnabled = false
        barChart?.invalidate()
        barChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    val selectedBook = bookNames[e.x.toInt()]
                    Toast.makeText(requireContext(), selectedBook, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {
                // Đối với trường hợp không có cột nào được chọn
            }
        })

    }

    private fun showPopupAccount(view: View) {
        binding?.apply {
            val popupMenu = PopupMenu(requireContext(), view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_account, popupMenu.menu)

//            val title = popupMenu.menu[0].title.toString()
//            val spannable = SpannableString(title)
//            spannable.setSpan(
//                LeadingMarginSpan.Standard(dpToPx(view, 60), 0),
//                0,
//                spannable.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            spannable.setSpan(
//                ForegroundColorSpan(
//                    resources.getColor(R.color.black)
//                ), 0, spannable.length, 0
//            )
//            popupMenu.menu[0].title = spannable
            for (i in 0 until popupMenu.menu.size()) {
                val menuItem = popupMenu.menu.getItem(i)
                val title = menuItem.title.toString()
                val spannable = SpannableString(title)
                spannable.setSpan(
                    LeadingMarginSpan.Standard(
                        dpToPx(view, 50),
                        dpToPx(view, 10)
                    ), //90:left, 0:right
                    0,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        resources.getColor(R.color.black)
                    ), 0, spannable.length, 0
                )
                menuItem.title = spannable
            }

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_logout -> {
                        bindingAlert?.textDescription?.text =
                            resources.getString(R.string.sign_out_message)
                        bindingAlert?.textConfirm?.setOnClickListener {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container, SignInFragment()).commit()
                            MySharedPreferences.clearPreferences()
                            dialog?.dismiss()
                        }
                        bindingAlert?.textCancel?.setOnClickListener {
                            dialog?.dismiss()
                        }
                        dialog?.show()

                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, ChangePassFragment())
                            .addToBackStack("HomeFragment")
                            .commit()
                        return@setOnMenuItemClickListener true
                    }
                }
            }

            popupMenu.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showPopupMenu(view: View) {
        binding?.apply {
            val popupMenu = PopupMenu(requireContext(), view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_context_change_year, popupMenu.menu)

            for (i in 0 until popupMenu.menu.size()) {
                val menuItem = popupMenu.menu.getItem(i)
                menuItem.title = "Năm ${currentYear - i}"
                val title = menuItem.title.toString()
                val spannable = SpannableString(title)
                spannable.setSpan(
                    LeadingMarginSpan.Standard(dpToPx(view, 60), 0),
                    0,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(
                        resources.getColor(R.color.black)
                    ), 0, spannable.length, 0
                )
                menuItem.title = spannable
            }

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_2022 -> {
                        textviewRevenue.text =
                            resources.getString(R.string.revenue_year) + " " + "2022"
                        viewModel.getRevenueYear(2022)
                        loadingProgressBar.show()
                        textRevenueOverview.text =
                            resources.getString(R.string.revenue_overview) + " " + "2022"
                        viewModel.getRevenueMonthOfYear(2022)
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        textviewRevenue.text =
                            resources.getString(R.string.revenue_year) + " " + "2023"
                        viewModel.getRevenueYear(2023)
                        loadingProgressBar.show()
                        textRevenueOverview.text =
                            resources.getString(R.string.revenue_overview) + " " + "2023"
                        viewModel.getRevenueMonthOfYear(2023)
                        return@setOnMenuItemClickListener false
                    }
                }
            }

            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelUser.getAllCustomer()
        viewModel.getRevenueYear(currentYear)
        viewModel.getOrderByToday(currentDate)
        viewModel.getRevenueMonthOfYear(currentYear)
    }
}