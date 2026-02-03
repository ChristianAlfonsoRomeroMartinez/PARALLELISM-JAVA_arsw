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


Primer computador a evaluar

Caracteristicas
![alt text](img/nacholaptop.jpeg)

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
Tuvimos que dentro de la misma ejecución dentro de VS, poner un print para que nos pudiera dar el tiempo de ejecución en estos casos.

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

# Segundo computador a evaluar

Caracteristicas
![alt text](img/chrislaptop.png)

## 1 hilo:

Mientras estaba en ejecución:
![alt text](img/coreh1.png)
![alt text](img/threds.png)

Después de su ejecución:
![alt text](img/despuescore1.png)
![alt text](img/thredsh1des.png)


## núcleos: 16

Mientras estaba en ejecución:
![alt text](img/coresin.png)
![alt text](img/threadsin.png)

Después de su ejecución:
![alt text](img/coreend.png)
![alt text](img/threadsend.png)

## núcleos: 16*2

Mientras estaba en ejecución:
![alt text](img/core2in.png)
![alt text](img/alwaystheard2.png)

Después de su ejecución:
![alt text](img/core2end.png)
![alt text](img/alwaystheard2.png)

## 100000 hilos:

Mientras estaba en ejecución:
![alt text](img/102core.png)
![alt text](img/102thread.png)

Después de su ejecución:
![alt text](img/102coreend.png)
![alt text](img/102threadend.png)

## 1000000 hilos:

Mientras estaba en ejecución:

![alt text](img/103core.png)
![alt text](img/103thread.png)

Después de la ejecución:

![alt text](img/103coreend.png)
![alt text](img/103threadend.png)

<<<<<<< Updated upstream
Ahora pasamos a la ejecución con Go; Go de por sí es es demasiado eficiente en su ejecución, haciendo que la concurrencia sea mucho más ligera. Entonces las primeras pruebas de ejecución (con baja cantidad de hilos), el tiempo de ejecución es 0ms, porque lo hace más eficiente de lo que Java lo llega a hacer. Desde 100 hilos se ven los tiempos (diferentes a 0), los cuales dieron los siguientes tiempos:

![alt text](img/go2.png)

![alt text](img/go.png)

Ya teniendo los tiempos utilizando los dos lenguajes, sacamos una gráfica para cada uno de los tiempos y estos fueron los resultados:

Java:
![alt text](img/tiempoJava.png)
Go:
![alt text](img/tiempoGo.png)



Como se puede ver, los tiempos son más elevados usando Java que usando Go, Java inicia en aproximadamente 150.000 ms, luego baja encontrando un punto óptimo con 1000 hilos, y luego vuelve a elevarse el tiempo llevando a casi 200.000 ms con 1.000.000 de hilos, mientras que con Go se mantiene cercano a 0ms hasta los 10.000 hilos, ya con 100.000 hilos aumentó el tiempo a 26 ms.

Viendo esto, e indagando un poco con respecto al funcionamiento a nivel de la concurrencia de cada lenguaje, podemos interpretar que Go es más eficiente en concurrencia que Java porque:
1. Goroutines son 500x más ligeras que threads
2. Scheduler en user-space evita syscalls costosos
3. Work stealing automático para mejor balanceo
4. Multiplexación M:N permite muchas goroutines sobre pocos threads reales
5. Manejo inteligente de bloqueos mantiene CPU ocupada

## Parte 4: Ejercicio Black List Search
1. 
El mejor desempeño no se logra con 500 hilos debido a que la cantidad de hilos usada, por más que da un tiempo aceptable, todavía no alcanza al punto óptimo de 1000 hilos. El tiempo utilizando 500 hilos da 309ms, mientras que con 1000 hilos nos daba 258ms, entonces con 1000 hilos es un 16% más rápido; si hacemos ahora la comparación con 200 hilos, el tiempo nos da 633ms, haciendo que con 500 hilos sea 51% más rápido. Es decir que a pesar que 500 hilos si es una buena cantidad de hilos según los recursos del computador, no saca el provecho al máximo, cosa que si ocurre con 1000 hilos.
Existe un punto de saturación donde agregar más hilos no mejora proporcionalmente el rendimiento debido al overhead y la fracción no paralelizable del código.

2. 
Speedup de 12 hilos:  153,990 / 9,250  = 16.6x
Speedup de 24 hilos:  153,990 / 4,644  = 33.2x

Mejora de 12→24: 9,250 / 4,644 = 1.99x (casi el doble)

La solución usando el doble de hilos que núcleos (24 hilos) se comporta significativamente mejor que usar solo los núcleos físicos (12 hilos):

1. Aprovecha hyperthreading:

- Tu CPU tiene 12 núcleos físicos con 2 threads lógicos cada uno
- Total: 24 threads lógicos
- Con 24 hilos aprovechas completamente el hyperthreading

2. Mejor ocultamiento de latencia:

- Las consultas a isInBlackListServer tienen latencia (I/O o delays)
- Con 24 hilos, mientras un thread espera, otro puede ejecutar
- Con solo 12 hilos, dejas recursos sin usar

3. Problema I/O-bound:

- Este problema NO es puramente CPU-bound
- Hay esperas en cada consulta a la blacklist
- Más hilos = mejor aprovechamiento del tiempo de espera

3.
Planteemos dos escenarios:
Escenario A: 100 hilos en 1 CPU
- Tiempo: 1,168 ms
- Limitación: 12 núcleos físicos compartidos
- Overhead: Context switching entre 100 hilos

Escenario B: c hilos en 100/c máquinas distribuidas
Supongamos que cada máquina tiene los mismos 12 núcleos.
Ejemplo con c = 12:
- 100/12 ≈ 8.33 máquinas (redondeamos a 9 máquinas)
- Cada máquina ejecuta 12 hilos
- Total: 9 máquinas × 12 hilos = 108 hilos

Aplicando la ley de Amdahls, se aplicaría mejor para el escenario B. Esto debido a que:
1. Paralelismo real:
- Escenario A: 100 hilos compitiendo por 12 núcleos → paralelismo limitado
- Escenario B: 9 máquinas × 12 núcleos = 108 núcleos reales → paralelismo verdadero

2. Menos overhead:
- Escenario A: Context switching masivo, contención de recursos
- Escenario B: Cada máquina maneja solo c hilos, mucho más eficiente

3. Cálculo favorece al escenario B
Escenario A: 1,168 ms
Escenario B: 1,028 ms

4. Fracción paralelizable (P) se aprovecha mejor:
- Con distribución real, cada máquina hace su trabajo independiente
- Solo hay comunicación al final para agregar resultados
- La parte no paralelizable (1-P) se minimiza

Entonces, si se mejora la distribución porque existiría una escalabilidad horizontal, menos contención por lo que cada máquina trabaja de manera independiente y hay un mejor uso de la Ley de Amdahls.

=======


