package ro.usv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeSet;

public class Fisiere {
    public static File downloadFisier(String numefis){
        try{
            URL website = new URL(numefis);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            File fdest = new File(numefis.substring(numefis.lastIndexOf("/")+1));
            FileOutputStream fos = new FileOutputStream(fdest);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return fdest;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String extrageContinut(String numeFisierDocument) {
        try {
            return new String(Files.readAllBytes(Paths.get(numeFisierDocument)));
        } catch (IOException e) {
            System.err.println("A apărut o eroare la citirea fișierului: " + e.getMessage());
            return "";
        }
    }
}