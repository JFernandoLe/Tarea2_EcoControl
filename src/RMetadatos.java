import java.net.*;
import java.io.*;
import java.util.*;

public class RMetadatos {
    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(5555);
            System.out.println("Servidor esperando datagrama..");
            int contador = 0,contadorInterno=0; //Contabdor interno es para contabilizar los elementos dentro del mapa
            Map<Integer, byte[]> paquetesRecibidos = new TreeMap<>(); // Para almacenar los paquetes en desorden

            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);    
                s.receive(p);
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
                int n = dis.readInt();
                
            
                // Almacenamos el paquete si no llega en orden
                if (n == contador) {
                    System.out.println("Se recibió el paquete " + n + " en orden correcto");
                    int tam = dis.readInt();
                    byte[] b = new byte[tam];
                    dis.read(b);
                    String cadena = new String(b);
                    System.out.println("Paquete recibido con los datos: #paquete->" + n + " con " + tam + " bytes y el mensaje: " + cadena);
                    contador++;
                    // Vemos si el siguiente paquete está en el mapa
                    if(paquetesRecibidos.containsKey(contador)){
                    contadorInterno=contador; // Si está, lo procesamos
                      while (paquetesRecibidos.containsKey(contadorInterno)) { // Procesamos todos los paquetes que estén en el mapa
                        byte[] b1 = paquetesRecibidos.get(contadorInterno); // Obtenemos el paquete
                        String cadena1 = new String(b1); // Convertimos el paquete a cadena
                        int tam1 = b1.length; // Obtenemos el tamaño del paquete
                        System.out.println("Paquete recuperado con los datos: #paquete->" + contadorInterno + " con " + tam1 + " bytes y el mensaje: " + cadena1);
                        contadorInterno++;
                    }
                    contador=contadorInterno;
                    }
                } else {
                    System.out.println("Se recibió el paquete " + n + " fuera de orden");
                    // Almacenamos el paquete para procesarlo más tarde
                    int tam = dis.readInt();
                    byte[] b = new byte[tam];
                    dis.read(b);
                    paquetesRecibidos.put(n, b);
                }
                dis.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
