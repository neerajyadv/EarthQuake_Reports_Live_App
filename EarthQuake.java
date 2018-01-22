package com.example.android.quakereport;


public class EarthQuake {


        //this class i am creating to show magntiude, Place, and date

        private double nmagnitude;
        private String nlocation;
        private long ndateandtime; //in milliseconds thats why long
        private String nurl;

        EarthQuake(double nmagnitude, String nlocation, long ndateandtime, String nurl)
        {
            this.nmagnitude=nmagnitude;
            this.nlocation=nlocation;
            this.ndateandtime=ndateandtime;
            this.nurl=nurl;
        }

        public double getMagnitude()
        {
            return nmagnitude;
        }

        public String getLocation()
        {
            return nlocation;
        }

        public long getDateandTime()
        {
            return ndateandtime; //it gives milliseconds from which we can extract both time and date
        }

        public String getUrl()
        {
            return  nurl;
        }


}
