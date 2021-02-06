import org.openqa.selenium.Keys;

import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class Bots {

    public static Function<Find, Exception> algoritimoExecutavel = null;
    public static boolean paginaVisivel = false;
    public static int numeroDeExecucoes;
    public static int numeroDeThreads;
    public static int tempoDeEsperaDriver = 100;

    public static String email = "***@gmail.com";
    public static String senha = "***";

    //localProject->

    public static void main(String[] args) {

        System.out.println("Número de execuções: ?");
        Scanner input = new Scanner(System.in);
        int num = input.nextInt();

        List<String> urls = new ArrayList<>(getBanco());
        int div = (urls.size())/num;

        numeroDeExecucoes = num;
        numeroDeThreads = num;

        System.out.println("Total em banco: " +urls.size());
        System.out.println("Processos por Thread: " +div);

        algoritimoExecutavel = find -> {
            try { find.init(paginaVisivel); } catch (AWTException e) { e.printStackTrace(); }
            /*Area de Código*/

            find.time(find.getNumero()*10000);
            find.page("https://www.facebook.com");
            find.one("i email").sendKeys(email);
            find.one("i pass").sendKeys(senha);
            find.one("i pass").sendKeys(Keys.ENTER);
            find.waitWhileDisable("css div[role='main']");

            for(int i = 0; i < div; i++){
                try {
                    find.page(urls.get(i + (div * find.getNumero())));

                    if (find.visible("css div[aria-label='Adicionar'] > div [class='hu5pjgll op6gxeva']")) {

                        find.one("css div[aria-label='Adicionar'] > div [class='hu5pjgll op6gxeva']").click();
                        find.time(5000);
                        infoPrint(1, find.one("css h1[class='gmql0nx0 l94mrbxd p1ri9a11 lzcic4wl bp9cbjyn j83agx80']").getText(), find.getNumero());

                    }else if(find.visible("css div[aria-label='Cancelar solicitação']")){

                        infoPrint(2, find.one("css h1[class='gmql0nx0 l94mrbxd p1ri9a11 lzcic4wl bp9cbjyn j83agx80']").getText(), find.getNumero());

                    }else{

                        infoPrint(0, find.one("css h1[class='gmql0nx0 l94mrbxd p1ri9a11 lzcic4wl bp9cbjyn j83agx80']").getText(), find.getNumero());

                    }

                }catch (Exception ignore){
                    infoPrint(0, "off", find.getNumero());
                }
            }

            /*Area de Código*/
            return null;
        };
        new OrdenarThreads();
    }

    private static List<String> getBanco(){
        try {
            String url = "jdbc:mysql://localhost/infofind";
            Connection connection = DriverManager.getConnection(url,"root","abc");

            PreparedStatement pstmt = connection.prepareStatement("SELECT DISTINCT url FROM pessoasnofilter;");
            ResultSet resultSet = pstmt.executeQuery();

            List<String> uList = new ArrayList<>();

            while (resultSet.next()) {
                uList.add(resultSet.getString("url"));
            }

            return uList;
        }catch (SQLException e) {

        }
        return null;
    }

    private static synchronized void infoPrint(int sit, String nome, int numero){
        if(sit == 0){
            System.out.println(+numero+": - | "+nome);
        }else if(sit == 1){
            System.out.println(+numero+": + | "+nome);
        }else if (sit == 2){
            System.out.println(+numero+": = | "+nome);
        }
    }
}
