package ictclogin;

public class Main {
    public static void main(String[] args) {

        System.out.println("Attemping...");
        String result = "invalid arguments";
        if(args.length == 3) {
            if (args[0].equals("login")) {
                result = Ictclogin_functions.doLogin(args[1], args[2]);
            }
        } else if (args.length == 1){
            if (args[0].equals("logout")){
                result = Ictclogin_functions.doLogout();
            }
        }
        if (result.equals("ictc.hs"))
            System.out.println("Couldn't connect to ictc.");
        else
            System.out.println(result);
    }
}
