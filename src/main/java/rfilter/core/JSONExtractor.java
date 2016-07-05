package rfilter.core;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;

public class JSONExtractor implements Extractor {

    JsonParser jsonr;

    public JSONExtractor(InputStream stream) {
        jsonr = Json.createParser(stream);
    }
    @Override
    public Iterator<Report> iterator() {
        return new Iter();
    }

    public class Iter implements Iterator<Report> {

        JsonParser.Event event;

        private void findNextRecord() {
            while (jsonr.hasNext()) {
                event = jsonr.next();
                if (event == JsonParser.Event.START_OBJECT) {
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            findNextRecord();
            return (event == JsonParser.Event.START_OBJECT);
        }

        @Override
        public Report next() {
            Report report = new Report();
            String key = "";
            while (jsonr.hasNext()) {
                event = jsonr.next();
                switch (event) {
                    case KEY_NAME:
                        key = jsonr.getString();
                        break;
                    case VALUE_NUMBER:
                    case VALUE_STRING:
                        updateReport(key, report);
                        break;
                    case END_OBJECT:
                        return report;
                    default:
                        return new Report();
                }
            }
            return new Report();
        }

        private void updateReport(String key, Report report) {
            switch (key) {
                case "retries-request":
                    report.retriesRequest = jsonr.getInt();
                    break;
                case "packets-serviced":
                    report.packetsServiced = jsonr.getInt();
                    break;
                case "packets-requested":
                    report.packetsRequested = jsonr.getInt();
                    break;
                case "max-hole-size":
                    report.maxHoleSize = jsonr.getInt();
                    break;
                case "request-time":
                    report.requestTime = Date.from(Instant.ofEpochMilli(jsonr.getLong()));
                    break;
                case "client-guid":
                    report.clientGuid = jsonr.getString();
                    break;
                case "service-guid":
                    report.serviceGuid = jsonr.getString();
                    break;
                case "client-address":
                    report.clientAddress = jsonr.getString();
                    break;
                default: ;
            }
        }
    }
}
