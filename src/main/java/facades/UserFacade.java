package facades;

import entity.Role;
import entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import errorhandling.AuthenticationException;
import errorhandling.UserException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {
  
    private static EntityManagerFactory emf;
    private static UserFacade instance;
    
    private UserFacade(){}
    
    /**
     * 
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User createUser(String username, String password) throws UserException {
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("user"));
        User user = new User(username,password);
        user.setRoleList(roleList);
        EntityManager entityManager = emf.createEntityManager();
        try {
            boolean notInUse = (entityManager.find(User.class, username) == null);
            if(notInUse) {
                entityManager.getTransaction().begin();
                entityManager.persist(user);
                entityManager.getTransaction().commit();
            }
            else throw new UserException(UserException.IN_USE_USERNAME);
        }
        finally {
            entityManager.close();
        }
        return user;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

}
