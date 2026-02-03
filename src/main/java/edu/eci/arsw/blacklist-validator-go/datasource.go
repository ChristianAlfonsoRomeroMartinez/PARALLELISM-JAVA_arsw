package main

import (
    "fmt"
    "math/rand"
    "sync"
    "time"
)

type HostBlacklistsDataSourceFacade struct {
    registeredServersCount int
    mu                     sync.Mutex
}

var (
    instance *HostBlacklistsDataSourceFacade
    once     sync.Once
)

func GetInstance() *HostBlacklistsDataSourceFacade {
    once.Do(func() {
        rand.Seed(time.Now().UnixNano())
        instance = &HostBlacklistsDataSourceFacade{
            registeredServersCount: 8192,
        }
    })
    return instance
}

func (ds *HostBlacklistsDataSourceFacade) GetRegisteredServersCount() int {
    return ds.registeredServersCount
}

func (ds *HostBlacklistsDataSourceFacade) IsInBlackListServer(serverIndex int, ipAddress string) bool {
    if ipAddress == "202.24.34.55" {
        return serverIndex%1000 == 0 || serverIndex%1500 == 0 || 
               serverIndex%2000 == 0 || serverIndex%2500 == 0 ||
               serverIndex%3000 == 0 || serverIndex%3500 == 0
    }

    if ipAddress == "200.24.34.55" {
        return serverIndex < 20
    }
    
    return false
}

func (ds *HostBlacklistsDataSourceFacade) ReportAsNotTrustworthy(ipAddress string) {
    ds.mu.Lock()
    defer ds.mu.Unlock()
    fmt.Printf("INFO: HOST %s Reported as NOT trustworthy\n", ipAddress)
}

func (ds *HostBlacklistsDataSourceFacade) ReportAsTrustworthy(ipAddress string) {
    ds.mu.Lock()
    defer ds.mu.Unlock()
    fmt.Printf("INFO: HOST %s Reported as trustworthy\n", ipAddress)
}