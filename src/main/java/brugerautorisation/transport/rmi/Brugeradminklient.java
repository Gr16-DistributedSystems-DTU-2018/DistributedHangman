package brugerautorisation.transport.rmi;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Diverse;

import java.rmi.Naming;

public class Brugeradminklient {

    private static String studieId = "s151641";
    private static String studieKode = "godkode";

    public static void main(String[] arg) throws Exception {

        //ba.sendGlemtAdgangskodeEmail("s123456", "Dette er en test, husk at skifte kode");
        //ba.ændrAdgangskode("s123456", "kode1xyz", "kode1xyz");
        //ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
        //ba.setEkstraFelt(studieId, studieKode, "Job", "Krappemand");
        //Object ekstraFelt = ba.getEkstraFelt(studieId, studieKode, "Job");
        //System.out.println("Brugerens Job er: " + ekstraFelt);
        //ba.setEkstraFelt("s123456", "kode1xyz", "hobby", "Tennis og programmering"); // Skriv noget andet her

        //String webside = (String) ba.getEkstraFelt("s123456", "kode1xyz", "webside");
        //System.out.println("Brugerens webside er: " + webside);

        /* Get the RMI Stub by name lookup */
        //Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
        Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        /* Get the user */
        Bruger b = ba.hentBruger(studieId, studieKode);
        System.out.println("Fik bruger = " + b);
        System.out.println("Data: " + Diverse.toString(b));
    }

}