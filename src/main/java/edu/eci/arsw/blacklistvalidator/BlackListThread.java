package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class BlackListThread extends Thread {
    private int startIndex;
    private int endIndex;
    private String ipAddress;
    private HostBlacklistsDataSourceFacade skds;
    private List<Integer> blackListOccurrences;
    private int occurrencesCount;
    
    public BlackListThread(int start, int end, String ip, HostBlacklistsDataSourceFacade facade) {
        this.startIndex = start;
        this.endIndex = end;
        this.ipAddress = ip;
        this.skds = facade;
        this.blackListOccurrences = new LinkedList<>();
        this.occurrencesCount = 0;
    }
    
    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            if (skds.isInBlackListServer(i, ipAddress)) {
                blackListOccurrences.add(i);
                occurrencesCount++;
            }
        }
    }
    
    public int getOccurrencesCount() {
        return occurrencesCount;
    }
    
    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }
}
