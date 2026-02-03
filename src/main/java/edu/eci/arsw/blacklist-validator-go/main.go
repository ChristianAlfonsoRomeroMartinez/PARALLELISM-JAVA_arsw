package main

import (
    "fmt"
    //"runtime"
    "time"
)

func main() {
    validator := &HostBlackListsValidator{}
    
    testIP := "202.24.34.55"

    //cores := runtime.NumCPU()
    //fmt.Printf("Número de núcleos disponibles: %d\n")
    
    numThreads := 50
    
    fmt.Printf("Iniciando prueba con %d goroutine(s)\n", numThreads)
    fmt.Printf("Probando IP: %s\n", testIP)
    
    startTime := time.Now()
    
    var blackListOccurrences []int
    if numThreads == 1 {
        blackListOccurrences = validator.CheckHost(testIP)
    } else {
        blackListOccurrences = validator.CheckHostParallel(testIP, numThreads)
    }
    
    duration := time.Since(startTime)

    fmt.Println("RESULTADOS:")
    fmt.Printf("Número de goroutines: %d\n", numThreads)
    fmt.Printf("Tiempo de ejecución: %d ms\n", duration.Milliseconds())
    fmt.Printf("Ocurrencias encontradas: %d\n", len(blackListOccurrences))
    fmt.Printf("Listas negras donde se encontró: %v\n", blackListOccurrences)
}