package rfilter.core;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Iterator;

public class XMLExtractor implements Extractor {

    XMLInputFactory xmlif = XMLInputFactory.newInstance();
    XMLStreamReader xmlr;

    public XMLExtractor(InputStream stream) {
        try {
            xmlr = xmlif.createXMLStreamReader(stream.toString(), stream);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }



    @Override
    public Iterator<Report> iterator() {
        return new Iter();
    }


    public class Iter implements Iterator<Report> {

        int eventType;

        private boolean atRecord() {
            return (eventType == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("report"));
        }
        private void findNextRecord() throws XMLStreamException {
            while (xmlr.hasNext()) {
                eventType = xmlr.next();
                if (atRecord()) {
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            try {
                findNextRecord();
                return atRecord();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public Report next() {
            try {
                Report report = new Report();
                // iterate inner tags
                String tag = "";
                int prev;
                while (xmlr.hasNext()) {
                    prev = eventType;
                    eventType = xmlr.next();
                    if (eventType == XMLStreamConstants.START_ELEMENT) {
                        tag = xmlr.getLocalName();
                    } else if (eventType == XMLStreamConstants.CHARACTERS && prev == XMLStreamConstants.START_ELEMENT) {
                        updateReport(tag, report);
                    } else if (eventType == XMLStreamConstants.END_ELEMENT && xmlr.getLocalName().equals("report")) {
                        break;
                    }
                }
                return report;
            } catch (XMLStreamException | ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private void updateReport(String tag, Report report) throws ParseException {
            switch (tag) {
                case "retries-request":
                    report.retriesRequest = Integer.parseInt(xmlr.getText());
                    break;
                case "packets-serviced":
                    report.packetsServiced = Integer.parseInt(xmlr.getText());
                    break;
                case "packets-requested":
                    report.packetsRequested = Integer.parseInt(xmlr.getText());
                    break;
                case "max-hole-size":
                    report.maxHoleSize = Integer.parseInt(xmlr.getText());
                    break;
                case "request-time":
                    report.requestTime = Report.DATE_FORMAT.parse(xmlr.getText());
                    break;
                case "client-guid":
                    report.clientGuid = xmlr.getText();
                    break;
                case "service-guid":
                    report.serviceGuid = xmlr.getText();
                    break;
                case "client-address":
                    report.clientAddress = xmlr.getText();
                    break;
                default: ;
            }
        }
    }
}
