package server.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 12.05.13
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class AdminConsole {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter String");
        String s = br.readLine();
        System.out.print("Enter Integer:");
        try{
            int i = Integer.parseInt(br.readLine());
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
    }

}
