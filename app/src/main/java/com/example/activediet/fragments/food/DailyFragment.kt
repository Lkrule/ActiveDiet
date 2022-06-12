package com.example.activediet.fragments.food

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.adapters.MealsAdapter
import com.example.activediet.adapters.ProductAdapter
import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.MealTotals
import com.example.activediet.databinding.FragmentDailyBinding
import com.example.activediet.fragments.WelcomeFragment
import com.example.activediet.utilities.run.Constants.BMR_PREF
import com.example.activediet.viewmodels.food.DailyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val MEALS_COUNT = 5

@AndroidEntryPoint
class DailyFragment : Fragment(), MealsAdapter.MealsAdapterListener,
    ProductAdapter.ProductAdapterListener {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MealsAdapter

    val viewModel: DailyViewModel by viewModels()

    private val products = Array<MutableList<IngredientSearch>>(MEALS_COUNT) { mutableListOf() }

    private lateinit var meals: List<String>

    private val totalsList = Array(MEALS_COUNT) { MealTotals(0f, 0f, 0f, 0f) }

    private lateinit var highlightedDate: String

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private var calculatedBMR: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        highlightedDate = SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time)
        calculatedBMR = sharedPrefs.getFloat(BMR_PREF, 0f)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
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
        meals = listOf(
            getString(R.string.breakfast),
            getString(R.string.second_breakfast),
            getString(R.string.lunch),
            getString(R.string.diner),
            getString(R.string.supper)
        )

        adapter = MealsAdapter(meals, this)
        val rv = binding.dailyRv
        rv.adapter = adapter

        updateDateTextView()

        loadAllProducts(highlightedDate)

        binding.dailyNextDayBtn.setOnClickListener {
            changeDate(1)
            loadAllProducts(highlightedDate)
        }

        binding.dailyPreviousDayBtn.setOnClickListener {
            changeDate(-1)
            loadAllProducts(highlightedDate)
        }

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

    override fun viewHolderBind(pos: Int, holder: MealsAdapter.ViewHolder) {
        viewModel.productsLiveDataArray.also {
            holder.binding.rv.adapter = ProductAdapter(products[pos], this, pos)
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

                holder.binding.rv.adapter?.let { adapter ->
                    adapter.notifyDataSetChanged()
                }

                updateDailyTotals(calculateDailyTotals())
            }
        }
    }

    override fun addItemClicked(pos: Int) {
        val action = DailyFragmentDirections
            .actionDailyFragmentToSearchFragment(highlightedDate)
        findNavController().navigate(action)
    }

    override fun onProductRemoveClick(ingredient: IngredientSearch, mealIndex: Int) {
        viewModel.deleteProduct(ingredient, mealIndex)
    }

    private fun calculateDailyTotals(): MealTotals {
        val kcal = totalsList.sumOf {
            it.kcal.toDouble()
        }.toFloat()

        val fats = totalsList.sumOf {
            it.fats.toDouble()
        }.toFloat()

        val proteins = totalsList.sumOf {
            it.proteins.toDouble()
        }.toFloat()

        val carbs = totalsList.sumOf {
            it.carbs.toDouble()
        }.toFloat()

        return MealTotals(kcal = kcal, fats = fats, carbs = carbs, proteins = proteins)
    }

    private fun updateDailyTotals(totals: MealTotals) {
        binding.apply {
            dailyTotalsKcalTv.text = String.format("%.2f", totals.kcal) + " / " +
                    String.format("%.2f", calculatedBMR)
            dailyTotalsFatsTv.text = String.format("%.2f", totals.fats) + " g"
            dailyTotalsProteinsTv.text = String.format("%.2f", totals.proteins) + " g"
            dailyTotalsCarbsTv.text = String.format("%.2f", totals.carbs) + " g"
        }
    }

    private fun changeDate(byDays: Int) {
        val format = SimpleDateFormat("dd-MM-yyyy")
        val date = format.parse(highlightedDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, byDays)
        highlightedDate = format.format(calendar.time)
        updateDateTextView()
    }

    private fun updateDateTextView() {
        binding.dailyDateTv.text = highlightedDate
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.daily_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.daily_menu_settings -> {
                val action = DailyFragmentDirections.actionDailyFragmentToCalculatorFragment()
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }
}