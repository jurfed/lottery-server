import controllers.DBController;
import controllers.ServerController;
import org.hibernate.exception.JDBCConnectionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * The get method handles a request for previously saved data, and the post method for writing new data
 */
@WebServlet("/")
public class MainServlet extends HttpServlet {

    /**
     * The name of the file in which the serialized parameters are stored
     */
    private static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT";
    private Long credit;
    private Double sound;
    private String language;
    private String message;
    private ServerController serverController;
    private Map<String, String[]> requestParameters;
    private static DBController dBController;

    public MainServlet() {
        super();
        try{
            serverController = ServerController.getInstance();
            dBController = DBController.getInstance();
            dBController.createBD();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Request for getting previously saved data
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            getPostSynch(req, resp,true);
    }

    /**
     * Saving new data from user in file
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getPostSynch(req, resp,false);

    }


    protected synchronized void getPostSynch(HttpServletRequest req, HttpServletResponse resp, boolean get) throws IOException, ServletException {

        if(get){
            message = "";
            /**
             * Serialized Parameters File
             */
            String origin = req.getHeader("Origin");
            resp.setHeader("Access-Control-Allow-Origin", origin);
            resp.setHeader("Access-Control-Allow-Methods", VALID_METHODS);


            if (req.getParameter("webpage") != null) {
                try {
                    serverController.showUserParameters(req, resp);
                } catch (JDBCConnectionException e) {
                    req.setAttribute("message", "Error: No connection to data base");
                } catch (NullPointerException e) {
                    req.setAttribute("message", "Message: No data found");
                }
                req.getRequestDispatcher("parameters.jsp").forward(req, resp);
            } else {
                serverController.sendAnswer(req, resp);
                resp.getWriter().flush();
                resp.getWriter().close();
            }
        }else {
            message = "";
            /**
             * Map with received parameters for saving
             */
            String origin = req.getHeader("Origin");
            resp.setHeader("Access-Control-Allow-Origin", origin);
            resp.setHeader("Access-Control-Allow-Methods", VALID_METHODS);
            serverController.saveData(req, resp);
            if (req.getParameter("webpage") != null) {
                try {
                    req.setAttribute("message", serverController.getWebMessage());
                    serverController.showUserParameters(req, resp);
                } catch (JDBCConnectionException e) {
                    req.setAttribute("message", "Error: No connection to data base");
                } catch (NullPointerException e) {
                    req.setAttribute("message", "Error: No connection to data base");
                }
                req.getRequestDispatcher("parameters.jsp").forward(req, resp);
            } else {
                resp.getWriter().flush();
                resp.getWriter().close();
            }
        }

    }

}
