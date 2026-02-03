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


## Parte 2: Ejercicio Black List Search
Siguiendo las instrucciones del enunciado en la clase hostBlackValidator agregamos el método checkHostParalelo, a pesar de que el enunciado pedía agregar el método checkHost, pero este ya estaba implementado como búsqueda no exhaustiva, por lo que decidimos dejarlo para poder hacer comparaciones entre este método y el que implementamos.

El método checkHostParalelo cuenta con 2 entradas, la primera de ellas la dirección ip que vamos a analizar y buscar en los repos de ips reportadas, y la segunda en cuantos hilos vamos a hacer la búsqueda. 

Entrando mas a detalle, primero creamos una lista para guardar las listas donde encontremos la ip reportada y revisa cuantas listas tenemos, luego hacemos la división del trabajo para asignar las tareas a cada uno de los hilos, en este caso asignamos de cual hasta cual lista debe revisar cada hilo, una vez iniciado el trabajo debemos esperar que todos terminen como una estrategia de sincronización, así la recolección de datos es confiable y podemos proceder con el reporte.

En la clase BlackListThread tenemos la esencia de la búsqueda, es decir es donde se verifica si la ip esta en la lista de listas asignadas al hilo, en esencia retorna donde encuentra la ip y cuenta cuantas veces la encuentra

Por ultimo en main hicimos una pequeña modificación cambiando el llamado de checkHost por checkHostParalelo y sus respectivos argumentos.








## Parte 2.1: Discusión
Como bien ya se explica dentro del enunciado, la estrategia es ineficiente en su planteamiento inicial, porque cada hilo completa su segmento completo sin importar si otros hilos ya encontraron suficientes ocurrencias. Si entre todos los hilos ya se alcanzó BLACK_LIST_ALARM_COUNT=5, deberían detenerse inmediatamente.
La nueva implementación pensada para minimizar el número de consultas, es el de implementar un contador compartido de ocurrencias entre todos los hilos, que permita:
1. Que cada hilo incremente el contador cuando encuentra una ocurrencia
2. Que cada hilo verifique periódicamente si ya se alcanzó el límite
3. Que los hilos se detengan cuando el contador llegue a 5
Sin embargo, esta nueva implementación trae un nuevo problema con respecto a la sincronización y la concurrencia porque:
- Múltiples hilos necesitan acceder y modificar el mismo contador (variable compartida)
- Se requiere exclusión mutua para evitar condiciones de carrera (race conditions)
- Se necesitan usar cosas como: synchronized, AtomicInteger, volatile para poder arreglar este nuevo problema.

## Parte 3: Evaluación de Desempeño

Primero vamos a evaluarlo con JAVA.

1 hilo:

Mientras estaba en ejecución:
![alt text](img/hilo1ejec.png)

![alt text](img/hilo1ejec2.png)

Después de su ejecución:
![alt text](img/hilo1later.png)

![alt text](img/hilo1later2.png)

#núcleos:

Mientras estaba en ejecución:

![alt text](img/coresBefore.png)

Después de su ejecución:

![alt text](img/coresAfter.png)

![alt text](img/coresAfter2.png)

#núcleos *2:

Mientras estaba en ejecución:

![alt text](img/cores2Before.png)

Después de su ejecución:

![alt text](img/cores2After.png)

![alt text](img/cores2After2.png)

Para 50, 100, 1000 y 10000 fue tan rápido todo que no pudimos sacar captura del jVisualVM.

100000 hilos:

Mientras estaba en ejecución:

![alt text](img/thousandBefore.png)

Después de la ejecución:

![alt text](img/thousandAfter.png)

![alt text](img/thousandAfter2.png)

1000000 hilos:

Mientras estaba en ejecución:

![alt text](img/millionBefore.png)

Después de la ejecución:

![alt text](img/millionAfter.png)

