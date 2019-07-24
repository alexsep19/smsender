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
//    Правление СНТ Черничка информирует о проведении общего собрания 27.07.19 в 14:00.Повестка дня:перевыборы председателя и членов правления.В случае не явки, необходимо оформить доверенность (бланки находятся у сторожа).На это смс не отвечать.
static String message = "Pravlenie SNT Chernichka informiruet o provedenii obschego sobraniya 27.07.19 v 14:00.Povestka dnya:perevyiboryi predsedatelya i chlenov pravleniya.V sluchae ne yavki, neobhodimo oformit doverennost (blanki nahodyatsya u storozha).Na eto sms ne otvechat".replace(" ","%20");

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
//        String url ="http://192.168.1.42:8080/recsms?username=xxx&password=xxx&phone=%s&message=%s";
        String curl = "curl -X \"POST\" ";
        String url = "\"http://192.168.1.42:8080/v1/sms?phone=%s&message=%s\"";
        int i = 1;

        for (String phone: phones){
            String post = curl + String.format(url, "8" + phone.substring(2), message);
            Thread.sleep(10000);
            System.out.println(post);
//            Process p = Runtime.getRuntime().exec(post);
//            p.waitFor();
//            if (p.exitValue() != 0) break;
            System.out.println("phones " + phone + " " + i++);
        }
    }

    public static void main( String[] args ) throws IOException, InterruptedException {
        System.out.println( "Started" );
        App app = new App();
        Set<String> phones = app.readPhones("g1.txt");
        sendSms(phones);
        System.out.println( "Finished " + phones.size() + " records");
    }
}
