package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Struct;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nysil on 19-01-2018.
 */

public class EartrhQuakeAdapter extends ArrayAdapter<EarthQuake> {

    public EartrhQuakeAdapter(@NonNull Context context, ArrayList<EarthQuake> earthQuakes) {
        super(context, 0, earthQuakes);
    }

    private static final String LOCATION_SEPARATOR = " of ";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        EarthQuake currentEarthQuake = getItem(position);

        //since we have got the currentEarthQuae object that means the positions
        // we can now start showing them in our listview


        //first we show magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.list_magnitude);

        //fetching the background from magnitudeView
        GradientDrawable circleCreated=(GradientDrawable)magnitudeView.getBackground();

        //get correct color for the magnitude
        int colorBack= getMagnitudeColor(currentEarthQuake.getMagnitude());

        circleCreated.setColor(colorBack);

        //but we are taking magnitude in double which can give us a larhe decimal value.
        // so we format the input to a single digit value
        //by creating a method which return single digit value
        String formatted_Value_Of_Magnitude = formatMagnitude(currentEarthQuake.getMagnitude());
        magnitudeView.setText(formatted_Value_Of_Magnitude);


        //now we need to show the locations in both our main and offset location views
        TextView offsetLocationView=(TextView)listItemView.findViewById(R.id.list_offSetLocation);
        TextView mainLocationView=(TextView)listItemView.findViewById(R.id.list_primaryLocation);

        //taking location now to show in that text view
        String originalLocation =currentEarthQuake.getLocation();
        //this origianalLocation may or may not consist both primary and offset data


        //now if the data has a offset value it will consist "of" always
        // so what we can do is break that before of and after of
        //and store in two diffrent variables which we can use in setText() !!

        String offsetLocationData;
        String mainLocationData;


        // Check whether the originalLocation string contains the " of " text
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " of " text. We expect an array of 2 Strings, where
            // the first String will be "5km N" and the second String will be "Cairo, Egypt".
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            // Location offset should be "5km N " + " of " --> "5km N of"
            offsetLocationData = parts[0] + LOCATION_SEPARATOR;
            // Primary location should be "Cairo, Egypt"
            mainLocationData = parts[1];
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            offsetLocationData = getContext().getString(R.string.near_the);
            // The primary location will be the full location string "Pacific-Antarctic Ridge".
            mainLocationData = originalLocation;
        }

        offsetLocationView.setText(offsetLocationData);
        mainLocationView.setText(mainLocationData);

        //now its turn to set Date and Time

        TextView dateView= (TextView)listItemView.findViewById(R.id.list_date);
        TextView timeView=(TextView) listItemView.findViewById(R.id.list_time);

        //now getting date and time from EarthQuake Class

        Date dateandtime= new Date(currentEarthQuake.getDateandTime());


        String formattedtime= formatTime(dateandtime);

        String formatteddate= formatDate(dateandtime);

        dateView.setText(formatteddate);
        timeView.setText(formattedtime);


        return listItemView;

    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;

        int magnitudeFloor= (int)Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId); //?
    }

    private String formatDate(Date dateandtime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateandtime);
    }

    private String formatTime(Date dateandtime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateandtime);
    }


    //that previous method which took magnitude and returned a single decimal value
    private String formatMagnitude(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }


}
