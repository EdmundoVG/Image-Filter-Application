package app_filtroslogicos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Filtros extends JPanel implements Filtro {
    
    //Variables
    private int w, h, opcion;
    private String ruta;
    private BufferedImage imgOriginal, imagen;
    private int nivel;
    private int canalR;
    private int canalG;
    private int canalB;
    
    //Constructor
    
    Filtros(JPanel panel){
        this.w = panel.getWidth();
        this.h = panel.getHeight();
        this.setSize(w, h);
    }
    
    //Setter
    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setCanalR(int canalR) {
        this.canalR = canalR;
    }

    public void setCanalG(int canalG) {
        this.canalG = canalG;
    }

    public void setCanalB(int canalB) {
        this.canalB = canalB;
    }
    
    public BufferedImage getImagenOriginal(){
        BufferedImage imagen = null;
        try{
            imagen = ImageIO.read(new File(ruta));
        }catch(IOException ex){
            try{
                imagen = ImageIO.read(new URL(ruta));
            } catch (IOException ex2) {
                JOptionPane.showMessageDialog(this, "Archivo de la imagen no se puede abrir");
            }
        }
        return imagen;
    }

    @Override
    public void paint(Graphics g) {
        switch (opcion){
            case 0:
                imgOriginal = getImagenOriginal();
                g.drawImage(imgOriginal, 0, 0, h, w, this);
            break;
            
            case 7:
                imgOriginal = getImagenOriginal();
                
                for(int i = 0; i < imgOriginal.getWidth(); i++){
                    for(int j = 0; j < imgOriginal.getHeight(); j++){
                        
                        int pixelRGB = imgOriginal.getRGB(i, j);
                        //Retorna dato en ARGB, int de 32 bits
                        
                        //Ejemplo = Opaque Black = ARGB(255, 0, 0, 0) = 0xFF000000 = -16777216
                        
                        System.out.println("RGBPi " + pixelRGB);
                        //Utilizo el operador bitwise AND y shift-right para extraer el valor rojo del pixel
                        
                        /*Explicacion = todos los bytes se ponen en cero excepto aquellos que contengan el componente rojo, despues
                          se desplazan (>>) 16 lugares a la derecha esto para extraer el valor del rojo, osea que se mueva al
                          los bytes mas a la derecha
                        
                            Rojo = 0x00ff0000 -(16 lugares >>16 )-> 0x000000RR (este es un numero de entre 0 a 255)
                            Verde = 0x0000ff00 -(8 lugares >>8)-> 0x000000RR (este es un numero de entre 0 a 255)
                            Azul = 0x000000ff
                        */
                        
                        int red = (pixelRGB & 0x00ff0000) >> 16;
                        System.out.println("red " + red);
                        
                        int  green = (pixelRGB & 0x0000ff00) >> 8;
                        System.out.println("green " + green);
                        
                        int  blue = pixelRGB & 0x000000ff;
                        System.out.println("blue " + blue);
                        
                        //Union de R, G y B en un solo numero
                        float unionRGB = ((float)red / ((green + blue) / 2));
                        System.out.println("unionRGB " + unionRGB);
                        
                        //Digito que se compara para saber si unionRGB eesta dentro del espectro del color rojo
                        double comprobadorRojo = 1.8;
                        
                        if (unionRGB > comprobadorRojo) {
                            Color color = new Color( 0, 255, 255);
                            imgOriginal.setRGB(i, j, color.getRGB());
                            g.drawImage(imgOriginal, 0, 0, w, h, this);
                        }
                        
                        
                        
                        /*
                        Color color = new Color(pixelRGB, true);
                        
                        int valorRojo = color.getRed();
                        int valorVerde = color.getGreen(); 
                        int valorAzul = color.getBlue();
                        
                        //color.getRed() > 210 && color.getBlue() < 150 && color.getGreen() < 150
                        
                        System.out.println("RGB " + color.getRGB());
                       
                        System.out.println("Red " + color.getRed());
                        
                        if(color.getRGB() > -1108224 && color.getRGB() < 150 && color.getGreen() < 150){
                            
                            System.out.println("detecto rojo");
                            int nuevoRojo = valorRojo - valorRojo;
                            imgOriginal.setRGB(i, j, new Color(nuevoRojo , valorVerde, valorAzul).getRGB());
                            g.drawImage(imgOriginal, 0, 0, w, h, this);
                        }
                         */
                        
                    }
                    System.out.println("Nada ");
                }
                
            break;
            
            case 8:
                g.drawImage(imgOriginal, 0, 0, w, h, this);
            break;
            
            default:
                imagen = Filtro(imgOriginal, canalR, canalG, canalB);
                g.drawImage(imagen, 0, 0, w, h, this);
        }
    }
    
    @Override
    public Color colorSalida(Color entrada) {
        int r = entrada.getRed();
        int g = entrada.getGreen();
        int b = entrada.getBlue();
        int a = entrada.getAlpha();
        switch(opcion){
            case 1:
                int gris = (r + g + b) / 3;
                return new Color(gris, gris, gris, a);
            case 2:
                return new Color( 255 - r, 255 - g, 255 - b, a);
            case 3:
                int prom = (int)(( r + g + b ) / 3 );
                if(prom < nivel)
                    return Color.BLACK;
                else
                    return Color.WHITE;
            case 4:
                return new Color( r - ( r % nivel), g - ( g % nivel), b - (b % nivel), a);
            case 5:
                double tr = 0.393 * r + 0.769 * g + 0.189 * b;
                double tg = 0.349 * r + 0.686 * g + 0.168 * b;
                double tb = 0.272 * r + 0.534 * g + 0.131 * b;
                
                if( tr > 255 ){
                    r = 255;
                }else {
                    r = (int) tr;
                }
                if( tg > 255 ){
                    g = 255;
                }else {
                    g = (int) tg;
                }
                if( tb > 255 ){
                    b = 255;
                }else {
                    b = (int) tb;
                }
                return new Color(r, g, b, a);
            case 6:
                return Color.BLACK;
            
            default:
                return new Color(r, g, b, a);
        }
    }
    
}
