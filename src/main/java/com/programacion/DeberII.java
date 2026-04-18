package com.programacion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DeberII {
    public static void main(String[] args) {

        File carpeta = new File("src/main/resources/assets");
        File archivoEntrada = new File(carpeta, "img2.png");

        // Verificamos una sola vez si la imagen existe
        if (!archivoEntrada.exists()) {
            System.out.println("Error: No se encuentra el archivo en la carpeta assets.");
            return;
        }

        System.out.println("Iniciando procesamiento de imágenes...\n");

        // 1, 2 y 5. Estos efectos no dependen de N, se llaman una sola vez
        aplicarEfectoEsmerilado(archivoEntrada, carpeta);
        aplicarDesvanecimientoCircular(archivoEntrada, carpeta);
        aplicarBlancoYNegro(archivoEntrada, carpeta);

        // 3, 4 y 6. El profesor pide analizar con estos valores exactos:
        int[] valoresDeN = {2, 4, 8, 64, 128, 255};

        System.out.println("\nGenerando variaciones para N...");
        for (int N : valoresDeN) {
            aplicarEfectoRetro1(archivoEntrada, carpeta, N);
            aplicarEfectoRetro2(archivoEntrada, carpeta, N);
            aplicarEscalaDeGrisesN(archivoEntrada, carpeta, N);
        }

        System.out.println("\n¡Todos los efectos han sido generados correctamente!");
    }

    // --------------------------------------------------------
    // MÉTODO 1: VIDRIO ESMERILADO
    // --------------------------------------------------------
    public static void aplicarEfectoEsmerilado(File entrada, File carpetaSalida) {
        try {
            System.out.println("-> Generando Efecto Vidrio Esmerilado...");
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();

            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel = original.getRGB(x, y);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    // Brillo y Alfa
                    double brillo = (0.2126 * r) + (0.7152 * g) + (0.0722 * b);
                    int a = (int) (50 + (brillo / 255.0) * 205);

                    if (a > 255) a = 255;

                    int nuevoPixel = (a << 24) | (r << 16) | (g << 8) | b;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }

            File archivoSalida = new File(carpetaSalida, "1_esmerilado.png");
            ImageIO.write(resultado, "png", archivoSalida);
            System.out.println(" Guardado como: resultado_esmerilado.png");

        } catch (IOException e) {
            System.out.println("Falló el esmerilado: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // METODO 2: DESVANECIMIENTO CIRCULAR
    // --------------------------------------------------------
    public static void aplicarDesvanecimientoCircular(File entrada, File carpetaSalida) {
        try {
            System.out.println("-> Generando Desvanecimiento Circular...");
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();

            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            double centroX = ancho / 2.0;
            double centroY = alto / 2.0;
            double distanciaMaxima = Math.sqrt(Math.pow(centroX, 2) + Math.pow(centroY, 2));

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel = original.getRGB(x, y);
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    // Distancia y Alfa
                    double distanciaAlCentro = Math.sqrt(Math.pow(x - centroX, 2) + Math.pow(y - centroY, 2));
                    double proporcionOpacidad = 1.0 - (distanciaAlCentro / distanciaMaxima);

                    int a = (int) (255 * proporcionOpacidad);

                    if (a < 0) a = 0;
                    if (a > 255) a = 255;

                    int nuevoPixel = (a << 24) | (r << 16) | (g << 8) | b;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }

            File archivoSalida = new File(carpetaSalida, "2_circular.png");
            ImageIO.write(resultado, "png", archivoSalida);
            System.out.println("   [OK] Guardado como: resultado_circular.png");

        } catch (IOException e) {
            System.out.println("   [ERROR] Falló el circular: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // METODO 3: EFECTO RETRO 1 (N Colores por canal RGB)
    // --------------------------------------------------------
    public static void aplicarEfectoRetro1(File entrada, File carpetaSalida, int N) {
        try {
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();
            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int p = original.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    // Fórmula robusta para cuantizar a N niveles
                    int nuevoR = (int)(Math.round((r / 255.0) * (N - 1)) * (255.0 / (N - 1)));
                    int nuevoG = (int)(Math.round((g / 255.0) * (N - 1)) * (255.0 / (N - 1)));
                    int nuevoB = (int)(Math.round((b / 255.0) * (N - 1)) * (255.0 / (N - 1)));

                    int nuevoPixel = (a << 24) | (nuevoR << 16) | (nuevoG << 8) | nuevoB;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }
            ImageIO.write(resultado, "png", new File(carpetaSalida, "3_retro1_N" + N + ".png"));
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // --------------------------------------------------------
    // METODO 4: EFECTO RETRO 2 (2 Canales a N niveles, el 3ro en 0)
    // Ejemplo: Conservamos R y G, anulamos B
    // --------------------------------------------------------
    public static void aplicarEfectoRetro2(File entrada, File carpetaSalida, int N) {
        try {
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();
            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int p = original.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    // Ignoramos el canal 'b' para este efecto

                    int nuevoR = (int)(Math.round((r / 255.0) * (N - 1)) * (255.0 / (N - 1)));
                    int nuevoG = (int)(Math.round((g / 255.0) * (N - 1)) * (255.0 / (N - 1)));
                    int nuevoB = 0; // ANULAMOS EL CANAL AZUL

                    int nuevoPixel = (a << 24) | (nuevoR << 16) | (nuevoG << 8) | nuevoB;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }
            ImageIO.write(resultado, "png", new File(carpetaSalida, "4_retro2_RG_N" + N + ".png"));
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // --------------------------------------------------------
    // METODO 5: EFECTO BLANCO Y NEGRO (Umbralización pura)
    // --------------------------------------------------------
    public static void aplicarBlancoYNegro(File entrada, File carpetaSalida) {
        try {
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();
            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int p = original.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    // Calculamos el brillo general
                    double brillo = (0.2126 * r) + (0.7152 * g) + (0.0722 * b);

                    // Si el brillo es mayor a la mitad (127), es blanco (255). Si no, negro (0).
                    int colorFinal = (brillo > 127) ? 255 : 0;

                    int nuevoPixel = (a << 24) | (colorFinal << 16) | (colorFinal << 8) | colorFinal;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }
            ImageIO.write(resultado, "png", new File(carpetaSalida, "5_blanco_negro.png"));
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // --------------------------------------------------------
    // METODO 6: EFECTO ESCALA DE GRISES A 'N' NIVELES
    // --------------------------------------------------------
    public static void aplicarEscalaDeGrisesN(File entrada, File carpetaSalida, int N) {
        try {
            BufferedImage original = ImageIO.read(entrada);
            int ancho = original.getWidth();
            int alto = original.getHeight();
            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int p = original.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    // 1. Primero convertimos a gris real
                    int grisReal = (int) ((0.2126 * r) + (0.7152 * g) + (0.0722 * b));

                    // 2. Luego cuantizamos ese gris a N niveles
                    int grisCuantizado = (int)(Math.round((grisReal / 255.0) * (N - 1)) * (255.0 / (N - 1)));

                    // 3. Asignamos el mismo gris a R, G y B
                    int nuevoPixel = (a << 24) | (grisCuantizado << 16) | (grisCuantizado << 8) | grisCuantizado;
                    resultado.setRGB(x, y, nuevoPixel);
                }
            }
            ImageIO.write(resultado, "png", new File(carpetaSalida, "6_grises_N" + N + ".png"));
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }
}