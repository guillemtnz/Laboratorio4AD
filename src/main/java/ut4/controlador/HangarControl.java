package ut4.controlador;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ut4.modelo.Nave;
import ut4.util.HibernateUtil;


public class HangarControl {

    // Guarda la nave en una transacción; rollback si falla
    public void registrarNuevaNave(Nave nave) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(nave);

            tx.commit();
            System.out.println("Nave registrada con ID: " + nave.getId());

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error al registrar la nave: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Busca la nave por ID y cambia su estado
    public void actualizarEstadoNave(int idNave, String nuevoEstado) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // get devuelve null si no existe; si existe, la nave queda persistente
            Nave naveRecuperada = session.get(Nave.class, idNave);

            if (naveRecuperada != null) {
                System.out.println("Nave localizada en el hangar: " + naveRecuperada);

                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    naveRecuperada.setEstadoSistema(nuevoEstado);

                  
                    tx.commit();
                    System.out.println("Estado actualizado a '" + nuevoEstado + "'.");

                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    System.err.println("Error en la modificación: " + e.getMessage());
                }

            } else {
                System.out.println("No se encontró ninguna nave con el ID especificado.");
            }

        } catch (Exception e) {
            System.err.println("Error al recuperar la nave: " + e.getMessage());
        }
    }

    // Consulta HQL parametrizada por modelo
    public List<Nave> obtenerCazasPorModelo(String modeloBuscado) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hqlFiltro = "FROM Nave n WHERE n.modelo = :modeloRef";

            Query<Nave> queryFiltro = session.createQuery(hqlFiltro, Nave.class);
            queryFiltro.setParameter("modeloRef", modeloBuscado);

            return queryFiltro.getResultList();

        } catch (Exception e) {
            System.err.println("Error en la consulta HQL: " + e.getMessage());
            return List.of();
        }
    }
}
