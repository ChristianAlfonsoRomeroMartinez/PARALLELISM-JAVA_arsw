/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    //Inicia el registro de log, si axiste accede a el en caso que no lo crea
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());


    public List<Integer> checkHostParalelo(String ipaddress, int nThreads) {
        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();

        List<BlacklistCheckerThread> threads = new ArrayList<>();

        int serversPerThread = totalServers / nThreads;

        for (int i = 0; i < nThreads; i++) {
            int startServer = i * serversPerThread;
            int endServer = (i == nThreads - 1) ? totalServers : (i + 1) * serversPerThread;

            BlacklistCheckerThread thread = new BlacklistCheckerThread(ipaddress, startServer, endServer, skds);
            threads.add(thread);
            thread.start();
        }

        int totalCheckedLists = 0;
        int totalOccurrences = 0;

        for (BlacklistCheckerThread thread : threads) {
            try {
                thread.join();
                List<Integer> foundLists = thread.getFoundLists();
                blackListOcurrences.addAll(foundLists);
                totalOccurrences += thread.getOccurrencesCount();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.log(Level.SEVERE, "En espera del hilo", e);
            }
        }

        totalCheckedLists = totalServers;

        if (totalOccurrences >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
                new Object[]{totalCheckedLists, totalServers});
        return blackListOcurrences;
    } 

    
    
    
}
