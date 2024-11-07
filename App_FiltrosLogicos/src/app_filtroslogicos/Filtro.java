package app_filtroslogicos;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface Filtro {
    
    public Color colorSalida(Color entrada);
    
    default Color transicion(Color entrada, double tr, double tg, double tb){
        Color salida = colorSalida(entrada);
        return new Color(lerpChanel(entrada.getRed(), salida.getRed(),tr), lerpChanel(entrada.getGreen(), salida.getGreen(), tg), lerpChanel(entrada.getBlue(), salida.getBlue(), tb), entrada.getAlpha());
    }
    
    default int lerpChanel(double n1, double n2, double t){
        return ( int )(( n2 - n1 ) * t + n1 );
    }
    
    default public BufferedImage Filtro(BufferedImage img, double tr, double tg, double tb){
        BufferedImage imagen = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        System.out.println("Ancho " + img.getWidth());
        System.out.println("Altura " + img.getHeight());
        for(int c = 0; c < img.getWidth(); c++){
            for(int f = 0; f < img.getHeight(); f++){
                Color entrada = new Color(img.getRGB(c, f), true);
                Color salida = transicion(entrada, tr, tg, tb);
                imagen.setRGB(c, f, salida.getRGB());
            }
        }
        return imagen;
    }
     
}
