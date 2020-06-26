package com.pandalab.calculadoraRfc;

import android.util.Log;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;

public class Rfc {
    private static HashMap<Character, Integer> tablaI;
    private static HashMap<Integer, Character> tablaII;
    private static HashMap<Character, Integer> tablaIII;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private LocalDate fechaNac;
    private String rfc;
    private String homoclave;
    private int digitoVerificador;

    Rfc(String nombre, String apellidoP, String apellidoM, LocalDate fechaNac) {
        this.nombre = nombre.trim().toUpperCase();
        this.apellidoP = apellidoP.trim().toUpperCase();
        this.apellidoM = apellidoM.trim().toUpperCase();
        this.fechaNac = fechaNac;
        this.homoclave = "";
        if (tablaI == null) {
            tablaI = new HashMap<>();
            char[] claves = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'Ñ', 'A', 'B', 'C',
                    'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int count = 0;
            tablaI.put(' ', count);
            for (char k: claves) {
                tablaI.put(k, count);
                if (count == 19) {
                    count++;
                } else if (count == 29) {
                    count += 2;
                }
                count++;
            }
        }
        if (tablaII == null) {
            tablaII = new HashMap<>();
            char[] valores = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                    'W', 'X', 'Y', 'Z'};
            for (int i = 0; i < valores.length; i++) {
                tablaII.put(i, valores[i]);
            }
        }
        if (tablaIII == null) {
            tablaIII = new HashMap<>();
            char[] claves = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                    'W', 'X', 'Y', 'Z', ' '};
            for (int i = 0; i < claves.length; i++) {
                tablaIII.put(claves[i], i);
            }
        }
    }

    private String getHomoclave() {
        return homoclave;
    }

    public String getRFC() {
        casoEspecialMaJo();
        casoEspecialCompuesto();
        rfc = apellidoP.substring(0, 2)
                + (apellidoM.length() >= 2 ? apellidoM.substring(0, 1) + nombre.substring(0, 1) : nombre.substring(0, 2))
                + String.format(Locale.ENGLISH, "%02d", fechaNac.getYear() % 100)
                + String.format(Locale.ENGLISH, "%02d", fechaNac.getMonthValue())
                + String.format(Locale.ENGLISH, "%02d", fechaNac.getDayOfMonth());
        casoEspecialPalabraInadecuada();
        calcularHomoclave();
        rfc += homoclave;
        calcularDigitoVerificador();
        rfc += digitoVerificador;
        Log.d("dbg", rfc);
        return rfc;
    }

    private void casoEspecialMaJo() {
        String[] excepciones = {"MARIA", "JOSE", "MA ", "MA."};
        for (String caso: excepciones) {
            if (nombre.indexOf(caso) == 0) {
                int fin = nombre.lastIndexOf(caso) + 1;
                nombre = nombre.substring(fin, nombre.length());
                break;
            }
        }
    }

    private void casoEspecialCompuesto() {
        String[] excepciones = {"DE LAS", "DE LOS", "DE LA", "VANDER", "VANDEN", "DEL", "DE", "VAN",
                "VON", "DA", "DI", "MC", "Y", "O"};
        for (String caso: excepciones) {
            if (nombre.indexOf(caso) == 0) {
                int fin  = nombre.lastIndexOf(caso) + 1;
                nombre = nombre.substring(fin);
                break;
            }
        }
    }

    private void casoEspecialPalabraInadecuada() {
        String[] excepciones = {"BUEI", "CACA", "CAGA", "CAKA", "COJE", "COGE", "COJO", "FETO",
                "JOTO", "KAGO", "KOJO", "KACO", "KULO", "LOCO", "MAMO", "MEAS", "MION", "BUEY",
                "CACO", "CAGO", "COJA", "COJI", "CULO", "GUEY", "KAGA", "KOGE", "KAKA", "LOCA",
                "LOKA", "MAME", "MEAR", "MEON", "MOCO", "PEDA", "PEDO", "PUTA", "PUTO", "QULO",
                "RUIN", "GATA", "PENE", "RATA", "VACA", "PITO"};

        for (String caso: excepciones) {
            if (rfc.indexOf(caso) == 0) {
                rfc = rfc.substring(0, 3) + "X";
                break;
            }
        }
    }

    private void calcularHomoclave() {
        String s = "0";
        for (char c: (apellidoP + " " + apellidoM + " " + nombre).toCharArray()) {
            //noinspection ConstantConditions
            s += String.format(Locale.ENGLISH, "%02d", tablaI.containsKey(c) ? tablaI.get(c) : 0);
        }
        Log.d("dbg", s);
        int a;
        int b;
        int sum = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            a = Integer.parseInt(s.substring(i, i+2));
            b = Integer.parseInt(s.substring(i+1, i+2));
            sum += a * b;
        }
        Log.d("dbg", "" + sum);
        sum %= 1000;
        int entero = sum / 34;
        int residuo = sum % 34;
        homoclave += tablaII.get(entero);
        homoclave += tablaII.get(residuo);
    }

    private void calcularDigitoVerificador() {
        int sum = 0;
        char[] caracteres = rfc.toCharArray();
        for (int i = 0; i < caracteres.length; i++) {
            Log.d("dbg", "" + tablaIII.get(caracteres[i]));
            //noinspection ConstantConditions
            sum += tablaIII.get(caracteres[i])
                    * (13 - i);
        }
        Log.d("dbg", "" + sum);
        digitoVerificador = 11 - (sum % 11);
    }
}
