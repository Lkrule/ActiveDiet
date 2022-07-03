package com.example.activediet.fragments.food

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.adapters.MealsAdapter
import com.example.activediet.adapters.ProductAdapter
import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.MealTotals
import com.example.activediet.databinding.FragmentScheduleBinding
import com.example.activediet.fragments.WelcomeFragment
import com.example.activediet.utilities.Constants.BMR
import com.example.activediet.utilities.Constants.MEALS_COUNT
import com.example.activediet.utilities.run.CustomMarkerView
import com.example.activediet.viewmodels.food.ScheduleViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ScheduleFragment : Fragment(), MealsAdapter.MealsAdapterListener{
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: MealsAdapter
    val viewModel: ScheduleViewModel by viewModels()
    private val products = Array<MutableList<IngredientSearch>>(MEALS_COUNT) { mutableListOf() }
    private lateinit var meals: List<String>
    private val totalsList = Array(MEALS_COUNT) { MealTotals(0f, 0f, 0f, 0f) }

    // date
    private lateinit var currentDate: String
    private lateinit var dateListener : DatePickerDialog.OnDateSetListener

    @Inject
    lateinit var sharedPrefs: SharedPreferences


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDate = SimpleDateFormat("dd-MM-yyyy")
            .format(Calendar.getInstance().time)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        meals = listOf(
            getString(R.string.breakfast),
            getString(R.string.second_breakfast),
            getString(R.string.lunch),
            getString(R.string.diner),
            getString(R.string.supper)
        )
        binding.dailyDate.setOnClickListener{
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(requireContext()
                ,android.R.style.Theme_Dialog
                ,dateListener,year,month,day)

            dialog.window?.
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            binding.dailyDate.text = "$day-$month-$year"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val CHANNEL_ID = "Daily_Reminder"
        val notificationId = 1337
        createNotificationChannel(CHANNEL_ID, notificationId)
        val set_reminder = binding.reminder
        set_reminder.setOnClickListener {
            sendNotification(CHANNEL_ID, notificationId)
        }

        adapter = MealsAdapter(meals, this)
        val rv = binding.dailyRv
        rv.adapter = adapter



        setupBar()

        // helper functions
        updateDateTextView()
        loadAllProducts(currentDate)
        setOnClickListeners()

    }

    fun sendNotification(CHANNEL_ID: String,notificationId: Int){
        val intent = Intent(context, WelcomeFragment::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0,intent,0)
        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.diet_reminder2)
        val bitmapLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.foodsjpg)

        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Food Notification")
                .setContentText("I didn't eat my meal yet!")
                .setLargeIcon(bitmapLargeIcon)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
        if (builder != null) {
            with(context?.let { NotificationManagerCompat.from(it) }){
                this?.notify(notificationId, builder.build())
            }
        }
    }

    private fun createNotificationChannel(CHANNEL_ID: String, notificationId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun loadAllProducts(date: String) {
        meals.forEachIndexed { index, _ ->
            viewModel.loadProducts(index, date)
        }
    }


    private fun setupBar(){
        binding.apply {
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(false)
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisLeft.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.apply {
                description.text = "Avg Cals Over Time"
                legend.isEnabled = false
            }
        }


        viewModel.allProducts.observe(viewLifecycleOwner) { it ->
            var date = ""
            var calsOfDay = 0F
            val listDate : MutableList<Float> = mutableListOf()
            val sorted = it.sortedBy { it.date }
            for(food in sorted){
                if (date != food.date)  {
                    listDate.add(calsOfDay)
                    date = food.date
                    calsOfDay = 0F
                }
                calsOfDay += food.nutrients.getCalories().amount
            }
            //
            if (listDate.isNotEmpty()) {
                listDate.removeFirst()
                listDate.add(calsOfDay)
            }

            val allCals =
                listDate.indices.map { i -> BarEntry(i.toFloat(), listDate[i]) }
            val barDataSet = BarDataSet(allCals, "Cals Over Date").apply {
                valueTextColor = Color.WHITE
                color = ContextCompat.getColor(requireContext(), R.color.colorAccent)



            }

            binding.apply {
                barChart.apply {
                    data = BarData(barDataSet)
                    marker = CustomMarkerView(listDate, requireContext(), R.layout.marker_view)
                    invalidate()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun viewHolderBind(pos: Int, holder: MealsAdapter.ViewHolder) {
        viewModel.productsArray.also {
            holder.binding.rv.adapter = ProductAdapter(products[pos], viewModel, pos)
            it[pos].observe(viewLifecycleOwner) { list ->
                products[pos].clear()
                products[pos].addAll(list)

                totalsList[pos].clearAll()
                list.forEach { item ->
                    totalsList[pos].update(
                        kcal = item.nutrients.getCalories().amount,
                        fat = item.nutrients.getFat().amount,
                        carbs = item.nutrients.getCarbs().amount,
                        proteins = item.nutrients.getProtein().amount,
                    )
                }

                adapter.viewHolders[pos].updateTotals(totalsList[pos])

                holder.binding.rv.adapter?.notifyDataSetChanged()
                calcTotals()
            }
        }
    }

    override fun addItemClicked(pos: Int) {
        val action = ScheduleFragmentDirections
            .actionScheduleFragmentToAddCustomFoodFragment(currentDate)
        findNavController().navigate(action)
    }


    @SuppressLint("SetTextI18n")
    private fun calcTotals() {
        val kcal = totalsList.sumOf { it.kcal.toDouble() }.toFloat()
        val fats = totalsList.sumOf { it.fats.toDouble() }.toFloat()
        val proteins = totalsList.sumOf { it.proteins.toDouble() }.toFloat()
        val carbs = totalsList.sumOf { it.carbs.toDouble() }.toFloat()

        binding.apply {
            dailyTotalsKcal.text = String.format("%.2f", kcal) + " / " +
                    String.format("%.2f", sharedPrefs.getFloat(BMR, 0f))
            dailyTotalsFats.text = String.format("%.2f", fats) + " g"
            dailyTotalsProteins.text = String.format("%.2f", proteins) + " g"
            dailyTotalsCarbs.text = String.format("%.2f", carbs) + " g"
        }
    }


    private fun changeDate(byDays: Int) {
        val format = SimpleDateFormat("dd-MM-yyyy")
        val date = format.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, byDays)
        currentDate = format.format(calendar.time)
        updateDateTextView()
    }




    private fun updateDateTextView() {
        binding.dailyDate.text = currentDate
    }

    private fun setOnClickListeners(){
        binding.dailyNextDayBtn.setOnClickListener {
            changeDate(1)
            loadAllProducts(currentDate)
        }

        binding.dailyPreviousDayBtn.setOnClickListener {
            changeDate(-1)
            loadAllProducts(currentDate)
        }
    }


}