/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv = new HostBlackListsValidator();
        
        String testIP = "202.24.34.55";
        
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Número de núcleos disponibles: " + cores);

<<<<<<< Updated upstream
        int numThreads = 200;
=======
        int numThreads =  1000000;
>>>>>>> Stashed changes
        
        System.out.println("Iniciando prueba con " + numThreads + " hilo(s)");
        System.out.println("Probando IP: " + testIP);

        long startTime = System.currentTimeMillis();

        List<Integer> blackListOcurrences = hblv.checkHostParalelo(testIP, numThreads);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("RESULTADOS:");
        System.out.println("Número de hilos: " + numThreads);
        System.out.println("Tiempo de ejecución: " + duration + " ms");
        System.out.println("Ocurrencias encontradas: " + blackListOcurrences.size());
        System.out.println("Listas negras donde se encontró: " + blackListOcurrences);
    }
}
