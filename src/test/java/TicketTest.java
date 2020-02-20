import com.google.gson.Gson;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.provider.FFIProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ticketfactory.TicketGeneratorRunner;

public class TicketTest {


    @Test
    public void createInstanceTest() {
        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
//        FreeKick/300/10/3
        //FreeKick/200/10/200
        String answerTicket = ticketGeneratorRunner.generateTickets("FreeKick", 200,201,3);
        System.out.println(answerTicket);

    }

    @Test
    public void createInstanceTest2() {
        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();

        String answerTicket = ticketGeneratorRunner.getGamesInfo();
        System.out.println(answerTicket);
    }

    @Test
    public void createInstanceTest3() {
        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();

        //String answerTicket = ticketGeneratorRunner.getRandomTicket("FreeKick", 100);
        //System.out.println(answerTicket);
    }


    @After
    public void destroy() {
        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
        ticketGeneratorRunner.destroy();
    }



}
