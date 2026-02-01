package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;
import java.util.List;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class BlacklistCheckerThread extends Thread {
    private int startServer;
    private int endServer;
    private String ipaddress;
    private List<Integer> foundLists;
    private HostBlacklistsDataSourceFacade datasource;

    public BlacklistCheckerThread(String ipaddress, int startServer, int endServer, HostBlacklistsDataSourceFacade datasource) {
        this.ipaddress = ipaddress;
        this.startServer = startServer;
        this.endServer = endServer;
        this.datasource = datasource;
        this.foundLists = new LinkedList<>();
       
    }

    @Override
    public void run() {

        for (int i = startServer; i < endServer; i++) {
            if (datasource.isInBlackListServer(i, ipaddress)) {
                foundLists.add(i);
            }
        }


}

    public List<Integer> getFoundLists() {
        return foundLists;
    }

    public int getOccurrencesCount() {
        return foundLists.size();
    }

}