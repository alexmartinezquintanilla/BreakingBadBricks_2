/**
 * BreakingBricks
 *
 * Personaje para juego creado en Examen
 *
 * @author José Elí Santiago Rodríguez A07025007, Alex Martinez Quintanilla
 * A00810480
 * @version 1.01 2014/09/17
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
import java.net.URL;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BreakingBricks extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del JFrame y este no parpadee */
    // Imagen a proyectar en el JFrame
    private Image imaImagenJFrame;
    // Objeto grafico de la Imagen
    private Graphics graGraficaJFrame;
    //Imagen de fondo
    private URL urlImagenBackG = this.getClass().getResource("fondo.png");
    //Contador de vidas
    private int iVidas;
    //Objeto de la clase personaje. El Bate (slider de brick breaker)
    private Personaje perCrowbar;
    //vida y score como personajes por si se implementa colision
    private Personaje perScoreVidas;
    //Objeto personaje mosca aliada
    private Personaje perMosca;
    //Dirección en la que se mueve el crowbar
    private int iDireccionCrowbar;
    //Dirección de la mosca
    private int iDireccionMosca;
    //Linked List para las charolas
    private LinkedList lnkCharolas;
    //Linked list para la pelota (temporal?)
    private LinkedList lnkProyectiles;
    //Contador de puntos
    private int iScore;
    private String[] sarrLevels = new String[3];
    //Contador para las veces que el crowbar ha sido colisionado (a cambiar
    //por número de veces que la charola ha sido colisionada?
    // Objeto SoundClip cuando la pelota colisiona con el crowbar o la pared
    private SoundClip scSonidoColisionPelota;
    //Objeto SoundClip para cuando la charola es golpeada la primera vez
    private SoundClip scSonidoColisionCharolaGolpeada;
    //Objeto SoundClip para cuando la charola es destruida 
    private SoundClip scSonidoColisionCharolaRota;
    //Objeto SoundClip para la música de fondo
    private SoundClip scSonidoBGM;
    //Objeto Soundclip para cuando la mosca está en la derecha
    private SoundClip scSonidoMoscaD;
    //Objeto SoundClip para cuando la mosca está en la izquierda
    private SoundClip scSonidoMoscaI;
    //Boleano para pausar el juego.
    private boolean bPausado;
    //URL para cargar imagen de la mosca
    private URL urlImagenMosca = this.getClass().getResource("Mosca/mosco.gif");
    //URL para cargar imagen de la charola
    private URL urlImagenCharola
            = this.getClass().getResource("Charola/charola.png");
    //URL para cargar imagen de la charola golpeada
    private URL urlImagenCharolaGolpeada
            = this.getClass().getResource("Charola/charolagolpeada.png");
    //URL para cargar imagen de la charola rota
    private URL urlImagenCharolaRota
            = this.getClass().getResource("Charola/charolarota.gif");
    //URL para cargar la imagen de la pelota
    private URL urlImagenPelota
            = this.getClass().getResource("proyectil.gif");
    //URL para cargar la imagen de pausa
    private URL urlImagenPausa = this.getClass().getResource("pause.png");
    //Imagen al pausar el juego.
    private URL urlImagenScoreVidas = this.getClass().getResource("scorevidas.png");
    //imagen para el score y las vidas
    private Image imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
    private URL urlImagenInicio = this.getClass().getResource("pantallaInicio.png");
    //Imagen al pausar el juego.
    private Image imaImagenInicio = Toolkit.getDefaultToolkit().getImage(urlImagenInicio);
    //subo la imagen del bg
    private URL urlImagenGOver = this.getClass().getResource("game_over.jpg");
    // creo imagen para el background
    private Image imaImagenGOver = Toolkit.getDefaultToolkit().getImage(urlImagenGOver);
    //X del proyectil
    private int iMovX;
    //Y del proyectil
    private int iMovY;
    //Booleana para saber si el juego comenzó o no
    private boolean bGameStarted;
    //Ubicación de la mosca para sonido estereo: True = Der. False = Izq
    private boolean bUbicacionMosca;
    private boolean bGameOver;
    private boolean bBallFell;
    private int iLvl;
    private boolean bLevelCleared;
    private boolean bGameWon;

    //Constructor de BreakingBricks
    public BreakingBricks() {
        init();
        start();
    }

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>JFrame</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el JFRAME de un tamaño 800,700
        setSize(800, 700);

        // introducir instrucciones para iniciar juego
        bPausado = false; //Booleana para pausar

        //Inicializamos las vidas al azar entre 3 y 5
        iVidas = (int) (Math.random() * (6 - 3) + 3);

        //Inicializamos el score en 0
        iScore = 0;

        //inicializamos el movimiento del proyectil en X en -1
        iMovX = 4;

        //inicializamos el movimiento del proyectil en Y en -1
        iMovY = 4;

        //inicializamos la variable que checa si ya empezó el juego en falso.
        bGameStarted = false;

        //inicializamos la variable que checa si ya empezó el juego en falso.
        bGameOver = false;

        //inicializamos la variable que checa si la pelota se salió del juego.
        bBallFell = true;

        //Inicializamos el nivel como no completado
        bLevelCleared = false;

        //Inicializamos el juego como no ganado
        bGameWon = false;

        //Inicializamos el arreglo de niveles.
        sarrLevels[0] = "lvl1.txt";
        sarrLevels[1] = "lvl2.txt";
        sarrLevels[2] = "lvl2.txt";

        //inicializamos el lvl en 0
        iLvl = 0;

        // se crea imagen del crowbar
        URL urlImagenCrowbar = this.getClass().getResource("crowbar.png");

        //se crea la imagen de la msoca
        URL urlImagenMosca = this.getClass().getResource("Mosca/mosco.gif");

        //se crea el personaje mosca
        perMosca = new Personaje((int) (Math.random() * (this.getWidth() - 1) + 1), (int) (Math.random() * (this.getHeight() - 1) + 1),
                Toolkit.getDefaultToolkit().getImage(urlImagenMosca));
        perMosca.setVelocidad(7);
        if (perMosca.getX() > this.getWidth() / 2) {
            bUbicacionMosca = true;
        } else {
            bUbicacionMosca = false;
        }

        // se crea el crowbar
        perCrowbar = new Personaje(getWidth() / 2, getHeight() / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenCrowbar));
        //Se inicializa con velocidad 7
        perCrowbar.setVelocidad(7);

        perScoreVidas = new Personaje(40, 450,
                Toolkit.getDefaultToolkit().getImage(urlImagenScoreVidas));

        // se posiciona al crowbar en el centro de la pantalla y en la parte inferior
        perCrowbar.setX((getWidth() / 2) - (perCrowbar.getAncho() / 2));
        perCrowbar.setY(getHeight() - perCrowbar.getAlto() - 20);

        // se posiciona a Susy en alguna parte al azar del cuadrante 
        // superior izquierdo
        int posX;
        int posY;

        lnkCharolas = new LinkedList();

        //en este for se crean de 8 a 10 caminadores y se guardan en la lista de
        //caminadores
        int iAzar = (int) (Math.random() * (11 - 8) + 8);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = 0;
            posY = (int) (Math.random() * getHeight());
            // se crea el personaje caminador
            Personaje perCharola;
            perCharola = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenCharola));
            perCharola.setX(0 - perCharola.getAncho());
            perCharola.setY((int) (Math.random() * (getHeight()
                    - perCharola.getAlto())));
            perCharola.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCharolas.add(perCharola);
        }

        lnkProyectiles = new LinkedList();

        //imagen de las vidas y el score como personaje
        //se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        iAzar = (int) (Math.random() * (16 - 10) + 10);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = (int) (Math.random() * getHeight());
            posY = 0;
            // se crea el personaje caminador
            Personaje perProyectil;
            perProyectil = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
            perProyectil.setX((int) (Math.random() * (getWidth()
                    - perProyectil.getAncho())));
            perProyectil.setY(-perProyectil.getAlto()
                    - ((int) (Math.random() * getWidth())));
            lnkProyectiles.add(perProyectil);
        }

        //creo el sonido del crowbar golpeando la pelota
        scSonidoColisionPelota = new SoundClip("tapa.wav");
        //creo el sonido  de la charola golpeada la primera vez
        scSonidoColisionCharolaGolpeada = new SoundClip("Charola/charolagolpeada.wav");
        //creo el sonido de la charola rompiéndose
        scSonidoColisionCharolaRota = new SoundClip("Charola/charolarota.wav");
        //Creo el sonido para la música de fondo; lo loopeo y lo inicio.
        scSonidoBGM = new SoundClip("BGM.wav");
        scSonidoBGM.setLooping(true);
        scSonidoBGM.play();
        //Creo el sonido para cuando la mosca estará en la derecha y loopeo
        scSonidoMoscaD = new SoundClip("Mosca/moscaderecha.wav");
        scSonidoMoscaD.setLooping(true);
        if (bUbicacionMosca) {
            scSonidoMoscaD.play();
        }
        //Creo el sonido para cuando la mosca estará en la izquierda y loopeo
        scSonidoMoscaI = new SoundClip("Mosca/moscaizquierda.wav");
        scSonidoMoscaI.setLooping(!bUbicacionMosca);
        if (!bUbicacionMosca) {
            scSonidoMoscaI.play();
        }

        //Inicializo la dirección de la mosca como 0
        iDireccionMosca = 0;
        addKeyListener(this);
    }

    /**
     * start
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>JFrame</code>
     *
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * run
     *
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones de
     * nuestro juego.
     *
     */
    public void run() {
        // se realiza el ciclo del juego en este caso nunca termina
        while (true) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
             se checa si hubo colisiones para desaparecer jugadores o corregir
             movimientos y se vuelve a pintar todo
             */
            if (!bPausado && bGameStarted && !bGameOver) {
                actualiza();
                checaColision();
                repaint();
            }
            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego "
                        + iexError.toString());
            }
        }
    }

    /**
     * actualiza
     *
     * Metodo que actualiza la posicion del objeto elefante
     *
     */
    public void actualiza() {
        // instrucciones para actualizar personajes
        iDireccionMosca = ((int) (Math.random() * (5 - 1) + 1));
        switch (iDireccionMosca) {

            case 1: {

                perMosca.setY(perMosca.getY() - perMosca.getVelocidad());
                break; //se mueve hacia arriba
            }
            case 2: {

                perMosca.setY(perMosca.getY() + perMosca.getVelocidad());
                break; //se mueve hacia abajo
            }
            case 3: {

                perMosca.setX(perMosca.getX() - perMosca.getVelocidad());
                break; //se mueve hacia la izquierda
            }
            case 4: {

                perMosca.setX(perMosca.getX() + perMosca.getVelocidad());
                break; //se mueve hacia la derecha
            }
        }
        //Detecta dónde está la mosca para ver qué clip correr
        if (perMosca.getX() > this.getWidth() / 2) {
            bUbicacionMosca = true;

        } else {
            bUbicacionMosca = false;

        }

        //Nena actualiza movimiento dependiendo de la tecla que se presionó
        switch (iDireccionCrowbar) {
            case 3:
                perCrowbar.derecha();
                break;
            case 4:
                perCrowbar.izquierda();
                break;
            case 5:
                perCrowbar.setX(perCrowbar.getX());
                break;

        }
        //Actualiza el movimiento de los caminadores
        for (Object lnkCharola : lnkCharolas) {
            Personaje perCharola = (Personaje) lnkCharola;
            if (perCharola.getGolpes() > 1) {
                perCharola.setVelocidad(perCharola.getVelocidad() + 1);
                perCharola.abajo();
            }
        }
        //Actualiza el movimiento de los corredores
        for (Object lnkProyectil : lnkProyectiles) {
            Personaje perProyectil = (Personaje) lnkProyectil;
            if (bBallFell) {
                perProyectil.setX(perCrowbar.getX() + perCrowbar.getAncho() / 2 - perProyectil.getAncho() / 2);
                perProyectil.setY(perCrowbar.getY() - 30);
            } else {
                perProyectil.setX(perProyectil.getX() + iMovX);
                perProyectil.setY(perProyectil.getY() + iMovY);
            }
        }
        if (iVidas < 1) {
            bGameOver = true;
        }
        if (bLevelCleared) {
            iLvl++;
            bLevelCleared = false;
            bGameStarted = true;
            bGameOver = false;
            if (iLvl > 3) {
                bGameWon = true;
            } else {
                try {
                    leeArchivo(); //Carga datos
                } catch (IOException ex) {
                    Logger.getLogger(BreakingBricks.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            }
        }
    }

    /**
     * checaColision
     *
     * Metodo usado para checar la colision del objeto elefante con las orillas
     * del <code>JFrame</code>.
     *
     */
    public void checaColision() {
        if ((perCrowbar.getX() + perCrowbar.getAncho()) >= getWidth()) {
            perCrowbar.setX(getWidth() - perCrowbar.getAncho());
        }
        if (perCrowbar.getX() <= 0) {
            perCrowbar.setX(0);
        }

        for (int iIterator = 0; iIterator < lnkCharolas.size(); iIterator++) {
            Personaje perCharola = (Personaje) lnkCharolas.get(iIterator);
            if (perCharola.getY() >= getHeight()) {
                lnkCharolas.remove(perCharola);
            }
        }

        if (lnkCharolas.size() == 0) {
            bLevelCleared = true;
        }

        //Checa colisiones de la pelota con paredes y con charolas
        for (Object lnkProyectil : lnkProyectiles) {
            Personaje perProyectil = (Personaje) lnkProyectil;
            if (perProyectil.getX() + perProyectil.getAncho() >= getWidth() || perProyectil.getX() <= 0) {
                iMovX = -iMovX;
                scSonidoColisionPelota.play();
            }
            if (perProyectil.getY() > perCrowbar.getY()) {
                iMovX = -iMovX;
                scSonidoColisionPelota.play();
            }
            if ((perCrowbar.colisionaDerecha(perProyectil) && iMovX <= 0)) {
                perProyectil.setY(perCrowbar.getY() - perProyectil.getAlto());
                iMovY = -iMovY;
                iMovX = 1;
                scSonidoColisionPelota.play();
            } else if ((perCrowbar.colisionaDerecha(perProyectil) && iMovX >= 0)) {
                perProyectil.setY(perCrowbar.getY() - perProyectil.getAlto());
                iMovY = -iMovY;
                iMovX += 4;
                scSonidoColisionPelota.play();
            }
            if ((perCrowbar.colisionaIzquierda(perProyectil) && iMovX >= 0)) {
                perProyectil.setY(perCrowbar.getY() - perProyectil.getAlto());
                iMovY = -iMovY;
                iMovX = -1;
            } else if ((perCrowbar.colisionaIzquierda(perProyectil) && iMovX <= 0)) {
                perProyectil.setY(perCrowbar.getY() - perProyectil.getAlto());
                iMovY = -iMovY;
                iMovX += -4;
                scSonidoColisionPelota.play();
            }
            if ((perCrowbar.colisionaEnMedio(perProyectil))) {
                iMovY = -iMovY;
                iMovX = 0;
                scSonidoColisionPelota.play();
            }

            if (perProyectil.getY() <= 0) {
                iMovY = -iMovY;
                scSonidoColisionPelota.play();
            }
            for (Object lnkCharola : lnkCharolas) {
                Personaje perCharola = (Personaje) lnkCharola;
                if (perProyectil.colisiona(perCharola) && !perCharola.getDead()) {
                    if (urlImagenCharolaRota != null && (perCharola.getGolpes() > 1)) {
                        iMovY = -iMovY;
                        perCharola.setDead(true);
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaRota));
                        scSonidoColisionCharolaRota.play();
                        iScore += 10;
                    } else {
                        iMovY = -iMovY;
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaGolpeada));
                        perCharola.setGolpes(perCharola.getGolpes() + 1);
                        scSonidoColisionCharolaGolpeada.play();
                        iScore++;
                    }
                }
            }
            //Colisiones de la mosca con las charolas
            for (Object lnkCharola : lnkCharolas) {
                Personaje perCharola = (Personaje) lnkCharola;
                if (perMosca.colisiona(perCharola) && !perCharola.getDead()) {
                    if (urlImagenCharolaRota != null && (perCharola.getGolpes() > 1)) {
                        perCharola.setDead(true);
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaRota));
                        scSonidoColisionCharolaRota.play();
                    } else {
                        perCharola.setImagen(Toolkit.getDefaultToolkit().getImage(urlImagenCharolaGolpeada));
                        perCharola.setGolpes(perCharola.getGolpes() + 1);
                        scSonidoColisionCharolaGolpeada.play();
                    }

                    scSonidoColisionCharolaGolpeada.play();
                }
            }
            //Colisiones de la mosca con las orillas

            if (perMosca.getX() < 0) {
                perMosca.setX(10);
            }
            if (perMosca.getX() > this.getWidth() - perMosca.getAncho()) {
                perMosca.setX(this.getWidth() - perMosca.getAncho());
            }
            if (perMosca.getY() < 0) {
                perMosca.setY(2);
            }
            if (perMosca.getY() > this.getHeight() - perMosca.getAlto()) {
                perMosca.setY(this.getHeight() - perMosca.getAlto());
            }

            //Si la pelota se sale del juego por el fondo pierde una vida y reinicia en la barreta
            if (perProyectil.getY() + perProyectil.getAlto() >= getHeight()) {
                iVidas += -1;
                bBallFell = true;
                iMovY = -iMovY;
                perProyectil.setX(perCrowbar.getX() + perCrowbar.getAncho() / 2 - perProyectil.getAncho() / 2);
                perProyectil.setY(perCrowbar.getY() - 30);
                scSonidoColisionPelota.play();
            }
        }
    }

    /**
     * update
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>, heredado de la clase
     * Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y define cuando
     * usar ahora el paint
     *
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenJFrame == null) {
            imaImagenJFrame = createImage(this.getSize().width,
                    this.getSize().height);
            graGraficaJFrame = imaImagenJFrame.getGraphics();
        }
        // creo imagen para el background
        Image imaImagenBackG
                = Toolkit.getDefaultToolkit().getImage(urlImagenBackG);
        // Despliego la imagen
        graGraficaJFrame.drawImage(imaImagenBackG, 0, 0,
                getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaJFrame.setColor(getForeground());
        paint1(graGraficaJFrame);

        // Dibuja la imagen actualizada
        graGrafico.drawImage(imaImagenJFrame, 0, 0, this);
    }

    /**
     * paint1
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>, heredado de la clase
     * Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint1(Graphics g) {
        if (perCrowbar != null & lnkCharolas != null & lnkProyectiles != null) {

            //Dibuja la imagen de Nena en la posicion actualizada
            g.drawImage(perCrowbar.getImagen(), perCrowbar.getX(),
                    perCrowbar.getY(), this);

            //Dibuja a la mosca en la posición actualizada
            //Dibuja la imagen de Nena en la posicion actualizada
            g.drawImage(perMosca.getImagen(), perMosca.getX(),
                    perMosca.getY(), this);

            //Dibuja la imagen de los caminadores en la posicion actualizada
            for (Object lnkCharola : lnkCharolas) {
                Personaje perCharola = (Personaje) lnkCharola;
                g.drawImage(perCharola.getImagen(), perCharola.getX(),
                        perCharola.getY(), this);
            }

            //Dibuja la imagen de los corredores en la posicion actualizada
            for (Object lnkProyectil : lnkProyectiles) {
                Personaje perProyectil = (Personaje) lnkProyectil;
                g.drawImage(perProyectil.getImagen(), perProyectil.getX(),
                        perProyectil.getY(), this);
            }

            //dibuja la imagen del score y las vidas
            g.drawImage(perScoreVidas.getImagen(), perScoreVidas.getX(),
                    perScoreVidas.getY(), this);
            //Despliega las vidas restantes y el score
            g.setColor(Color.GREEN);
            Font fFont = new Font("Verdana", Font.BOLD, 18);
            g.setFont(fFont);
            g.drawString(" : " + iScore, 160, 495);
            g.drawString(" : " + iVidas, 200, 535);

        }
        if (bPausado) {
            g.drawImage(imaImagenPausa, (((getWidth() / 2))
                    - imaImagenPausa.getWidth(this) / 2), (((getHeight() / 2))
                    - imaImagenPausa.getHeight(this) / 2), this);

        }
        if (!bGameStarted) {
            g.drawImage(imaImagenInicio, (((getWidth() / 2))
                    - imaImagenInicio.getWidth(this) / 2), (((getHeight() / 2))
                    - imaImagenInicio.getHeight(this) / 2), this);

        }
        if (bGameOver) {
            g.drawImage(imaImagenGOver, (((getWidth() / 2))
                    - imaImagenGOver.getWidth(this) / 2), (((getHeight() / 2))
                    - imaImagenGOver.getHeight(this) / 2), this);
            g.drawImage(perScoreVidas.getImagen(), perScoreVidas.getX(),
                    perScoreVidas.getY(), this);
            g.drawString(" : " + iScore, 160, 495);
            g.drawString(" : " + iVidas, 200, 535);
        }
    }

    public void grabaArchivo() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter("datos.txt"));

        fileOut.println(lnkProyectiles.size());
        for (Object lnkCorre1 : lnkProyectiles) {
            Personaje perCorre = (Personaje) lnkCorre1;
            fileOut.println(perCorre.getX());
            fileOut.println(perCorre.getY());
            fileOut.println(perCorre.getVelocidad());
        }
        fileOut.println(lnkCharolas.size());
        for (Object lnkCamina1 : lnkCharolas) {
            Personaje perCamina = (Personaje) lnkCamina1;
            fileOut.println(perCamina.getX());
            fileOut.println(perCamina.getY());
            fileOut.println(perCamina.getVelocidad());
        }

        fileOut.println(perCrowbar.getX());
        fileOut.println(perCrowbar.getY());
        fileOut.println(iDireccionCrowbar);
        fileOut.println(iScore);
        fileOut.println(iVidas);

        fileOut.close();

    }

    public void leeArchivo() throws IOException {
        lnkProyectiles.clear();
        lnkCharolas.clear();
        BufferedReader fileIn;
        boolean bNoFileFound;
        try {
            fileIn = new BufferedReader(new FileReader(sarrLevels[iLvl]));
            bNoFileFound = false;
        } catch (FileNotFoundException e) {
            bNoFileFound = true;
            init();
        }
        if (!bNoFileFound) {
            fileIn = new BufferedReader(new FileReader(sarrLevels[iLvl]));
            String dato = fileIn.readLine();
            while (dato != null) {
                int iCorredores = Integer.parseInt(dato);
                for (int iK = 1; iK <= iCorredores; iK++) {
                    // se crea el personaje caminador
                    Personaje perCorre;
                    perCorre = new Personaje(0, 0,
                            Toolkit.getDefaultToolkit().
                            getImage(urlImagenPelota));
                    perCorre.setX(Integer.parseInt(fileIn.readLine()));
                    perCorre.setY(Integer.parseInt(fileIn.readLine()));
                    perCorre.setVelocidad(Integer.parseInt(fileIn.readLine()));
                    lnkProyectiles.add(perCorre);
                }

                int iCaminadores = Integer.parseInt(fileIn.readLine());
                for (int iK = 1; iK <= iCaminadores; iK++) {
                    // se crea el personaje caminador
                    Personaje perCamina;
                    perCamina = new Personaje(0, 0,
                            Toolkit.getDefaultToolkit().
                            getImage(urlImagenCharola));
                    perCamina.setX(Integer.parseInt(fileIn.readLine()));
                    perCamina.setY(Integer.parseInt(fileIn.readLine()));
                    perCamina.setVelocidad(Integer.parseInt(fileIn.readLine()));
                    lnkCharolas.add(perCamina);
                }

                perCrowbar.setX(Integer.parseInt(fileIn.readLine()));
                perCrowbar.setY(Integer.parseInt(fileIn.readLine()));
                iDireccionCrowbar = (Integer.parseInt(fileIn.readLine()));
                iScore = (Integer.parseInt(fileIn.readLine()));
                iVidas = (Integer.parseInt(fileIn.readLine()));
            }
            fileIn.close();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        //To change body of generated methods, choose Tools | Templates.
        // si presiono flecha para Derecha
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDireccionCrowbar = 3;  // cambio la dirección hacia derecha
        }
        // si presiono flecha para Izquierda
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            iDireccionCrowbar = 4;   // cambio la dirección hacia izq
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
//        // si presiono flecha para abajo
//        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
//            iDireccionCrowbar = 1;  // cambio la dirección hacia abajo
//        }
        // si presiono Space bar para arriba
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            if (bGameStarted) {
                bBallFell = false;   // cambio la dirección hacia arriba
            }
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDireccionCrowbar = 5;  // cambio la dirección hacia derecha
        }
        // si presiono flecha para Izquierda
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            iDireccionCrowbar = 5;   // cambio la dirección hacia izq
        }
        //Si presionan P booleano Pausa cambia de estado.
        if (keyEvent.getKeyCode() == KeyEvent.VK_P) {

            bPausado = !bPausado; //Click a P pausa o despausa el juego
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_C) {

            bGameStarted = true;
            bGameOver = false;

            try {
                leeArchivo(); //Carga datos
            } catch (IOException ex) {
                Logger.getLogger(BreakingBricks.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_G) {
            //Guarda datos
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(BreakingBricks.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
}
