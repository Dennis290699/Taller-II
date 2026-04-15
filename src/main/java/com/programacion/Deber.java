package com.programacion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Deber {

    public static void main(String[] args) {

        File carpeta = new File("src/main/resources/assets");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        int ancho = 900;
        int alto = 500;

        try {

            // 1. Izquierda a derecha
            // El color cambia basándose en el eje X (de 0 a ancho)
            BufferedImage img1 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int c = (int) (((float) x / ancho) * 255);
                    int pixel = (c << 16) | (c << 8) | 250;
                    img1.setRGB(x, y, pixel);
                }
            }
            ImageIO.write(img1, "jpg", new File(carpeta, "izquierda-derecha.jpg"));
            System.out.println("Imagen izquierda a derecha guardada");


            // 2. Derecha a izquierda
            // Invertimos la fórmula en X para que el inicio oscuro esté en la derecha
            BufferedImage img2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < alto; y++) {
                for (int x = ancho - 1; x >= 0; x--) {
                    int c = (int) (((float) (ancho - 1 - x) / ancho) * 255);
                    int pixel = (c << 16) | (c << 8) | 250;
                    img2.setRGB(x, y, pixel);
                }
            }
            ImageIO.write(img2, "jpg", new File(carpeta, "derecha-izquierda.jpg"));
            System.out.println("Imagen derecha a izquierda guardada");


            // 3. Arriba hacia abajo
            // El color cambia basándose en el eje Y (de 0 a alto)
            BufferedImage img3 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int c = (int) (((float) y / alto) * 255);
                    int pixel = (c << 16) | (c << 8) | 250;
                    img3.setRGB(x, y, pixel);
                }
            }
            ImageIO.write(img3, "jpg", new File(carpeta, "arriba-abajo.jpg"));
            System.out.println("Imagen arriba a abajo guardada");


            // 4. Abajo hacia arriba
            // Invertimos la fórmula en Y para que el inicio oscuro esté abajo
            BufferedImage img4 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            for (int y = alto - 1; y >= 0; y--) {
                for (int x = 0; x < ancho; x++) {
                    int c = (int) (((float) (alto - 1 - y) / alto) * 255);
                    int pixel = (c << 16) | (c << 8) | 250;
                    img4.setRGB(x, y, pixel);
                }
            }
            ImageIO.write(img4, "jpg", new File(carpeta, "abajo-arriba.jpg"));
            System.out.println("Imagen abajo a arriba guardada");


            // 5. Desde el centro hacia las esquinas
            // Solución: Calculamos la distancia de cada píxel al centro.
            // Así evitamos pintar un cuadrado encima de otro miles de veces.
            BufferedImage img5 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            int centroX = ancho / 2;
            int centroY = alto / 2;
            int radioMaximo = Math.max(centroX, centroY);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    // Distancia desde el píxel actual hasta el centro
                    int distX = Math.abs(x - centroX);
                    int distY = Math.abs(y - centroY);

                    // Usamos la distancia mayor para crear el efecto de cuadrados concéntricos
                    int radio = Math.max(distX, distY);

                    int c = (int) (((float) radio / radioMaximo) * 255);
                    if (c > 255) c = 255; // Por seguridad, para no pasarnos del límite del color

                    int pixel = (c << 16) | (c << 8) | 250;
                    img5.setRGB(x, y, pixel);
                }
            }
            ImageIO.write(img5, "jpg", new File(carpeta, "centro-esquinas.jpg"));
            System.out.println("Imagen centro a esquinas guardada");

            System.out.println("Todas las imagenes fueron generadas correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}