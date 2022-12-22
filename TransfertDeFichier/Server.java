package server;

import java.awt.Component;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseListener;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import server.Fichier;

public class Server {
    static ArrayList<Fichier> lesFichiers = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        
        int id =0;

        JFrame fenetre = new JFrame("fichier recu par le client");
        fenetre.setSize( 500,500);
        fenetre.setLayout(new BoxLayout(fenetre.getContentPane(), BoxLayout.Y_AXIS));
        fenetre.setDefaultCloseOperation(fenetre.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel fichier = new JLabel("Voici les fichiers recues :");
        fichier.setBorder(new EmptyBorder(20,0,10,0));
        fichier.setAlignmentX(Component.CENTER_ALIGNMENT);

        fenetre.add(fichier);
        fenetre.add(panel);
        fenetre.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(9999);
        
        while(true){
            try {
                
                Socket socket = serverSocket.accept();

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int fileNameLength = dataInputStream.readInt();

                if (fileNameLength>0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength>0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes,0,fileContentLength);
 
                        JPanel lignes = new JPanel();
                        lignes.setLayout(new BoxLayout(lignes, BoxLayout.Y_AXIS));

                        JLabel nomFichier = new JLabel(fileName);
                        nomFichier.setBorder(new EmptyBorder(10,0,10,0));

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            lignes.setName(String.valueOf(id));
                            lignes.addMouseListener(getMyMouseListener());

                            lignes.add(nomFichier);
                            panel.add(lignes);
                            fenetre.validate();
                        }
                        else{
                            lignes.setName(String.valueOf(id));
                            lignes.addMouseListener(getMyMouseListener());
                            lignes.add(nomFichier);
                            panel.add(lignes);

                            fenetre.validate();
                        }
                        lesFichiers.add(new Fichier(id, fileName, fileContentBytes, getFileExtension(fileName)));
                    }   
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        }


    }

    public static MouseListener getMyMouseListener(){

            return new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {

                    JPanel jPanel = (JPanel)e.getSource();
                     
                    int id = Integer.parseInt(jPanel.getName());

                    for (Fichier leFichier : lesFichiers) {
                        if(leFichier.getId() == id){
                            JFrame window = creatFrame(leFichier.getNom(),leFichier.getDonnee(),leFichier.getFichierExtens());
                            window.setVisible(true);
                        }
                    }

                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            };
    }

    public static JFrame creatFrame(String nom,byte[] donnee,String fichierExtens){
        JFrame windows = new JFrame();
        windows.setSize(500,500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("obtenir fichier");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(20,0,10,0));

        JButton yes = new JButton("save");
        yes.setPreferredSize(new Dimension(150,75));

        JButton no = new JButton("cancel");
        no.setPreferredSize(new Dimension(150,75));

        JLabel fichierDown = new JLabel();
        fichierDown.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btns = new JPanel();
        btns.setBorder(new EmptyBorder(20,0,10,0));
        btns.add(yes);
        btns.add(no);

        if (fichierExtens.equalsIgnoreCase("txt")) {
            fichierDown.setText("<html>" + new String(donnee)+ "</html>");
        }
        else{
            fichierDown.setIcon(new ImageIcon(donnee));
        }

        yes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File downFile = new File(nom);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(downFile);
                    fileOutputStream.write(donnee);
                    fileOutputStream.close();
                    windows.dispose();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
            
        });

        no.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                windows.dispose();
            }
        });

        panel.add(label);
        panel.add(fichierDown);
        panel.add(btns);

        windows.add(panel);
        return windows;

    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if(i>0){
            return fileName.substring(i+1);
        }
        return "none";
    }

}