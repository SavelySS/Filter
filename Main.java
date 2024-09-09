import java.io.*;
import java.util.Scanner;

public class Main {
    public static String SaveDir = System.getProperty("user.dir");
    public static String [] nameArr = {"strings.txt", "integers.txt", "floats.txt"};
    public static boolean flagA = false, flagS = false, flagF = false, flagO = false, flagP = false;
    public static Integer colElString  = null, maxString = null, minString = null,
            colElInteger  = null, maxInteger = null, minInteger = null, sumInt = null, colElFloat = null;
    public static double maxDouble = Double.MIN_VALUE, minDouble = Double.MAX_VALUE, sumD;

    public static void main(String[] args) throws IOException {
        String flagPName = "", flagOPath = "";
        String [] nameInp = new String[args.length];
        int j = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o": flagO = true; flagOPath = args[++i]; break;
                case "-p": flagP = true; flagPName =  args[++i]; break;
                case "-a": flagA = true; break;
                case "-s": flagS = true; break;
                case "-f": flagF = true; break;
                default: nameInp[j++] = args[i]; break;
            }
        }

        if (flagO) {
            SaveDir = checkDir(flagOPath);
        }
        if (flagP) {
            for (int i = 0; i < nameArr.length; i++) {
                nameArr[i] = flagPName + nameArr[i];
            }
        }
        for (int i = 0; i < j; i++) {
            readWrite(nameInp[i]);
        }
        if (flagS | flagF) {
            if (colElString != null) {
                System.out.printf("Количество строчных: %s\n", colElString);
                if (flagF) {
                    System.out.printf("Максимальная длинна строки: %d знаков\n", maxString);
                    System.out.printf("Минимальная длинна строки: %d знаков\n", minString);
                }
            }
            if (colElInteger != null) {
                System.out.printf("Количество целых: %s\n", colElInteger);
                if (flagF) {
                    System.out.printf("Максимальное целое значение %d\n", maxInteger);
                    System.out.printf("Минимальное целое значение %d\n", minInteger);
                    System.out.printf("Сумма целых %d\n", sumInt);
                    System.out.printf("Ср. знач целых %.2f\n", (double)(sumInt) / colElInteger);
                }
            }
            if (colElFloat != null) {
                System.out.printf("Колчество вещественных: %s\n", colElFloat);
                if (flagF) {
                    System.out.printf("Максимальное вещественное значение %e\n", maxDouble);
                    System.out.printf("Минимальное вещественное значение %e\n", minDouble);
                    System.out.printf("Сумма вещественных %f\n", sumD);
                    System.out.printf("Ср. знач вещественных %.2f\n", sumD / colElFloat);
                }
            }
        }
    }
    public static String checkDir(String dir) {
        String currentDir = System.getProperty("user.dir") +"\\" + dir;
        System.out.println("Current dir using System:" + currentDir);
        File di = new File(currentDir);
        di.mkdirs();
        return currentDir;
    }
    public static void readWrite(String pathF) throws FileNotFoundException {
        try {
            File file = new File(pathF);
            Scanner sc = new Scanner(file);
            boolean flagInt = flagA, flagFloat = flagA, flagString = flagA;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                switch (checkType(line)) {
                    case "string": File fileString = new File(SaveDir + "\\" + nameArr[0]);
                        fileString.createNewFile();
                        try(FileOutputStream fos=new FileOutputStream(SaveDir + "\\" + nameArr[0], flagString)) {
                            fos.write((line+"\n").getBytes());
                        } flagString = true;
                        if(colElString == null) {
                            colElString = 1;
                            maxString = line.length();
                            minString = line.length();
                        }
                        else {
                            colElString += 1;
                            maxString = line.length() > maxString ? line.length() : maxString;
                            minString = line.length() < minString ? line.length() : minString;
                        } break;
                    case "int": File fileInt = new File(SaveDir + "\\" + nameArr[1]);
                        fileInt.createNewFile();
                        try(FileOutputStream fos=new FileOutputStream(SaveDir + "\\" + nameArr[1], flagInt)) {
                            fos.write((line+"\n").getBytes());
                        } flagInt = true;
                        if (colElInteger == null) {
                            colElInteger = 1;
                            maxInteger = Integer.parseInt(line);
                            minInteger = Integer.parseInt(line);
                            sumInt = Integer.parseInt(line);
                        }
                        else {
                            colElInteger += 1;
                            sumInt += Integer.parseInt(line);
                            maxInteger = Integer.parseInt(line) > maxInteger ? Integer.parseInt(line) : maxInteger;
                            minInteger = Integer.parseInt(line) < minInteger ? Integer.parseInt(line) : minInteger;
                        } break;
                    case "double": File fileFloat = new File(SaveDir + "\\" + nameArr[2]);
                        fileFloat.createNewFile();
                        try(FileOutputStream fos=new FileOutputStream(SaveDir + "\\" + nameArr[2], flagFloat)) {
                            fos.write((line+"\n").getBytes());
                        } flagFloat = true;
                        if (colElFloat == null) {
                            colElFloat = 1;
                            maxDouble = Double.parseDouble(line);
                            minDouble = Double.parseDouble(line);
                            sumD = Double.parseDouble(line);
                        }
                        else {
                            colElFloat += 1;
                            sumD += Double.parseDouble(line);
                            maxDouble = Math.max(Double.parseDouble(line), maxDouble);
                            minDouble = Math.min(Double.parseDouble(line), minDouble);
                        } break;
                    default: assert true; break;
                }
            }
            flagA = true;
        } catch (FileNotFoundException e) {
            System.out.printf("Ошибка! Файл %s не найден или не может быть прочитан.\n", pathF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String checkType(String obj) {
        if (obj == null || obj.isEmpty()) {
            return "empty";
        }
        try {
            int v = Integer.parseInt(obj);
            return "int";
        } catch (NumberFormatException nfe) {assert true;}
        try {
            double v = Double.parseDouble(obj);
            return "double";
        } catch (NumberFormatException nfe) {assert true;}
        try {
            String v = obj;
            return "string";
        } catch (NullPointerException npe) {assert true;}
        return "err";
    }
}
