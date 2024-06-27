package ro.usv;

import java.util.Objects;
import java.util.Scanner;
import java.util.SortedSet;

public class ComenziMotorDeCautare {
    public static void main(String[] args) {
        MiniMotorDeCautareImpl impl = new MiniMotorDeCautareImpl();
        //try{
        //Scanner scanner = new Scanner(new File("comenzi10.txt"));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            System.out.println(">>> " + line);
            String[] lineSplit = line.split(" ");
            switch (lineSplit[0].trim()) {
                case "build":
                    Pereche<Integer, Integer> pereche;
                    if (lineSplit.length == 2) {
                        pereche = impl.build(lineSplit[1]);
                    } else if(lineSplit.length == 3){
                        pereche = impl.build(lineSplit[1],lineSplit[2]);
                    }
                    else{
                        String[] split=new String[lineSplit.length-3];
                        System.arraycopy(lineSplit, 3, split, 0, lineSplit.length - 3);
                        pereche = impl.build(lineSplit[1],lineSplit[2],split);
                    }
                    System.out.println(pereche);
                    break;
                case "getNumberOfKeywords":
                    if (lineSplit.length == 1) {
                            System.out.println(impl.getNumberOfKeywords());
                    } else {
                                System.out.println(impl.getNumberOfKeywords(lineSplit[1].toLowerCase()));
                    }
                    break;
                case "getNumberOfDocuments":
                    if (lineSplit.length == 1) {
                        System.out.println(impl.getNumberOfDocuments());
                    } else {
                        System.out.println(impl.getNumberOfDocuments(lineSplit[1].toLowerCase()));
                    }
                    break;
                case "getKeywordsOfDocument":
                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                        System.out.println(impl.getKeywordsOfDocument(lineSplit[1]));
                    }
                    break;
                case "getDocumentsOfKeyword":
                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                        System.out.println(impl.getDocumentsOfKeyword(lineSplit[1].toLowerCase()));
                    }
                    break;
                case "printDetails":
                    impl.printDetails();
                    break;
                case "search":
                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                        SortedSet<String> result = impl.search(line.toLowerCase());
                        if (result.isEmpty()) {
                            System.out.println("Nu au fost gasite keywords");
                        } else
                            System.out.println(result);
                    }
                    break;
                case "stop":
                    System.exit(0);
                    break;
                case "addDocument":

                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {

                        System.out.println(    impl.addDocument(lineSplit[1]));
                    }
                    break;
                case "deleteDocument":

                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                      System.out.println( impl.deleteDocument(lineSplit[1]));
                    }
                    break;
                case "undeleteDocument":
                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                        System.out.println( impl.undeleteDocument(lineSplit[1]));
                    }
                    break;
                case "setShortDocName":
                    if (lineSplit.length == 1) {
                        System.out.println("Lipsa argument");
                    } else {
                        System.out.println(impl.setShortDocName(lineSplit[1]));
                    }
                    break;
                case "clear":
                    System.out.println(impl.clear());
                    break;
                default:
                    System.out.println(" Comanda necunoscuta: " + lineSplit[0]);
                    break;
            }
        }
        // }catch (FileNotFoundException e){
        //      System.out.println("Fisierul nu exista");
        //   }
    }
}
