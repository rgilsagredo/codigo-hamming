package com.tiernoparla.practica1;

import static java.lang.Math.*;

import java.util.Arrays;
import java.util.Random;

public class Practica1 {
    /**
     *
     */

    public static void main(String[] args) {

        // creo un caso conocido para desarrollar sobre él
        final byte[] mensaje = { 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0 };
        final byte[] codigoHamming_test = { 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0 };

        // sender
        byte[] codigoHamming = calcularCodigoHamming(mensaje);

        // hacemos 0, 1 o 2 modificaciones random
        final byte CERO = 0;
        final byte UNO = 1;
        final byte DOS = 2;

        Random rnd = new Random();
        byte[] mensajeRecibido = codigoHamming;

        final byte NUMERO_CAMBIOS = (byte) rnd.nextInt(3);

        switch (NUMERO_CAMBIOS) {
            case CERO:
                break;
            case UNO:
                // hacer 1 cambio
                mensajeRecibido = hacerUnCambio(mensajeRecibido);
                break;
            case DOS:
                // hacer 2 cambios
                mensajeRecibido = hacerDosCambios(codigoHamming, mensajeRecibido);
                break;
        }

        /*
         * // hacemos cambios a mano para ver que el reciever los detecta bien
         * final int POSICION_MODIFICADA_1 = 8;
         * final int POSICION_MODIFICADA_2 = 8;
         * 
         * final byte[] ceroModificaciones = codigoHamming.clone();
         * 
         * final byte[] unaModificacion = codigoHamming.clone();
         * unaModificacion[POSICION_MODIFICADA_1] = (byte) (1 -
         * unaModificacion[POSICION_MODIFICADA_1]);
         * 
         * final byte[] dosModificaciones = codigoHamming.clone();
         * dosModificaciones[POSICION_MODIFICADA_1] = (byte) (1 -
         * dosModificaciones[POSICION_MODIFICADA_1]);
         * dosModificaciones[POSICION_MODIFICADA_2] = (byte) (1 -
         * dosModificaciones[POSICION_MODIFICADA_2]);
         * 
         * final byte[] mensajeRecibido = dosModificaciones;
         */

        // concluirSiHayErrores(mensajeRecibido, recalcularMensaje(mensajeRecibido));

    } // main

    private static byte[] hacerDosCambios(byte[] codigoHamming, byte[] mensajeRecibido) {
        while (Arrays.equals(codigoHamming, mensajeRecibido)) {
            mensajeRecibido = hacerUnCambio(mensajeRecibido);
            mensajeRecibido = hacerUnCambio(mensajeRecibido);
        }
        return mensajeRecibido;
    }

    private static byte[] hacerUnCambio(byte[] mensajeRecibido) {
        Random rnd = new Random();
        final int POSICION_CAMBIO = rnd.nextInt(mensajeRecibido.length);
        mensajeRecibido[POSICION_CAMBIO] = (byte) (1 - mensajeRecibido[POSICION_CAMBIO]);
        return mensajeRecibido;
    }

    private static byte[] recalcularMensaje(final byte[] mensajeRecibido) {
        // hacemos el reciever
        final byte[] mensajeDeberiaSer = mensajeRecibido.clone();
        // recalculamos bits de paridad
        mensajeDeberiaSer[0] = calcularBitParidad(mensajeDeberiaSer, 0);
        for (int i = 0; pow(2, i) < mensajeDeberiaSer.length; i++) {
            mensajeDeberiaSer[(int) pow(2, i)] = calcularBitParidad(mensajeDeberiaSer, (int) pow(2, i));
        }
        return mensajeDeberiaSer;
    }

    private static void concluirSiHayErrores(final byte[] mensajeRecibido, final byte[] mensajeDeberiaSer) {
        // detectamos si hay algún error
        boolean hayError = !Arrays.equals(mensajeDeberiaSer, mensajeRecibido);

        final byte CERO = 0;
        // concluimos si hay 0, 1 o 2 errores
        if (!hayError) {
            System.out.println("Mensaje recibido sin errores.");
        } else {
            // vemos si hay 1 error
            if (mensajeDeberiaSer[CERO] != mensajeRecibido[CERO]) {
                System.out.println("Mensaje recibido con un error en: " + detectarError(mensajeRecibido));
            } else {
                System.out.println("Mensaje recibido con 2 errores.");
            }
        }
    }

    private static int detectarError(final byte[] mensajeRecibido) {
        // detectar el error
        int posicionError = 0;
        for (int i = 0; i < mensajeRecibido.length; i++) {
            if (mensajeRecibido[i] != 0) {
                posicionError = (posicionError ^ i);
            }
        }
        return posicionError;
    }

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
        return (byte) (counter % 2);
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
