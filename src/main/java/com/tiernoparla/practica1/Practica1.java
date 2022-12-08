package com.tiernoparla.practica1;

import static java.lang.Math.*;
import java.util.Arrays;
import java.util.Random;

public class Practica1 {

    public static void main(String[] args) {

        // generar mensaje
        final int TAMANO = 10;
        final byte[] MENSAJE = crearMensaje(TAMANO);

        // sender
        final byte[] CODIGO_HAMMING = calcularCodigoHamming(MENSAJE);

        // noise
        final byte[] MENSAJE_RECIBIDO = generarCambiosAleatorios(CODIGO_HAMMING);

        // reciever
        concluirSiHayErrores(MENSAJE_RECIBIDO, recalcularMensaje(MENSAJE_RECIBIDO));

    } // main

    private static byte[] crearMensaje(final int TAMANO) {

        final int DOS = 2;
        final byte[] MENSAJE = new byte[TAMANO];

        Random rnd = new Random();

        for (int i = 0; i < TAMANO; i++) {
            MENSAJE[i] = (byte) rnd.nextInt(DOS);
        }

        return MENSAJE;

    }

    public static byte[] generarCambiosAleatorios(byte[] codigoHamming) {

        final byte CERO = 0;
        final byte UNO = 1;
        final byte DOS = 2;

        byte[] mensajeRecibido = codigoHamming.clone();

        Random rnd = new Random();
        final byte NUMERO_CAMBIOS = (byte) rnd.nextInt(3);

        switch (NUMERO_CAMBIOS) {
            case CERO:
                break;
            case UNO:
                mensajeRecibido = hacerUnCambio(mensajeRecibido);
                break;
            case DOS:
                mensajeRecibido = hacerDosCambios(codigoHamming, mensajeRecibido);
                break;
        }

        return mensajeRecibido;
    }

    public static byte[] hacerDosCambios(byte[] codigoHamming, byte[] mensajeRecibido) {

        while (Arrays.equals(codigoHamming, mensajeRecibido)) {
            mensajeRecibido = hacerUnCambio(mensajeRecibido);
            mensajeRecibido = hacerUnCambio(mensajeRecibido);
        }

        return mensajeRecibido;
    }

    public static byte[] hacerUnCambio(byte[] mensajeRecibido) {

        Random rnd = new Random();
        final int POSICION_CAMBIO = rnd.nextInt(mensajeRecibido.length);

        mensajeRecibido[POSICION_CAMBIO] = (byte) (1 - mensajeRecibido[POSICION_CAMBIO]);

        return mensajeRecibido;
    }

    public static byte[] recalcularMensaje(final byte[] mensajeRecibido) {

        final byte[] MENSAJE_DEBERIA_SER = mensajeRecibido.clone();

        MENSAJE_DEBERIA_SER[0] = calcularBitParidad(MENSAJE_DEBERIA_SER, 0);

        for (int i = 0; pow(2, i) < MENSAJE_DEBERIA_SER.length; i++) {
            final int POSICION = (int) pow(2, i);
            MENSAJE_DEBERIA_SER[POSICION] = calcularBitParidad(MENSAJE_DEBERIA_SER, POSICION);
        }

        return MENSAJE_DEBERIA_SER;
    }

    public static void concluirSiHayErrores(final byte[] mensajeRecibido, final byte[] mensajeDeberiaSer) {

        boolean hayError = !Arrays.equals(mensajeDeberiaSer, mensajeRecibido);

        final byte CERO = 0;

        if (!hayError) {
            System.out.println("Mensaje recibido sin errores.");
        } else {
            if (mensajeDeberiaSer[CERO] != mensajeRecibido[CERO]) {
                System.out.println("Mensaje recibido con un error en: " + detectarError(mensajeRecibido));
            } else {
                System.out.println("Mensaje recibido con 2 errores.");
            }
        }
    }

    public static int detectarError(final byte[] mensajeRecibido) {

        int posicionError = 0;

        for (int i = 0; i < mensajeRecibido.length; i++) {
            if (mensajeRecibido[i] != 0) {
                posicionError = (posicionError ^ i);
            }
        }

        return posicionError;
    }

    public static byte[] calcularCodigoHamming(final byte[] mensaje) {

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

    public static byte calcularBitParidad(byte[] codigoHamming, int posicion) {

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
