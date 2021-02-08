package io.github.hdemusg.flymontag.ui.listen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class ListenFragment extends Fragment {

    private ListenViewModel listenViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listenViewModel =
                ViewModelProviders.of(this).get(ListenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listen, container, false);

        Button spotify = (Button) root.findViewById(R.id.spotify);
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Uri uriUrl = Uri.parse("spotify:artist:7KlvKwcScxowO6q1tSKApu");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        /*
        final TextView textView = root.findViewById(R.id.text_listen);
        listenViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }
}