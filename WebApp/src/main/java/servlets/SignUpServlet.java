package servlets;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFactory;
import dao.SaltDao;
import dao.SaltsDaoFactoryCreator;
import dao.UserDao;
import dao.UsersPasswordsDaoFactoryCreator;
import model.Salt;
import model.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("sign-up.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * FASE DI REGISTRAZIONE:
		 * 
		 * 1. L'utente invia le credenziali scelte: email, password ed eventualmente foto profilo
		 * 2. Le credenziali vengono recuperate dall'oggetto request
		 * 3. Viene chiamato un metodo in JDBCLoginDao che permette di inserire le credenziali scelte nel database
		 * 
		 * */
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		byte[] photo = request.getParameter("photo").getBytes();
		
		
		User user = new User(email, photo);
		
		Optional<User> insertedUser = null;
		
		boolean insertedSalt = false;
		
		
		try {
			
			/*
			 * Si apre una connessione verso il database users_passwords_db
			 * 
			 * */
			
			DaoFactory daoFactory = UsersPasswordsDaoFactoryCreator.getDaoFactory();
			UserDao loginDao = daoFactory.getUserDao();

			/*
			 * TODO: Gestire il caso di errore di email già esistente
			 * 
			 * */
			
			insertedUser = loginDao.signUp(user, password);
			
			if(insertedUser.isPresent()) {
				
				System.out.println("Inserimento utente avvenuto con successo");
				
				/*
				 * Ottenuto l'utente, faccio una query di INSERT sul database salts
				 * 
				 * */
				
		        DaoFactory saltDaoFactory = SaltsDaoFactoryCreator.getDaoFactory();
		        SaltDao saltDao = saltDaoFactory.getSaltDao();
		        
		        insertedSalt = saltDao.insertSaltIntoDB(new Salt(user.getId(), user.getSalt().getSalt()));
		        
		        if(insertedSalt)
		        	System.out.println("Intero inserimento avvenuto con successo");
		        else
		        	System.out.println("Intero inserimento fallito");
				
			}
			else
				System.out.println("Errore nell'inserimento");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		doGet(request, response);
		
	}

}
