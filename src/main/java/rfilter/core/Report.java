package rfilter.core;

import java.util.Date;

// datatype to represent a report antry
// (similar to Scala case class)
public class Report {
    public final String clientAddress;
    public final String clientGuid;
    public final Date   requestTime;
    public final String serviceGuid;
    public final int    retriesRequest;
    public final int    packetsRequested;
    public final int    packetsServiced;
    public final int    maxHoleSize;

    Report(String clientAddress,
           String clientGuid,
           Date   requestTime,
           String serviceGuid,
           int    retriesRequest,
           int    packetsRequested,
           int    packetsServiced,
           int    maxHoleSize)
    {
        this.clientAddress = clientAddress;
        this.clientGuid = clientGuid;
        this.requestTime = requestTime;
        this.serviceGuid = serviceGuid;
        this.retriesRequest = retriesRequest;
        this.packetsRequested = packetsRequested;
        this.packetsServiced = packetsServiced;
        this.maxHoleSize = maxHoleSize;
    }
}

