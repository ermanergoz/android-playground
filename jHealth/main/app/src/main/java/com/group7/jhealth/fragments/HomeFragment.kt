package com.group7.jhealth.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.group7.jhealth.KEY_BUNDLE_CALORIE_HISTORY
import com.group7.jhealth.KEY_BUNDLE_INTAKE_HISTORY
import com.group7.jhealth.KEY_BUNDLE_WEIGHT_HISTORY
import com.group7.jhealth.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

/**
 * Class for Setting up the Home Page
 */
class HomeFragment : Fragment() {

    private lateinit var listener: HomeFragmentListener

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dietMonitoringButton.setOnClickListener {
            listener.onClickListener(R.id.dietMonitoringButton)
        }

        sleepMonitoringButton.setOnClickListener {
            listener.onClickListener(R.id.sleepMonitoringButton)
        }

        waterTrackerButton.setOnClickListener {
            listener.onClickListener(R.id.waterTrackerButton)
        }

        workoutPlanButton.setOnClickListener {
            listener.onClickListener(R.id.workoutPlanButton)
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as HomeFragmentListener
        } catch (err: ClassCastException) {
            throw ClassCastException(("$context must implement HomeFragmentListener"))
        }
    }

    interface HomeFragmentListener {
        fun onClickListener(clickedItemId: Int)
    }
}
