package com.programacion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Imagen {
    public static void main(String[] args) {
        File file = new File("src/main/resources/assets/img1.jpg");
        File file2 = new File("src/main/resources/assets/img1-Nuevo.jpg");

        int ancho;
        int alto;
        int pixel;
        int pixelNuevo;

        int r;
        int g;
        int b;

        int mascara = 0xFF;

        Random random = new Random();

        int binario = (7 >> 2);

        try {
            ancho = 900;
            alto = 500;

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    r = random.nextInt(256);
                    g = random.nextInt(256);
                    b = 250;

                    //System.out.println("(" + r + ", " + g + ", " + b + ")");

                    pixelNuevo = (r << 16) | (g << 8) | (b << 0);
                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            ImageIO.write(bufer2, "jpg", file2);
            System.out.println("Imagen guardada");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }
}
