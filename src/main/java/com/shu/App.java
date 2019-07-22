package com.shu;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class App
{
    Pattern phoneMask = Pattern.compile("^\\+7\\d{10}$");

    private Set<String> readPhones(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();
        Set<String> strings = new HashSet<String>();
        String line = null;
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(fileName), StandardCharsets.UTF_8))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (phoneMask.matcher(line).matches()){
                    strings.add(line);
                }else{
                    System.out.println("Error phone " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    static private void sendSms(Set<String> phones) throws IOException, InterruptedException {
        for (String phone: phones){
            Process p = Runtime.getRuntime().exec("test.cmd");
            p.waitFor();
            System.out.println(p.exitValue());
            System.out.println("phones " + phone);
        }
    }

    public static void main( String[] args ) throws IOException, InterruptedException {
        System.out.println( "Started" );
        App app = new App();
        Set<String> phones = app.readPhones("ph.txt");
        sendSms(phones);
        System.out.println( "Finished " + phones.size() + " records");
    }
}
