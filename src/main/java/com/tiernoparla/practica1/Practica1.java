package com.tiernoparla.practica1;

import static java.lang.Math.*;

import java.util.Arrays;

public class Practica1 {
    public static void main(String[] args) {

        // creo un caso conocido para desarrollar sobre él
        final byte[] mensaje = { 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0 };
        final byte[] codigoHamming_test = { 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0 };

        // sender
        // calcular num bit paridad
        int numeroBitsParidad = calculcarNumeroBitsParidad(mensaje);

        // crear array (vacío) con codigo de hamming
        byte[] codigoHamming = new byte[numeroBitsParidad + mensaje.length + 1];

        // rellenamos las posiciones del array que no no son especiales
        int indiceMensaje = 0;
        for (int i = 3; i < codigoHamming.length; i++) {
            if (!esPotenciaDe2(i)) {
                codigoHamming[i] = mensaje[indiceMensaje];
                indiceMensaje++;
            }
        }

    } // main

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
