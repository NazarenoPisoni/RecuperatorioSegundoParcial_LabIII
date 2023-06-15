package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.CantidadSuperadaException;
import org.example.models.Jugador;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        TreeMap<String, ArrayList<Jugador>> ordenadosPorColorOjos = new TreeMap<>();
        TreeMap<String, ArrayList<Jugador>> ordenadosPorPosicion = new TreeMap<>();

        try{
            File file = new File("jugadores.json");
            ObjectMapper mapper = new ObjectMapper();
            Jugador [] lista = mapper.readValue(file, Jugador[].class);
            ArrayList<Jugador> jugadores = new ArrayList<>();
            for(Jugador j : lista){
                jugadores.add(j);
            }

            agregarPorColorDeOjos(jugadores, ordenadosPorColorOjos);
            agregarPorPosicion(jugadores, ordenadosPorPosicion);

            listar(ordenadosPorColorOjos);
            listar(ordenadosPorPosicion);

            int totalAzules = contarPorColorOjos(ordenadosPorColorOjos, "blue");
            System.out.println("Existen " + totalAzules + " jugadores con ojos azules.");

            int totalCuradores = contarPorPosicion(ordenadosPorPosicion, "curador");
            System.out.println("Existen " + totalCuradores + " curadores en el equipo.");

            try{
                int cantidad = compararCantidad(ordenadosPorColorOjos, "green", 2);
            }catch (CantidadSuperadaException e){
                System.out.println("Cantidad superada!");
                System.out.println("Nombre de la coleccion: " + e.getNombreColeccion());
                System.out.println("Clave: " + e.getClave());
                System.out.println("Diferencia: " + e.getDiferencia());

            }

            guardarJugadoresConMayorSueldo(ordenadosPorColorOjos, 2000);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Jugador> filtrarListaPorColorDeOjos(ArrayList<Jugador> jugadores, String eyeColor){
        ArrayList<Jugador> listaAux = new ArrayList<>();
        for(Jugador jugador : jugadores){
            if(jugador.getEyeColor().equals(eyeColor)){
                listaAux.add(jugador);
            }
        }
        return listaAux;
    }

    public static ArrayList<Jugador> filtrarListaPorPosicion(ArrayList<Jugador> jugadores, String position){
        ArrayList<Jugador> listaAux = new ArrayList<>();
        for(Jugador jugador : jugadores){
            if(jugador.getPosition().equals(position)){
                listaAux.add(jugador);
            }
        }
        return listaAux;
    }

    public static void agregarPorColorDeOjos(ArrayList<Jugador> lista, TreeMap<String, ArrayList<Jugador>> jugadores){
        for(Jugador jugador : lista){
            jugadores.put(jugador.getEyeColor(), filtrarListaPorColorDeOjos(lista, jugador.getEyeColor()));
        }
    }

    public static void agregarPorPosicion(ArrayList<Jugador> lista, TreeMap<String, ArrayList<Jugador>> jugadores){
        for(Jugador jugador : lista){
            jugadores.put(jugador.getPosition(), filtrarListaPorPosicion(lista, jugador.getPosition()));
        }
    }

    public static void listar(TreeMap<String, ArrayList<Jugador>> jugadores){
        for(Map.Entry<String, ArrayList<Jugador>> entry : jugadores.entrySet()){
            System.out.println("Clave: " + entry.getKey() + " , Valor: " + entry.getValue());
        }
    }

    public static int contarPorColorOjos(TreeMap<String, ArrayList<Jugador>> jugadores, String eyeColor){
        int contador = 0;
        for(Map.Entry<String, ArrayList<Jugador>> entry : jugadores.entrySet()){
            if(entry.getKey().equals(eyeColor)){
                contador = entry.getValue().size();
            }
        }
        return contador;
    }

    public static int contarPorPosicion(TreeMap<String, ArrayList<Jugador>> jugadores, String position){
        int contador = 0;
        for(Map.Entry<String, ArrayList<Jugador>> entry : jugadores.entrySet()){
            if(entry.getKey().equals(position)){
                contador = entry.getValue().size();
            }
        }
        return contador;
    }

    public static int compararCantidad(TreeMap<String, ArrayList<Jugador>> jugadores, String clave, int maximo) throws CantidadSuperadaException{
        int cantidad = 0;
        for(Map.Entry<String, ArrayList<Jugador>> entry : jugadores.entrySet()){
            if(entry.getKey().equals(clave)){
                cantidad = entry.getValue().size();
                if(cantidad > maximo){
                    throw new CantidadSuperadaException("Jugadores", entry.getKey(), cantidad - maximo);
                }
            }
        }
        return cantidad;
    }

    public static void guardarJugadoresConMayorSueldo(TreeMap<String, ArrayList<Jugador>> jugadores, double sueldo){
        ArrayList<Jugador> mayoresSueldos = new ArrayList<>();
        for(Map.Entry<String, ArrayList<Jugador>> entry : jugadores.entrySet()){
            ArrayList<Jugador> listaAux = entry.getValue();
            for(Jugador jugador : listaAux){
                if(Double.parseDouble(jugador.getBalance().replace("$", "").replace(",", "")) > sueldo){
                    mayoresSueldos.add(jugador);
                }
            }
        }
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("jugadoresMayorSueldo.dat"));
            objectOutputStream.writeObject(mayoresSueldos);
            objectOutputStream.close();
            System.out.println("Se guardaron con exito los siguientes jugadores: " + mayoresSueldos);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}