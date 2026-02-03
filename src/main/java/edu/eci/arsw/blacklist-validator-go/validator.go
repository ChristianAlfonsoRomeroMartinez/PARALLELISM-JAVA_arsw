package main

import (
    "fmt"
    "sync"
)

const (
    BlackListAlarmCount = 5
)

type BlacklistCheckerResult struct {
    FoundLists []int
}

type HostBlackListsValidator struct{}

func (v *HostBlackListsValidator) CheckHost(ipAddress string) []int {
    blackListOccurrences := []int{}
    occurrencesCount := 0
    
    skds := GetInstance()
    checkedListsCount := 0
    
    for i := 0; i < skds.GetRegisteredServersCount() && occurrencesCount < BlackListAlarmCount; i++ {
        checkedListsCount++
        
        if skds.IsInBlackListServer(i, ipAddress) {
            blackListOccurrences = append(blackListOccurrences, i)
            occurrencesCount++
        }
    }
    
    if occurrencesCount >= BlackListAlarmCount {
        skds.ReportAsNotTrustworthy(ipAddress)
    } else {
        skds.ReportAsTrustworthy(ipAddress)
    }
    
    fmt.Printf("INFO: Checked Black Lists: %d of %d\n", checkedListsCount, skds.GetRegisteredServersCount())
    
    return blackListOccurrences
}

func (v *HostBlackListsValidator) CheckHostParallel(ipAddress string, nThreads int) []int {
    skds := GetInstance()
    totalServers := skds.GetRegisteredServersCount()

    results := make(chan BlacklistCheckerResult, nThreads)

    var wg sync.WaitGroup

    serversPerThread := totalServers / nThreads
    
    for i := 0; i < nThreads; i++ {
        startServer := i * serversPerThread
        endServer := startServer + serversPerThread
   
        if i == nThreads-1 {
            endServer = totalServers
        }
        
        wg.Add(1)

        go func(start, end int) {
            defer wg.Done()
            
            foundLists := []int{}
            
            for j := start; j < end; j++ {
                if skds.IsInBlackListServer(j, ipAddress) {
                    foundLists = append(foundLists, j)
                }
            }
            
            results <- BlacklistCheckerResult{FoundLists: foundLists}
        }(startServer, endServer)
    }

    go func() {
        wg.Wait()
        close(results)
    }()

    blackListOccurrences := []int{}
    totalOccurrences := 0
    
    for result := range results {
        blackListOccurrences = append(blackListOccurrences, result.FoundLists...)
        totalOccurrences += len(result.FoundLists)
    }

    if totalOccurrences >= BlackListAlarmCount {
        skds.ReportAsNotTrustworthy(ipAddress)
    } else {
        skds.ReportAsTrustworthy(ipAddress)
    }
    
    fmt.Printf("INFO: Checked Black Lists: %d of %d\n", totalServers, totalServers)
    
    return blackListOccurrences
}