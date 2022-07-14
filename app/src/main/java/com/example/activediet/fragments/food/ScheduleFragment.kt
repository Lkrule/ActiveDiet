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
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.adapters.MealAdapter
import com.example.activediet.adapters.FoodAdapter
import com.example.activediet.data.Food
import com.example.activediet.data.Meal
import com.example.activediet.databinding.FragmentScheduleBinding
import com.example.activediet.fragments.WelcomeFragment
import com.example.activediet.utilities.Constants.BMR
import com.example.activediet.utilities.Constants.MEALS_COUNT
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
class ScheduleFragment : Fragment(), MealAdapter.MealAdapterListener{
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: MealAdapter
    val viewModel: ScheduleViewModel by viewModels()
    private val foods = Array<MutableList<Food>>(MEALS_COUNT) { mutableListOf() }
    private lateinit var meals: MutableList<Meal>

    // date
    private lateinit var dateListener : DatePickerDialog.OnDateSetListener
    private val curDate = MutableLiveData<String>()


    @Inject
    lateinit var sharedPrefs: SharedPreferences


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        curDate.observe(viewLifecycleOwner)  {
            updateDateTextView()
            updateMealsByDate(it)
        }

        with(curDate) {
            postValue( SimpleDateFormat("dd-MM-yyyy")
                .format(Calendar.getInstance().time))
        }
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        // meals
        meals = mutableListOf(
            Meal(getString(R.string.breakfast),0f, 0f, 0f, 0f),
            Meal(getString(R.string.brunch),0f, 0f, 0f, 0f),
            Meal(getString(R.string.lunch),0f, 0f, 0f, 0f),
            Meal(getString(R.string.diner),0f, 0f, 0f, 0f),
            Meal(getString(R.string.supper),0f, 0f, 0f, 0f))


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
            val fixed = month + 1
            if ( month < 10) curDate.postValue("$day-0$fixed-$year")
            else curDate.postValue("$day-$fixed-$year")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // unnecessary code
        val CHANNEL_ID = "Daily_Reminder"
        val notificationId = 1337
        createNotificationChannel(CHANNEL_ID, notificationId)
        val set_reminder = binding.reminder
        set_reminder.setOnClickListener {
            sendNotification(CHANNEL_ID, notificationId)
        }


        // other part
        adapter = MealAdapter(meals, this)
        val rv = binding.dailyRv
        rv.adapter = adapter
        setupBar()


        // helper functions
        setOnClickListeners()

    }

     // unnecessary code

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

    // end part of notification


    private fun updateMealsByDate(date: String) {
        meals.forEachIndexed { index, _ ->
            viewModel.updateMeal(index, date)
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
                description.text = "Cals Over Time"
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
                calsOfDay += food.cals
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
                    invalidate()
                }
            }
        }
    }

    override fun addBtnClicked(pos: Int) {
        val action = ScheduleFragmentDirections
            .actionScheduleFragmentToAddCustomFoodFragment(pos, curDate.value.toString())
        findNavController().navigate(action)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun viewHolderBind(pos: Int, holder: MealAdapter.ViewHolder) {
        viewModel.foodsArray.also { it ->
            holder.binding.rv.adapter = FoodAdapter(foods[pos], viewModel)

            it[pos].observe(viewLifecycleOwner) { list ->
                foods[pos].clear()
                foods[pos].addAll(list)
                adapter.getItems()[pos].clearAll()

                val carbs = list.sumOf { it.carbs.toDouble() }.toFloat()
                val fats = list.sumOf { it.fats.toDouble() }.toFloat()
                val cals = list.sumOf { it.cals.toDouble() }.toFloat()
                val proteins = list.sumOf { it.proteins.toDouble() }.toFloat()
                adapter.getItems()[pos]
                    .update(cals, fats, carbs, proteins)


                adapter.viewHolders[pos].updateMeals(adapter.getItems()[pos])
                holder.binding.rv.adapter?.notifyDataSetChanged()
                update()
            }
        }
    }



    private fun updateDateTextView() {
        binding.dailyDate.text = curDate.value
    }

    @SuppressLint("SimpleDateFormat")
    private fun setOnClickListeners(){
        binding.dailyNextDayBtn.setOnClickListener {
            val format = SimpleDateFormat("dd-MM-yyyy")
            val date = format.parse(curDate.value.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 1)
            curDate.postValue(format.format(calendar.time))
        }

        binding.dailyPreviousDayBtn.setOnClickListener {
            val format = SimpleDateFormat("dd-MM-yyyy")
            val date = format.parse(curDate.value.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, -1)
            curDate.postValue(format.format(calendar.time))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun update(){
        val kcal = adapter.getItems().sumOf { it.cals.toDouble() }.toFloat()
        val fats = adapter.getItems().sumOf { it.fats.toDouble() }.toFloat()
        val proteins = adapter.getItems().sumOf { it.proteins.toDouble() }.toFloat()
        val carbs = adapter.getItems().sumOf { it.carbs.toDouble() }.toFloat()

        binding.apply {
            totalsCals.text = String.format("%.2f", kcal) + " / " +
                    String.format("%.2f", sharedPrefs.getFloat(BMR, 0f))
            totalsFats.text = String.format("%.2f", fats) + " g"
            totalsProteins.text = String.format("%.2f", proteins) + " g"
            totalsCarbs.text = String.format("%.2f", carbs) + " g"
        }
    }


}