package ro.usv;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniMotorDeCautareImpl implements IMiniMotorDeCautare {

    TreeMap<String, SortedSet<String>> mapDocToKeys = new TreeMap<>();
    TreeMap<String, SortedSet<String>> mapKeysToDoc = new TreeMap<>();
    TreeMap<String, SortedSet<String>> inactiveMapDocToKeys = new TreeMap<>();
    ArrayList<String> extensions = new ArrayList<>(Arrays.asList("txt", "cpp", "java", "c", "xml", "csv", "html",
            "css", "js","py") );


    private boolean dontShowPath = false;

    @Override
    public Pereche<Integer, Integer> build(String numeFisSetSimplificat) {
        try {

            Pereche<Integer, Integer> pereche = new Pereche<>(0, 0);
            Scanner scanner = new Scanner(new File(numeFisSetSimplificat));

            while (scanner.hasNextLine()) {
                String line1 = scanner.nextLine();
                try {
                    String line2 = scanner.nextLine();
                    //    String[] keys=line2.split(sep);
                    SortedSet<String> keywords = new AnalizaText(line2, true).getCuvDist();
                    // System.out.println(Arrays.toString(keys));
                    mapDocToKeys.put(line1, new TreeSet<>());
                    for (String s : keywords
                    ) {
                        s = s.toLowerCase().trim();
                        if (!Objects.equals(s, "")) {
                            mapDocToKeys.get(line1).add(s);
                            if (!mapKeysToDoc.containsKey(s)) {
                                mapKeysToDoc.putIfAbsent(s, new TreeSet<>());

                            }
                            mapKeysToDoc.get(s.toLowerCase()).add(line1);
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Lipsa continut pentru documentul " + line1);
                    break;
                }
            }
            pereche.n1 = mapDocToKeys.size();
            pereche.n2 = mapKeysToDoc.size();
            return pereche;
        } catch (FileNotFoundException e) {
            System.out.println("No file found");
            return null;
        }
    }

    @Override
    public int getNumberOfKeywords() {
        return mapKeysToDoc.keySet().size();
    }

    @Override
    public int getNumberOfDocuments() {
        return mapDocToKeys.keySet().size();
    }

    @Override
    public int getNumberOfKeywords(String document) {
        return mapDocToKeys.get(document).size();
    }

    @Override
    public SortedSet<String> getKeywordsOfDocument(String document) {
        if (!mapDocToKeys.containsKey(document)) {
            return new TreeSet<>();
        }
        return mapDocToKeys.get(document);
    }

    @Override
    public int getNumberOfDocuments(String cuvantCheie) {
        return mapKeysToDoc.get(cuvantCheie).size();
    }

    @Override
    public SortedSet<String> getDocumentsOfKeyword(String cuvantCheie) {
        if (!mapKeysToDoc.containsKey(cuvantCheie)) {
            return new TreeSet<>();
        }
        if (dontShowPath) {
            SortedSet<String> set = new TreeSet<>();
            for (String s : mapKeysToDoc.get(cuvantCheie)
            ) {
                set.add(getNameFromPath(s));
            }
            return set;
        }
        return mapKeysToDoc.get(cuvantCheie);
    }

    @Override
    public void printDetails() {
        System.out.println("Numar documente: " + getNumberOfDocuments());
        int i = 0;
        for (String s : mapDocToKeys.keySet()) {
            if (dontShowPath) {
                System.out.println((i + 1) + ". " + getNameFromPath(s));
            } else {
                System.out.println((i + 1) + ". " + s);
            }
            i++;
        }
        i = 0;
        System.out.println("Numar cuvinte cheie: " + getNumberOfKeywords());
        for (String s : mapKeysToDoc.keySet()) {
            System.out.println((i + 1) + ". " + s + " - " + mapKeysToDoc.get(s).size() + " doc., active: " + mapKeysToDoc.get(s));
            i++;
        }
    }

    @Override
    public SortedSet<String> search(String interogare) {
        SortedSet<String> set;
        String[] cuvinte = interogare.split(" ");

        for (int i = 0; i < cuvinte.length - 1; i++) {
            cuvinte[i] = cuvinte[i + 1];
        }

        // Ajustam lungimea vectorului
        cuvinte = Arrays.copyOf(cuvinte, cuvinte.length - 1);

        if (cuvinte.length == 0) {
            return new TreeSet<>();
        }
        if (cuvinte.length == 1) {
            if (!cuvinte[0].contains("-")) {
                set =new TreeSet<>( mapKeysToDoc.get(cuvinte[0]));
            } else {
                set = (SortedSet<String>) mapDocToKeys.keySet();
                set.removeAll(mapKeysToDoc.get(cuvinte[0].substring(1)));
            }

        } else {
            if (!(interogare.contains("+") || interogare.contains("-"))) {

                set =new TreeSet<>( mapKeysToDoc.get(cuvinte[0]));

                for (int i = 1; i < cuvinte.length; i++) {
                    SortedSet<String> set2 = mapKeysToDoc.get(cuvinte[i]);
                    if(set2!=null) {
                    set.retainAll(set2);
                    }
                    else{
                        set.clear();
                    }
                }
            } else {

                set =new TreeSet<>( mapKeysToDoc.get(cuvinte[0]));
                for (int i = 1; i < cuvinte.length; i++) {

                    SortedSet<String> set2;
                    if (cuvinte[i].contains("+")) {
                        set2 = mapKeysToDoc.get(cuvinte[i].substring(1));
                        set.addAll(set2);
                    } else if (cuvinte[i].contains("-")) {
                        set2 = mapKeysToDoc.get(cuvinte[i].substring(1));

                      //  set.retainAll(mapDocToKeys.keySet());
                        set.removeAll(set2);
                    } else {
                        set2 = mapKeysToDoc.get(cuvinte[i]);
                        if(set2!=null) {
                            set.retainAll(set2);
                        }
                        else{
                            set.clear();
                        }
                    }
                }
            }
        }
        if (dontShowPath) {
            set.forEach(s -> s = getNameFromPath(s));
        }
        return set;

    }

    String eliminaCuvinteCuCaracter(int index,String continut){
        if(!Character.isLetterOrDigit(continut.charAt(index))) {

        }
        return continut;
    }

   ArrayList<Character> diacritics = new ArrayList<>(List.of('ă','î','â','ș','ţ'));
    public String eliminaCuvinte(SortedSet<String> cuvinteDeEliminat, String continut) {
        String regexSplit = "[^a-zA-Z0-9_@$ăîâșţ]+";

       StringBuilder sb = new StringBuilder();
       String[] strings = continut.split(regexSplit);
       for(String s : strings){
           if(!cuvinteDeEliminat.contains(s)){
               sb.append(s).append(" ");           }
       }
        return sb.toString();

    }

    @Override
    public Pereche<Integer, Integer> build(String dir, String fisierStopWords) {
        //build comenzi5.txt stopwords-ro.txt
        File director = new File(dir);
        if (director.isDirectory()) {
            File[] fisiere = director.listFiles();
            if (fisiere != null) {
                for (File fisier : fisiere) {
                    if (fisier.isDirectory()) {
                        build(fisier.getPath(), fisierStopWords);
                    } else {
                        if (fisier.isFile()) {
                            String[] path=fisier.getPath().split("[.]");
                            String extension=path[path.length-1];
                            if(extensions.contains(extension)){
                                if(!fisierStopWords.equalsIgnoreCase("null")){
                                addDocument(fisier.getPath(), Fisiere.extrageContinut(fisier.getPath()), getFiserStopWords(fisierStopWords));
                                }
                                else{
                                    addDocument(fisier.getPath());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (director.isFile()) {
                String[] path=director.getPath().split("[.]");
                String extension=path[path.length-1];
                if(extensions.contains(extension)){
                    if(!fisierStopWords.equalsIgnoreCase("null")){
                        addDocument(director.getPath(), Fisiere.extrageContinut(director.getPath()), getFiserStopWords(fisierStopWords));
                    }
                    else{
                        addDocument(director.getPath());
                    }
                }
            } else {
                System.out.println("Calea specificată nu este un director.");
            }
        }
        return new Pereche<>(mapDocToKeys.size(),  mapKeysToDoc.size());
    }

    Set<String> getFiserStopWords(String fiserStopWords) {
        try {
            Scanner scanner =new Scanner(new File(fiserStopWords));
            TreeSet<String> stopWords = new TreeSet<>();
            while (scanner.hasNext()){
              stopWords.add(scanner.nextLine());
            }
            return stopWords;
        } catch (FileNotFoundException e) {
            System.out.println("Fiserul de stopwords nu exista");
            return new TreeSet<>();
        }

    }

    @Override
    public Pereche<Integer, Integer> build(String dir, String fisierStopWords, String[] alteExtensii) {
        extensions.addAll(Arrays.asList(alteExtensii));
        Pereche<Integer,Integer> pereche=build(dir, fisierStopWords);
        extensions.removeAll(Arrays.asList(alteExtensii));
        return pereche;
    }

    @Override
    public boolean addDocument(String numeFisierDocument) {
        String continutCitit = Fisiere.extrageContinut(numeFisierDocument);
        return   addDocument(numeFisierDocument, continutCitit, null);
    }

    @Override
    public boolean addDocument(String numeDoc, String continut, Set<String> stopWords) {


        SortedSet<String> keywords = new AnalizaText(continut, true).getCuvDist();

        if (stopWords != null) {
            keywords.removeAll(stopWords);
            if (continut.isEmpty()) {
                return false;
            }
        }


        if (keywords.isEmpty()) {
            return false;
        }

        for (String s : keywords) {
            s = s.toLowerCase().trim();
            mapDocToKeys.putIfAbsent(numeDoc, new TreeSet<>());
            mapDocToKeys.get(numeDoc).add(s);
            mapKeysToDoc.putIfAbsent(s, new TreeSet<>());
            mapKeysToDoc.get(s).add(numeDoc);

          }

        return true;
    }

    @Override
    public boolean deleteDocument(String numeDocument) {
        if (!mapDocToKeys.containsKey(numeDocument)) {
            return false;
        }
        inactiveMapDocToKeys.put(numeDocument, mapDocToKeys.get(numeDocument));
        mapDocToKeys.remove(numeDocument);
        for (String s : inactiveMapDocToKeys.get(numeDocument)
        ) {
            mapKeysToDoc.get(s).remove(numeDocument);
            if (mapKeysToDoc.get(s).isEmpty()) {
                mapKeysToDoc.remove(s);
            }
        }
        return true;
    }

    @Override
    public boolean undeleteDocument(String numeDocument) {
        mapDocToKeys.put(numeDocument, inactiveMapDocToKeys.get(numeDocument));
        inactiveMapDocToKeys.remove(numeDocument);
        for (String s : mapDocToKeys.get(numeDocument)
        ) {
            mapKeysToDoc.putIfAbsent(s, new TreeSet<>());
            mapKeysToDoc.get(s).add(numeDocument);
        }
        return true;
    }

    @Override
    public boolean renameDocument(String numeDocument1, String numeDocument2) {
        SortedSet<String> set = mapDocToKeys.get(numeDocument1);
        mapDocToKeys.remove(numeDocument1);
        mapDocToKeys.put(numeDocument2, set);
        for (String s : set
        ) {
            mapKeysToDoc.get(s).remove(numeDocument1);
            mapKeysToDoc.get(s).add(numeDocument2);
        }
        return true;
    }

    @Override
    public Pereche<Integer, Integer> clear() {
        mapKeysToDoc.clear();
        mapDocToKeys.clear();
        inactiveMapDocToKeys.clear();
        return new Pereche<>(mapDocToKeys.size(), mapKeysToDoc.size());
    }

    @Override
    public boolean setShortDocName(String faraCaleCompleta) {
        dontShowPath = faraCaleCompleta.trim().equalsIgnoreCase("true");
        return dontShowPath;
    }

    public String getNameFromPath(String path) {
        String[] lines = path.split("\\\\");
        return lines[lines.length - 1];
    }

}
