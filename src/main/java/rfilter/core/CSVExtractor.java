package rfilter.core;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.text.ParseException;
import java.util.Iterator;

public class CSVExtractor implements Extractor {

    CSVReader csvr;

    public CSVExtractor(InputStream stream) {
        csvr = new CSVReader(new InputStreamReader(stream));
    }

    @Override
    public Iterator<Report> iterator() {
        return new Iter();
    }

    public class Iter implements Iterator<Report> {

        String [] nextLine;

        public Iter() {
            // skip headers
            try {
                nextLine = csvr.readNext();
            } catch (IOException e) {
                e.printStackTrace();
                nextLine = null;
            }
        }

        @Override
        public boolean hasNext() {
            try {
                nextLine = csvr.readNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (nextLine != null);
        }

        @Override
        public Report next() {
            //String [] nextLine;
            try {
                return new Report(nextLine[0],                           // client-address
                                  nextLine[1],                           // client-guid
                                  Report.DATE_FORMAT.parse(nextLine[2]), // request-time
                                  nextLine[3],                           // service-guid
                                  Integer.parseInt(nextLine[4]),         // retries-request
                                  Integer.parseInt(nextLine[5]),         // packets-requested
                                  Integer.parseInt(nextLine[6]),         // packets-serviced
                                  Integer.parseInt(nextLine[7]));        // max-hole-size
            } catch (ParseException e) {
                e.printStackTrace();
                return new Report();
            }
        }
    }
}
