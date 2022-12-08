package com.tiernoparla.practica1;

import static java.lang.Math.*;

import java.util.Arrays;

public class Practica1 {
    public static void main(String[] args) {

        // creo un caso conocido para desarrollar sobre él
        final byte[] mensaje = { 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0 };
        final byte[] codigoHamming_test = { 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0 };

        // sender
        byte[] codigoHamming = calcularCodigoHamming(mensaje);

        // hacemos cambios a mano para ver que el reciever los detecta bien
        final int POSICION_MODIFICADA_1 = 5;
        final int POSICION_MODIFICADA_2 = 6;

        final byte[] ceroModificaciones = codigoHamming.clone();
        
        final byte[] unaModificacion = codigoHamming.clone();
        unaModificacion[POSICION_MODIFICADA_1] = (byte)(1 - unaModificacion[POSICION_MODIFICADA_1]);

        final byte[] dosModificaciones = codigoHamming.clone();
        dosModificaciones[POSICION_MODIFICADA_1] = (byte)(1 - dosModificaciones[POSICION_MODIFICADA_1]);
        dosModificaciones[POSICION_MODIFICADA_2] = (byte)(1 - dosModificaciones[POSICION_MODIFICADA_2]);

        final byte[] mensajeRecibido = ceroModificaciones;

        // hacemos el reciever
        final byte[] mensajeDeberiaSer = mensajeRecibido.clone();
        // recalculamos bits de paridad
        mensajeDeberiaSer[0] = calcularBitParidad(mensajeDeberiaSer,0);
        for(int i = 0; pow(2,i) < mensajeDeberiaSer.length; i++){
            mensajeDeberiaSer[(int)pow(2,i)] = calcularBitParidad(mensajeDeberiaSer, (int)pow(2,i));
        }

        // detectamos si hay algún error
        boolean hayError = Arrays.equals(mensajeDeberiaSer, mensajeRecibido);
        





    } // main

    private static byte[] calcularCodigoHamming(final byte[] mensaje) {

        byte[] codigoHamming = new byte[calculcarNumeroBitsParidad(mensaje) + mensaje.length + 1];

        int indiceMensaje = mensaje.length - 1;
        for (int i = codigoHamming.length - 1; i >= 0; i--) {
            if (esPotenciaDe2(i) || i == 0) {
                codigoHamming[i] = calcularBitParidad(codigoHamming, i);
            } else {
                codigoHamming[i] = mensaje[indiceMensaje];
                indiceMensaje--;
            }
        }
        return codigoHamming;
    }

    private static byte calcularBitParidad(byte[] codigoHamming, int posicion) {
        int counter = 0;
        for (int i = codigoHamming.length - 1; i > posicion; i--) {
            if ((posicion & i) == posicion) {
                counter += codigoHamming[i];
            }
        }
        return (byte)(counter % 2);
    }

    public static int calculcarNumeroBitsParidad(final byte[] mensaje) {
        int numeroBitsParidad = 0;
        while (pow(2, numeroBitsParidad) - (numeroBitsParidad + 1) < mensaje.length) {
            numeroBitsParidad++;
        }
        return numeroBitsParidad;
    } // caclulcarNumeroBitsParidad

    public static boolean esPotenciaDe2(int numero) {
        return (numero & (numero - 1)) == 0;
    }

} // Practica1
