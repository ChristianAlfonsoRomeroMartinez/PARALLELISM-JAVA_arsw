## Solución laboratorio - Hilos y paralelismo

Hecho por:
- Christian Alfonso Romero Martínez
- Ignacio Andrés Castillo Rendón

## Propósito laboratorio
El propósito de este laboratorio de la materia de Arquitecturas de Softwate (ARSW) es de tener una introducción a la programación con hilos en el lenguaje de Java.

## Parte 1: Introducción a Hilos en Java

Se realizó la implementación de los códigos "CountThread" y "CountThreadsMain".

"CountThread":
public class CountThread extends Thread {
    
    private int start;
    private int end;
    
    public CountThread(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": " + i);
        }
    }
}

"CountThreadsMain":
public class CountThreadsMain {
    
    public static void main(String a[]){
        CountThread thread1 = new CountThread(0, 99);
        CountThread thread2 = new CountThread(99, 199);
        CountThread thread3 = new CountThread(200, 299);
        
        //System.out.println("Usando start()");
        //thread1.start();
        //thread2.start();
        //thread3.start();
        System.out.println("Usando run()");
        thread1.run();
        thread2.run();
        thread3.run();
    }
}


Luego de la implementación en código, se probó la ejecución del main tanto con start como con run.

Al implementar el inicio tanto con start() y con run() se evidencian cambios significativos en la salida.

Con start:

![alt text](img/start.png)

Con run:

![alt text](img/run.png)

Así como se evidencia en las fotografías, hay diferencias claves si utilizamos start o run. En primer lugar, al iniciar con start, se crean nuevos hilos que ejecutan en paralelo, lo cual hace que se presente una salida entrelazada, es decir, se ejecuta el hilo 0, luego el hilo 1, luego el hilo 2 en la mayoría de los casos, también se presenta el caso donde a alguno de los hilos se le ejecute un número de más pero mantiene siempre ese orden.

Mientras que al iniciar con run, se ejecutan los hilos pequeños en el hilo principal secuencialmente, se presenta una salida ordenada por hilos, que así como se ve en la imagen, se ejecutan todos los hilos en orden (primero el hilo 0 con todos los números, luego el hilo 1 de la misma manera y por último el hilo 2)
