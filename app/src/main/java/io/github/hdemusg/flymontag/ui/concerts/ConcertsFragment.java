package io.github.hdemusg.flymontag.ui.concerts;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.github.hdemusg.flymontag.R;

/*
  Author: Sumedh Garimella
  Description: The Concerts page allows fans to RSVP for events and add them to their calendars.
 */

public class ConcertsFragment extends Fragment {

    private ConcertsViewModel concertsViewModel;
    Button addEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        concertsViewModel =
                ViewModelProviders.of(this).get(ConcertsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_concerts, container, false);

        addEvent = root.findViewById(R.id.schedule);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GregorianCalendar calDate = null;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    calDate = new GregorianCalendar(2021, 03, 10);
                }

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(Events.TITLE, "Fly Montag @ Atlanta, GA")
                        .putExtra(Events.EVENT_LOCATION, "Student Center Parking Lot")
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                startActivity(intent);
            }
        });

        return root;
    }
}